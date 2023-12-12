package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListBosses extends AppCompatActivity {

    Button buttonFav;
    RecyclerView recyclerView;
    ArrayList<Bosses> bossesArrayList = new ArrayList<>();
    Handler handler = new Handler();
    BossesRecyclerAdapter bossesRecyclerAdapter;

    String url = "https://api.tibiadata.com/v4/boostablebosses";  // URL padrão para listar todas as criaturas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listbosses);

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
                Toast.makeText(ListBosses.this, "Erro ao ler dados do banco de dados", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuração do botão "Voltar"
        Button btnVoltar = findViewById(R.id.btnVoltarR);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListBosses.this, Entrada.class);
                startActivity(intent);
            }
        });

        // Chamando o método para obter dados da API e carregar a lista
        loadBossList();

        // Configuração da RecyclerView
        setRecyclerView();

        // Ir para a tela com os favoritos do usuário:
        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListBosses.this, BossesFavoritos.class);
                startActivity(i);
            }
        });
    }

    private void loadBossList() {
        try {
            PegaBdados pegaBdados = new PegaBdados();
            pegaBdados.setOnDataReadyListener(new PegaBdados.OnDataReadyListener() {
                @Override
                public void onDataReady(ArrayList<Bosses> bosses) {
                    if (bosses != null) {
                        bossesArrayList.clear();
                        bossesArrayList.addAll(bosses);
                        Log.e("Informações baixadas:", String.valueOf(bossesArrayList.size()));

                        // Atualize a instância do adapter e notifique as mudanças
                        bossesRecyclerAdapter = new BossesRecyclerAdapter(bossesArrayList);
                        recyclerView.setAdapter(bossesRecyclerAdapter);
                        bossesRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
            pegaBdados.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerView() {
        // Instanciando a classe BossesRecyclerAdapter com o ArrayList populado
        bossesRecyclerAdapter = new BossesRecyclerAdapter(bossesArrayList);

        // Setando a recyclerView para correta exibição com os itens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bossesRecyclerAdapter);
    }
}
