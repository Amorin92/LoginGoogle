package com.example.logingoogle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

//Adapter do Recycler View
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Creature> creatureArrayListLocal;
    ArrayList<Creature> creatureArrayListCopia;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public RecyclerAdapter(ArrayList<Creature> creatureArrayListLocal_) {
        this.creatureArrayListLocal = creatureArrayListLocal_;
        creatureArrayListCopia = new ArrayList<>(creatureArrayListLocal);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ViewHolder com XML do formato dos itens da lista...
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = creatureArrayListLocal.get(position).getName();
        String race = creatureArrayListLocal.get(position).getRace();
        String imageUrl = creatureArrayListLocal.get(position).getImageUrl();

        holder.mTextViewCre.setText(name);
        holder.mTextViewRace.setText(race);

        Glide.with(holder.mImageView.getContext())
                .asGif()
                .load(imageUrl)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return creatureArrayListLocal.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mTextViewCre;
        TextView mTextViewRace;
        ImageView mImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewCre = itemView.findViewById(R.id.textViewCreature);
            mTextViewRace = itemView.findViewById(R.id.textViewRace);

            mImageView = itemView.findViewById(R.id.imageView);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


            // MainActivity3 (lista a API)
            if (view.getContext().toString().contains("MainActivity3")) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Salvar Criatura")
                        .setMessage("Confirma salvar nos favoritos?")
                        .setIcon(R.drawable.ic_baseline_favorite_border_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, salvar no Firebase, método "inserirEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Criatura salva nos favoritos.", Toast.LENGTH_SHORT).show();
                                inserirEm(getLayoutPosition());

                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
            //UsuarioFavoritos, popup
            else {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Remover")
                        .setMessage("Deseja remover dos favoritos?")
                        .setIcon(R.drawable.ic_baseline_delete_outline_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, remover do Firebase, método "removerEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Criatura removida dos favoritos.", Toast.LENGTH_SHORT).show();
                                removerEm(getLayoutPosition());
                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
        }

        private void inserirEm(int layoutPosition) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            Creature c = creatureArrayListLocal.get(layoutPosition);

            databaseReference.child(user.getUid()).
                    child("Creatures").
                    child(c.getName()).
                    setValue(c);
        }

    }

    //remover no Firebase
    public void removerEm(int layoutPosition) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Creature c = creatureArrayListLocal.get(layoutPosition);

        //importante: sem esta linha o array não é atualizado corretamente
        //limpa o array para correta renderização da lista
        //após remoção, array será remontado com os valores restantes do firebase
        creatureArrayListLocal.clear();

        databaseReference.child(user.getUid()).child("Creatures").
                child(c.getName()).
                removeValue();
    }

    public void setCreatureList(ArrayList<Creature> creatureList) {
        this.creatureArrayListLocal = creatureList;
    }

    public void filtrar(String text) {
        // Limpa o array que monta a lista ao buscar algum termo na searchView
        creatureArrayListLocal.clear();

        if (text.isEmpty()) {
            creatureArrayListLocal.addAll(creatureArrayListCopia);
        } else {

            text = text.toLowerCase();

            // Percorre o array com os dados originais (todos os favoritos)
            for (Creature item : creatureArrayListCopia) {
                // Caso, nos dados originais, exista o termo procurado, popule o array vazio com o item
                if (item.getName().toLowerCase().contains(text) || item.getRace().toLowerCase().contains(text)) {
                    creatureArrayListLocal.add(item);
                }
            }
        }

        // Notifica o adapter sobre as mudanças nos dados
        notifyDataSetChanged();
    }
}
