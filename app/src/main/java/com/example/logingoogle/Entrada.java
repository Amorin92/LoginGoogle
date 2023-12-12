package com.example.logingoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class Entrada extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrada);

        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView4 = findViewById(R.id.imageView4);
        ImageView imageView5 = findViewById(R.id.imageView5);

        // Carregando e exibindo os GIFs animados usando a biblioteca Glide
        Glide.with(this).asGif().load(R.drawable.albino_dragon).into(imageView2);
        Glide.with(this).asGif().load(R.drawable.ascending_ferumbras).into(imageView4);
        Glide.with(this).asGif().load(R.drawable.rashid).into(imageView5);

        Button buttonCriaturas = findViewById(R.id.button);
        Button buttonBosses = findViewById(R.id.button2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btBoss = findViewById(R.id.buttonBoss);
        Button buttonCreature = findViewById(R.id.button3);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnRashid = findViewById(R.id.btnRashid_);




        buttonCriaturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicie a MainActivity3
                Intent intent = new Intent(Entrada.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        buttonBosses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicie a ListBosses
                Intent intent = new Intent(Entrada.this, ListBosses.class);
                startActivity(intent);
            }
        });

        btBoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicie a BossDoDia
                Intent intent = new Intent(Entrada.this, BossDoDia.class);
                startActivity(intent);
            }
        });

        buttonCreature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicie a BossDoDia
                Intent intent = new Intent(Entrada.this, CriaturaDoDia.class);
                startActivity(intent);
            }
        });

        btnRashid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicie a BossDoDia
                Intent intent = new Intent(Entrada.this, Rashid.class);
                startActivity(intent);
            }
        });

        // Configuração do botão "Voltar"
        Button btnVoltar = findViewById(R.id.button6);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(Entrada.this, "Você saiu!", Toast.LENGTH_LONG).show();

                startActivity(new Intent(Entrada.this, MainActivity.class));
            }
        });
    }
}