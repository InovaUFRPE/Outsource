package com.outsource.inovaufrpe.usuario.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.dominio.Usuario;

public class ConfiguracoesActivity extends AppCompatActivity {
    private EditText etAtualizaNome;
    private EditText etAtualizaSobrenome;
    private EditText etAtualizaEmail;
    private EditText etAtualizaTelefone;
    private Button btAtualizarID;
    private Button btDeletarID;
    private Button btSair;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference usuarioReference = databaseReference.child("usuario");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        setTitle("Configurações");
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference firebasereference = usuarioReference.child(user.getUid());
        firebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                etAtualizaNome.setText(usuario.getNome());
                etAtualizaSobrenome.setText(usuario.getUsername());
                etAtualizaEmail.setText(usuario.getEmail());
                etAtualizaTelefone.setText(usuario.getTelefone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        etAtualizaNome = (EditText) findViewById(R.id.etAtualizaNomeID);
        etAtualizaSobrenome = (EditText) findViewById(R.id.etAtualizaSobrenomeID);
        etAtualizaEmail = (EditText) findViewById(R.id.etAtualizaEmailID);
        etAtualizaTelefone = (EditText) findViewById(R.id.etAtualizaTelefoneID);
        btAtualizarID = (Button) findViewById(R.id.btAtualizarID);
        btDeletarID = (Button) findViewById(R.id.btDeletarID);
        btSair = (Button) findViewById(R.id.btSair);


        btAtualizarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Atualizar();
            }
        });

        btDeletarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deletar();
            }
        });

        btSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });
    }

    public void Atualizar(){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.updateEmail(etAtualizaEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Usuario usuario = new Usuario();
                    usuario.setId(firebaseAuth.getCurrentUser().getUid());
                    usuario.setNome(etAtualizaNome.getText().toString().trim());
                    usuario.setUsername(etAtualizaSobrenome.getText().toString().trim());
                    usuario.setEmail(etAtualizaEmail.getText().toString().trim());
                    usuario.setTelefone(etAtualizaTelefone.getText().toString().trim());
                    databaseReference.child("usuario").child(usuario.getId()).setValue(usuario);
                }
            }});
    }

    public void Deletar(){
        firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ConfiguracoesActivity.this,"Conta desativada com sucesso.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Logout(){
        Toast.makeText(ConfiguracoesActivity.this, "Saiu com sucesso.", Toast.LENGTH_SHORT).show();
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ConfiguracoesActivity.this, LoginActivity.class));
    }

}
