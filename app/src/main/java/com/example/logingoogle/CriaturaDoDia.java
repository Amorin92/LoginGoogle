package com.example.logingoogle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CriaturaDoDia extends AppCompatActivity {

    Button buttonFav;
    RecyclerView recyclerView;
    ArrayList<Creature> creatureArrayList = new ArrayList<>();
    Handler handler = new Handler();
    RecyclerAdapter recyclerAdapter;

    String url = "https://api.tibiadata.com/v4/creatures";  // URL padrão para listar todas as criaturas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criatura_do_dia);

        recyclerView = findViewById(R.id.recyclerView);
        buttonFav = findViewById(R.id.btnFav);

        TextView textEmail = findViewById(R.id.textEmail);

        // Obtenha a referência do banco de dados
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        // Obtenha o UID do usuário atual
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Crie uma referência para o nó do usuário no banco de dados
        DatabaseReference userReference = databaseReference.child("users").child(uid);

        // Adicione um ouvinte para obter o nome do usuário
        userReference.child("nome").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nomeUsuario = dataSnapshot.getValue(String.class);

                    // Defina o texto do botão com o nome do usuário
                    buttonFav.setText("Favoritos " + nomeUsuario);
                    textEmail.setText("Olá: " + nomeUsuario);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CriaturaDoDia.this, "Erro ao ler dados do banco de dados", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuração do botão "Voltar"
        Button btnVoltar = findViewById(R.id.btnVoltarR);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CriaturaDoDia.this, Entrada.class);
                startActivity(intent);
            }
        });

        // Chamando o método para obter dados da API e carregar a lista
        loadCreatureList();

        // Configuração da RecyclerView
        setRecyclerView();

    }

    private void loadCreatureList() {
        try {
            PegaJCDdados pegaJCDdados = new PegaJCDdados();
            pegaJCDdados.setOnDataReadyListener(new PegaJCDdados.OnDataReadyListener() {
                @Override
                public void onDataReady(ArrayList<Creature> creatures) {
                    if (creatures != null && !creatures.isEmpty()) {
                        // Limpa a lista
                        creatureArrayList.clear();

                        // Adiciona todas as criaturas à lista
                        creatureArrayList.addAll(creatures);

                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
            pegaJCDdados.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerView() {
        // Instanciando a classe RecyclerAdapter com a lista de criaturas
        recyclerAdapter = new RecyclerAdapter(creatureArrayList);

        // Setando a recyclerView para correta exibição com a lista de criaturas
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
