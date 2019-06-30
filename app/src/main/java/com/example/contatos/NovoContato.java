package com.example.contatos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

import java.util.HashMap;

public class NovoContato extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Contato contato;

    private EditText nome;
    private EditText empresa;
    private EditText telefone;

    private Button btnGravar;
    private Button btnVoltar;

    private boolean editar;
    private DocumentReference contatoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_contato);

        contato = new Contato();

        editar = getIntent().getBooleanExtra("editar", false);

        nome = findViewById(R.id.etNome);
        empresa = findViewById(R.id.etEmpresa);
        telefone = findViewById(R.id.etTelefone);

        btnGravar = findViewById(R.id.btnGravar);
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGravar(v);
            }
        });

        btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickVoltar(v);
            }
        });

        //Se for edição, carrega os dados conforme o id
        if (editar){
            carregar(getIntent().getIntExtra("_id", 0));
        } else {
            contato.set_id(getIntent().getIntExtra("_id", 0));
        }

    }

    /**
     * Busca o contato na coleção
     * @param id
     */
    void carregar(int id){
        contatoRef = db.collection("contatos").document(""+id);

        contatoRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Contato contato = documentSnapshot.toObject(Contato.class);
                inflarElementos(contato);
            }
        });


    }

    /**
     * Insere os valores recebidos nos elementos
     * @param contato
     */
    void inflarElementos(Contato contato){
        nome.setText(contato.getNome());
        empresa.setText(contato.getEmpresa());
        telefone.setText(contato.getTelefone());
    }

    void onClickGravar(final View view){

        contato.setNome(nome.getText().toString());
        contato.setEmpresa(empresa.getText().toString());
        contato.setTelefone(telefone.getText().toString());

        if(editar){
            HashMap<String, Object> edtContato = new HashMap<>();

            edtContato.put("nome", contato.getNome());
            edtContato.put("empresa", contato.getEmpresa());
            edtContato.put("telefone", contato.getTelefone());

            contatoRef.update(edtContato);

            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();

        } else {
            db.collection("contatos")
                    .document(""+contato.get_id())
                    .set(contato).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Contato Inserido", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Erro ao inserir contato", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    void onClickVoltar(View view){
        finish();
    }




}
