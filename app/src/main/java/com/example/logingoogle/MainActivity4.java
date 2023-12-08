package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity4 extends AppCompatActivity {

    EditText editTextNome, editTextEmail, editTextPassword;
    Button buttonCreate;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        editTextNome = findViewById(R.id.editTextNome);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCreate = findViewById(R.id.criar);
        firebaseAuth = getFirebaseAuthInstance();

        buttonCreate.setOnClickListener(view -> {
            String nome = editTextNome.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (nome.isEmpty()) {
                Toast.makeText(MainActivity4.this, "Por favor, insira um nome", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(MainActivity4.this, "Por favor, insira um e-mail", Toast.LENGTH_SHORT).show();
            } else if (isValidEmail(email)) {
                createFirebaseUser(nome, email, password);
            } else {
                Toast.makeText(MainActivity4.this, "Email inválido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void createFirebaseUser(String nome, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity4.this, "Usuário criado", Toast.LENGTH_SHORT).show();

                        // Obtenha a referência do banco de dados
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                        // Crie um nó de usuários usando o UID do usuário recém-criado
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference userReference = databaseReference.child("users").child(uid);

                        // Adicione o nome do usuário ao banco de dados
                        userReference.child("nome").setValue(nome);

                        // Navegue para a tela inicial (ou outra tela) após o sucesso
                        Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Opcional: encerrar a atividade atual se não quiser que o usuário volte para a tela de registro
                    } else {
                        Toast.makeText(MainActivity4.this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
