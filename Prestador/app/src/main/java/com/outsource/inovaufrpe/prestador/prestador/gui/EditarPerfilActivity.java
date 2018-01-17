package com.outsource.inovaufrpe.prestador.prestador.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Prestador;
import com.outsource.inovaufrpe.prestador.utils.FirebaseAux;

public class EditarPerfilActivity extends AppCompatActivity {
    private EditText etAtualizaNome;
    private EditText etAtualizaSobrenome;
    private EditText etAtualizaEmail;
    private EditText etAtualizaTelefone;

    private FirebaseAux firebase;

    /*
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference usuarioReference = databaseReference.child("prestador");
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        setTitle(getString(R.string.configuracoes));
        firebase = FirebaseAux.getInstancia();


        DatabaseReference firebasereference = firebase.getPrestadorReference().child(firebase.getUser().getUid());
        firebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Prestador usuario = dataSnapshot.getValue(Prestador.class);
                etAtualizaNome.setText(usuario.getNome());
                etAtualizaSobrenome.setText(usuario.getSobrenome());
                etAtualizaEmail.setText(usuario.getEmail());
                etAtualizaTelefone.setText(usuario.getTelefone());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        etAtualizaNome = findViewById(R.id.etAtualizaNomeID);
        etAtualizaSobrenome = findViewById(R.id.etAtualizaSobrenomeID);
        etAtualizaEmail = findViewById(R.id.etAtualizaEmailID);
        etAtualizaTelefone = findViewById(R.id.etAtualizaTelefoneID);
        Button btDeletarID = findViewById(R.id.btDeletarID);
        Button btSair = findViewById(R.id.btSair);

        btDeletarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public void Atualizar() {
        final FirebaseUser user = firebase.getUser();
        user.updateEmail(etAtualizaEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    final Prestador usuario = new Prestador();
                    usuario.setId(user.getUid());
                    usuario.setNome(etAtualizaNome.getText().toString().trim());
                    usuario.setSobrenome(etAtualizaSobrenome.getText().toString().trim());
                    usuario.setEmail(etAtualizaEmail.getText().toString().trim());
                    usuario.setTelefone(etAtualizaTelefone.getText().toString().trim());
                    firebase.getPrestadorReference().child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Double moeda = dataSnapshot.child("carteira").getValue(Double.class);
                            usuario.setCarteira(moeda);
                            usuario.setPesoNota(dataSnapshot.child("pesoNota").getValue(int.class));
                            usuario.setNota(dataSnapshot.child("nota").getValue(float.class));
                            firebase.getPrestadorReference().child(usuario.getId()).setValue(usuario);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    finish();
                    startActivity(new Intent(EditarPerfilActivity.this, MainActivity.class));
                }
            }
        });
    }

    public void Deletar() {
        firebase.getUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditarPerfilActivity.this, "Conta desativada com sucesso.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Logout() {
        Toast.makeText(EditarPerfilActivity.this, "Saiu com sucesso.", Toast.LENGTH_SHORT).show();
        firebase.getFirebaseAuth().signOut();
        LoginManager.getInstance().logOut();
        finish();
        startActivity(new Intent(EditarPerfilActivity.this, LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.salvar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.salvarBtn) {
            Atualizar();
        }
        return super.onOptionsItemSelected(item);
    }

}

