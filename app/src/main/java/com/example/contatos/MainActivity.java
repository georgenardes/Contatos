package com.example.contatos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private RecyclerView contatosRecyclerView;
    private ContatoRecycleAdapter contatosRecycleAdapter;

    private FloatingActionButton fabAddContato;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference contatosRef = db.collection("contatos");

    private int id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contatosRecyclerView = findViewById(R.id.recyclerView);
        fabAddContato = findViewById(R.id.fabAddContact);


        fabAddContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incluirContato();
            }
        });

        contatosRecycleAdapter = new ContatoRecycleAdapter();
        contatosRecycleAdapter.contatoArrayList = new ArrayList<>();
        contatosRecyclerView.setAdapter(contatosRecycleAdapter);
        contatosRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        carregarContatos();


    }


    void incluirContato(){

        Intent novoContato = new Intent(this, NovoContato.class);

        novoContato.putExtra("_id", id+1);
        startActivityForResult(novoContato, 200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                carregarContatos();
            }
        }


    }

    /**
     *  Deleta contato
     * @param contato
     */
    void deletarContato(Contato contato){
        db.collection("cities").document("contato").delete();
        DocumentReference contatoRef = db.collection("contatos").document(""+contato.get_id());

        contatoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                carregarContatos();
            }
        });
    }

    /**
     * Chama activity de inclusão passando editar = true e o id de contato
     * @param contato
     */
    void editarContato(Contato contato){
        Intent editarContato = new Intent(this, NovoContato.class);
        editarContato.putExtra("_id", contato.get_id());
        editarContato.putExtra("editar", true);
        startActivityForResult(editarContato, 200);
    }


    void carregarContatos() {
        contatosRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    contatosRecycleAdapter.contatoArrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Contato contato = documentSnapshot.toObject(Contato.class);
                        if (contato.get_id() > id) { // Verifica maior ID da coleção
                            id = contato.get_id();
                        }
                        contatosRecycleAdapter.contatoArrayList.add(0, contato);
                    }
                    contatosRecyclerView.setAdapter(contatosRecycleAdapter);
                    contatosRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                } else {
                    Log.d("Erooooooooooo", "Erro ao ler o doc");
                }
            }
        });
    }
}
