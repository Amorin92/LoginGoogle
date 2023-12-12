package com.example.logingoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Rashid extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rashid);

        // Configuração do ImageView
        ImageView imageView6 = findViewById(R.id.imageView6);
        Glide.with(this).asGif().load(R.drawable.rashid).into(imageView6);

        // Configuração do TextView
        TextView textView3 = findViewById(R.id.textView3);

        // Inicia o serviço para obter a cidade de Rashid
        new RashidService(textView3).execute();

        // Configuração do botão "Voltar"
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnVoltar = findViewById(R.id.btnVoltarR);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rashid.this, Entrada.class);
                startActivity(intent);
            }
        });
    }
}
