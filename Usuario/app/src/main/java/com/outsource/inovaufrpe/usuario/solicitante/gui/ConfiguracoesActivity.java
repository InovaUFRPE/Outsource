package com.outsource.inovaufrpe.usuario.solicitante.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;

public class ConfiguracoesActivity extends AppCompatActivity {
    private EditText etAtualizaNome;
    private EditText etAtualizaSobrenome;
    private EditText etAtualizaEmail;
    private EditText etAtualizaTelefone;
    private Button btAtualizarID;
    private Button btDeletarID;
    private Button btSair;
    private FirebaseAux firebase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        setTitle("Configurações");
        firebase = FirebaseAux.getInstancia();

        DatabaseReference firebasereference = firebase.getUsuarioReference().child(firebase.getUser().getUid());
        firebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                etAtualizaNome.setText(usuario.getNome());
                etAtualizaSobrenome.setText(usuario.getSobrenome());
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
        user = firebase.getUser();
        user.updateEmail(etAtualizaEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    final Usuario usuario = new Usuario();
                    usuario.setId(user.getUid());
                    usuario.setNome(etAtualizaNome.getText().toString().trim());
                    usuario.setSobrenome(etAtualizaSobrenome.getText().toString().trim());
                    usuario.setEmail(etAtualizaEmail.getText().toString().trim());
                    usuario.setTelefone(etAtualizaTelefone.getText().toString().trim());
                    firebase.getUsuarioReference().child(user.getUid()).child("carteira").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Long moeda = dataSnapshot.getValue(Long.class);
                            usuario.setCarteira(moeda);
                            firebase.getUsuarioReference().child(usuario.getId()).setValue(usuario);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }});
    }

    public void Deletar(){
        firebase.getUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        firebase.getFirebaseAuth().signOut();
        LoginManager.getInstance().logOut();
        finish();
        startActivity(new Intent(ConfiguracoesActivity.this, LoginActivity.class));
    }

}
