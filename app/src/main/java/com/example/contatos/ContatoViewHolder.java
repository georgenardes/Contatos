package com.example.contatos;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ContatoViewHolder extends RecyclerView.ViewHolder {
    private TextView nome;
    private TextView empresa;
    private TextView telefone;

    private ImageButton ibDelete;
    private ImageButton ibEdit;


    public ContatoViewHolder(@NonNull View itemView) {
        super(itemView);
        nome = itemView.findViewById(R.id.tvNome);
        empresa = itemView.findViewById(R.id.tvEmpresa);
        telefone = itemView.findViewById(R.id.tvTelefone);
        ibDelete = itemView.findViewById(R.id.ibDelete);
        ibEdit = itemView.findViewById(R.id.ibEdit);
    }

    void atualizaViewHolder(Contato contato){
        nome.setText(contato.getNome());
        empresa.setText(contato.getEmpresa());
        telefone.setText(contato.getTelefone());
    }



}
