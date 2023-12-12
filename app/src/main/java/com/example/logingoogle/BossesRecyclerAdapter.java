package com.example.logingoogle;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class BossesRecyclerAdapter extends RecyclerView.Adapter<BossesRecyclerAdapter.BossViewHolder> {

    private ArrayList<Bosses> bossesList;
    private ArrayList<Bosses> bossesListCopia;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public BossesRecyclerAdapter(ArrayList<Bosses> bossesList_) {
        this.bossesList = bossesList_;
        bossesListCopia = new ArrayList<>(bossesList);
    }

    @NonNull
    @Override
    public BossViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_b, parent, false);
        return new BossViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BossViewHolder holder, int position) {
        String name = bossesList.get(position).getName();
        String imageUrl = bossesList.get(position).getImageUrl();

        holder.mTextViewCre.setText(name);

        Glide.with(holder.mImageView.getContext())
                .asGif()
                .load(imageUrl)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return bossesList.size();
    }

    public class BossViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextViewCre;
        ImageView mImageView;

        public BossViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewCre = itemView.findViewById(R.id.textViewCreature);
            mImageView = itemView.findViewById(R.id.imageView);

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // MainActivity3 (lista a API)
            if (view.getContext().toString().contains("ListBosses")) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Salvar Boss")
                        .setMessage("Confirma salvar nos favoritos?")
                        .setIcon(R.drawable.ic_baseline_favorite_border_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, salvar no Firebase, método "inserirEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Boss salvo nos favoritos.", Toast.LENGTH_SHORT).show();
                                inserirEm(getLayoutPosition());

                            }
                        })
                        .setNegativeButton("Não", null).show();
            } else if (view.getContext().toString().contains("BossDoDia")) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Salvar Boss")
                        .setMessage("Confirma salvar nos favoritos?")
                        .setIcon(R.drawable.ic_baseline_favorite_border_24)
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            //click no botão de ok, salvar no Firebase, método "inserirEm()"
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(view.getContext(), "Boss salvo nos favoritos.", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(view.getContext(), "Boss removido dos favoritos.", Toast.LENGTH_SHORT).show();
                                removerEm(getLayoutPosition());
                            }
                        })
                        .setNegativeButton("Não", null).show();
            }
        }


        private void inserirEm(int layoutPosition) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (layoutPosition >= 0 && layoutPosition < bossesList.size()) {
                Bosses b = bossesList.get(layoutPosition);

                databaseReference.child(user.getUid())
                        .child("Bosses")
                        .child(b.getName())
                        .setValue(b);
            }
        }

        // Método para remover
        private void removerEm(int layoutPosition) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (layoutPosition >= 0 && layoutPosition < bossesList.size()) {
                Bosses b = bossesList.get(layoutPosition);

                // Remover do Firebase
                databaseReference.child(user.getUid())
                        .child("Bosses")
                        .child(b.getName())
                        .removeValue();

                // Remover da lista local
                bossesList.remove(layoutPosition);

                // Notificar o RecyclerView sobre as mudanças
                notifyItemRemoved(layoutPosition);
                notifyItemRangeChanged(layoutPosition, bossesList.size());
            }
        }
    }

    public void setBossesList(ArrayList<Bosses> bossesList) {
        this.bossesList = bossesList;
        notifyDataSetChanged();
    }

    public void filtrar(String text) {
        bossesList.clear();

        if (text.isEmpty()) {
            bossesList.addAll(bossesListCopia);
        } else {
            text = text.toLowerCase();

            for (Bosses item : bossesListCopia) {
                if (item.getName().toLowerCase().contains(text)) {
                    bossesList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }
}
