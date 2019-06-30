package com.example.contatos;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ContatoRecycleAdapter extends RecyclerView.Adapter {

    public ArrayList<Contato> contatoArrayList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View elementoLista = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.itemlist, viewGroup,false
        );
        return new ContatoViewHolder(elementoLista);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ContatoViewHolder contatoViewHolder = (ContatoViewHolder) viewHolder;
        final Contato contato = contatoArrayList.get(i);
        contatoViewHolder.atualizaViewHolder(contato);

        contatoViewHolder.itemView.findViewById(R.id.ibDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)v.getContext()).deletarContato(contato);

            }
        });

        contatoViewHolder.itemView.findViewById(R.id.ibEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)v.getContext()).editarContato(contato);

            }
        });



    }

    @Override
    public int getItemCount() {
        return contatoArrayList.size();
    }
}
