package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    private EditText editTextEmailReset;
    private Button btResetarSenha, btVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editTextEmailReset = findViewById(R.id.editTextEmailReset);
        btResetarSenha = findViewById(R.id.buttonReset);
        btVoltar = findViewById(R.id.btVoltar); // Corrigir a referência do botão "Voltar"

        btResetarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailReset.getText().toString();

                if (!TextUtils.isEmpty(email)) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity2.this, "E-mail de recuperação de senha enviado.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity2.this, "Falha ao enviar e-mail de recuperação de senha.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    editTextEmailReset.setError("Informe o e-mail");
                }
            }
        });

        btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
