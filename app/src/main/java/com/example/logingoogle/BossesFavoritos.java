package com.example.logingoogle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BossesFavoritos extends AppCompatActivity {

    Button btnSair;
    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<Bosses> BossesArrayListFavoritos = new ArrayList<>();

    BossesRecyclerAdapter BossesRecyclerAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bosses_favoritos);

        recyclerView = findViewById(R.id.recyclerView);
        btnSair = findViewById(R.id.btnSair);
        searchView = findViewById(R.id.searchViewFav);

        //searchView
        searchView.setIconified(false);
        //retira o foco automático
        searchView.clearFocus();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Toast.makeText(BossesFavoritos.this, "Você saiu!", Toast.LENGTH_LONG).show();

                startActivity(new Intent(BossesFavoritos.this, MainActivity.class));
            }
        });

        Button btnVoltar = findViewById(R.id.btnVoltarRI);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BossesFavoritos.this, Entrada.class);
                startActivity(intent);
            }
        });

        //consultar o Firebase
        setInfo();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {

                BossesRecyclerAdapter.filtrar(s);
                BossesRecyclerAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                BossesRecyclerAdapter.filtrar(s);
                BossesRecyclerAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void setInfo() {
        Query query;

        //usuário logado no momento
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //limpando o array para a consulta
        BossesArrayListFavoritos.clear();

        query = databaseReference.child(user.getUid()).child("Bosses");

        //execução da query. Se tiver dados, entra no onDataChange
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BossesArrayListFavoritos.clear();
                //este método é assíncrono, se não houver validação dos dados,
                //a lista será montada incorretamente pois não aguarda a consulta
                //assim, o if seguinte é necessário:
                if (dataSnapshot != null) {
                    for (DataSnapshot objDataSnapshot1 : dataSnapshot.getChildren()) {
                        Bosses b = objDataSnapshot1.getValue(Bosses.class);

                        // Verificar se o boss já está na lista antes de adicioná-lo
                        if (!BossesArrayListFavoritos.contains(b)) {
                            BossesArrayListFavoritos.add(b);
                        }
                    }
                    //setRecyclerView() para montagem e configuração da RecyclerView mas
                    //neste caso, setRecyclerView() tem que ser chamado aqui (dentro e ao final de onDataChange),
                    //de forma que é executado somente após os dados acima serem baixados do Firebase
                    setRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setRecyclerView() {

        BossesRecyclerAdapter = new BossesRecyclerAdapter(BossesArrayListFavoritos);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(BossesRecyclerAdapter);
    }
}