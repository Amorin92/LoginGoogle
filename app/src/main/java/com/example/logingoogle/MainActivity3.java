package com.example.logingoogle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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

public class MainActivity3 extends AppCompatActivity {

    Button buttonFav;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Creature> creatureArrayList = new ArrayList<>();
    Handler handler = new Handler();
    RecyclerAdapter recyclerAdapter;

    String url = "https://api.tibiadata.com/v4/creatures";  // URL padrão para listar todas as criaturas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.recyclerView);
        buttonFav = findViewById(R.id.btnFav);
        searchView = findViewById(R.id.searchView1);

        // SearchView sempre aberto
        searchView.setIconified(false);
        // Retira o foco automático e fecha o teclado ao iniciar a aplicação
        searchView.clearFocus();

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
                Toast.makeText(MainActivity3.this, "Erro ao ler dados do banco de dados", Toast.LENGTH_SHORT).show();
            }
        });


        // Configuração do botão "Voltar"
        Button btnVoltar = findViewById(R.id.btnVoltarR);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this, Entrada.class);
                startActivity(intent);
            }
        });

        // Chamando o método para obter dados da API e carregar a lista
        loadCreatureList();

        // Configuração da RecyclerView
        setRecyclerView();

        // Configuração do SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                try {
                    setInfo(s);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Código igual ao submit acima:
                        try {
                            if (s.isEmpty()) {
                                loadCreatureList();  // Se o texto estiver vazio, carrega a lista completa
                            } else {
                                setInfo(s);
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 400);

                return true;
            }
        });

        // Ir para a tela com os favoritos do usuário:
        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity3.this, UsuarioFavoritos.class);
                startActivity(i);
            }
        });
    }

    private void loadCreatureList() {
        try {
            PegaJdados pegaJdados = new PegaJdados();
            pegaJdados.setOnDataReadyListener(new PegaJdados.OnDataReadyListener() {
                @Override
                public void onDataReady(ArrayList<Creature> creatures) {
                    if (creatures != null) {
                        creatureArrayList.clear();
                        creatureArrayList.addAll(creatures);
                        Log.e("Informações baixadas:", String.valueOf(creatureArrayList.size()));
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
            pegaJdados.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerView() {
        // Instanciando a classe RecyclerAdapter com o ArrayList populado
        recyclerAdapter = new RecyclerAdapter(creatureArrayList);

        // Setando a recyclerView para correta exibição com os itens
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setInfo(String busca) throws ExecutionException, InterruptedException {
        // Limpa o array
        creatureArrayList.clear();

        if (busca.trim().isEmpty()) {
            loadCreatureList();  // Se o texto estiver vazio, carrega a lista completa
            return;
        }

        // Construa a URL da API para a busca
        String url = "https://api.tibiadata.com/v4/creature/" + busca;

        Log.e("URL da busca:", url);

        // Execute a classe PegaJdados para obter os dados da API
        try {
            PegaJdados pegaJdados = new PegaJdados();
            pegaJdados.setOnDataReadyListener(new PegaJdados.OnDataReadyListener() {
                @Override
                public void onDataReady(ArrayList<Creature> creatures) {
                    if (creatures != null && creatures.size() > 0) {
                        Creature creature = creatures.get(0);

                        if (creature.getErrorCode() == 0) {
                            // Se não houver erro, adicione à lista
                            creatureArrayList.add(creature);
                        } else {
                            // Se houver erro, exiba a mensagem de erro
                            Toast.makeText(MainActivity3.this, creature.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
            pegaJdados.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
