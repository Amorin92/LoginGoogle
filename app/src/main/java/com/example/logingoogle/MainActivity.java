package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
    Button buttonCreate, buttonLogin, buttonReset;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTextNome);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonCreate = findViewById(R.id.criar);
        buttonLogin = findViewById(R.id.acessar);
        buttonReset = findViewById(R.id.resetar);
        firebaseAuth = getFirebaseAuthInstance();

        buttonCreate.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity4.class);
            startActivity(intent);
        });

        buttonLogin.setOnClickListener(view -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, insira um e-mail", Toast.LENGTH_SHORT).show();
            } else {
                firebaseLogin(email, password);
            }
        });

        buttonReset.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }

    private FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void createFirebaseUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Usuário criado", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        sendEmailVerification(user);
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao criar usuário", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Email de verificação enviado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Erro ao enviar email de verificação", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void firebaseLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Usuário logado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, Entrada.class);
                        intent.putExtra("email_digitado", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
