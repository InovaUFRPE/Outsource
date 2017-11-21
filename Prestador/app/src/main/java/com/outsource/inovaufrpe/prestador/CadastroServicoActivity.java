package com.outsource.inovaufrpe.prestador;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.dominio.Servico;
import com.outsource.inovaufrpe.prestador.dominio.Prestador;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CadastroServicoActivity extends AppCompatActivity {
    EditText etNomeServicoID;
    EditText etDescricaoServicoID;
    EditText etPrecoServicoID;
    Button btVoltarID;
    Button btConfirmarID;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_servico);

        etNomeServicoID = (EditText)findViewById(R.id.etNomeServicoID);
        etDescricaoServicoID = (EditText)findViewById(R.id.etDescricaoServicoID);
        etPrecoServicoID = (EditText)findViewById(R.id.etPrecoServicoID);
        btVoltarID = (Button)findViewById(R.id.btVoltarID);
        btConfirmarID = (Button)findViewById(R.id.btConfirmarID);

        btVoltarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voltar();
            }
        });

        btConfirmarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar();
            }
        });

        if (firebaseDatabase == null) {
            inicializarFirebase();
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(CadastroServicoActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void voltar(){
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    public void confirmar(){
        Servico servico = criaServico();
        adicionaServicoUsuario(servico);
        finish();
        startActivity(new Intent(this,MainActivity.class));
    }

    private void adicionaServicoUsuario(Servico servico){
        final Servico newServico = servico;
        databaseReference.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Prestador prestador = dataSnapshot.getValue(Prestador.class);
                prestador.getUsername();
                databaseReference.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).setValue(prestador);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private Servico criaServico(){
        Date date = new Date();
        Servico servico = new Servico();
        servico.setId(UUID.randomUUID().toString());
        servico.setNome(etNomeServicoID.getText().toString().trim());
        servico.setDescricao(etDescricaoServicoID.getText().toString().trim());
        servico.setPreco(etPrecoServicoID.getText().toString().trim());
        servico.setData(date);
        databaseReference.child("servico").child(servico.getId()).setValue(servico);
        return servico;
    }
}
