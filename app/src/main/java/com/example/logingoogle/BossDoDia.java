package com.example.logingoogle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import java.util.concurrent.ExecutionException;

public class BossDoDia extends AppCompatActivity {
    Button buttonFav;
    RecyclerView recyclerView;
    ArrayList<Bosses> bossesArrayList = new ArrayList<>();
    private BossesRecyclerAdapter bossesRecyclerAdapter;
    Handler handler = new Handler();
    String url = "https://api.tibiadata.com/v4/boostablebosses";  // URL padrão para listar todas as criaturas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_do_dia);
        recyclerView = findViewById(R.id.recyclerView);
        buttonFav = findViewById(R.id.btnFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Adicione esta linha para definir o layout manager


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
                Toast.makeText(BossDoDia.this, "Erro ao ler dados do banco de dados", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuração do botão "Voltar"
        Button btnVoltar = findViewById(R.id.btnVoltarR);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BossDoDia.this, Entrada.class);
                startActivity(intent);
            }
        });

        // Chamando o método para obter dados da API e carregar a lista
        loadBossList();

    }

    private void loadBossList() {
        try {
            PegaJBDdados pegaJBDdados = new PegaJBDdados();
            pegaJBDdados.setOnDataReadyListener(new PegaJBDdados.OnDataReadyListener() {
                @Override
                public void onDataReady(Bosses boostedBoss) {
                    if (boostedBoss != null) {
                        bossesArrayList.clear();
                        bossesArrayList.add(boostedBoss);

                        // Inicialize a instância do adaptador se ainda não estiver inicializada
                        if (bossesRecyclerAdapter == null) {
                            bossesRecyclerAdapter = new BossesRecyclerAdapter(bossesArrayList);
                            recyclerView.setAdapter(bossesRecyclerAdapter);
                        } else {
                            // Apenas notifique as mudanças se o adaptador já estiver inicializado
                            bossesRecyclerAdapter.notifyDataSetChanged();
                        }
                    } else {
                        // Trate o caso em que não há chefe "boosted"
                        // Por exemplo, exiba uma mensagem ou tome alguma ação apropriada
                        Toast.makeText(BossDoDia.this, "Nenhum chefe 'boosted' encontrado.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Adicione esta linha para iniciar a execução da tarefa assíncrona
            pegaJBDdados.execute(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
