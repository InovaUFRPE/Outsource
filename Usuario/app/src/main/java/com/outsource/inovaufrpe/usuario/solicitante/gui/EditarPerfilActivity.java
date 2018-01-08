package com.outsource.inovaufrpe.usuario.solicitante.gui;

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
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;

public class EditarPerfilActivity extends AppCompatActivity {
    private EditText etAtualizaNome;
    private EditText etAtualizaSobrenome;
    private EditText etAtualizaEmail;
    private EditText etAtualizaTelefone;
    private FirebaseAux firebase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        setTitle(getString(R.string.editar_perfil));
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
        user = firebase.getUser();
        user.updateEmail(etAtualizaEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    final Usuario usuario = new Usuario();
                    usuario.setId(user.getUid());
                    usuario.setNome(etAtualizaNome.getText().toString().trim());
                    usuario.setSobrenome(etAtualizaSobrenome.getText().toString().trim());
                    usuario.setEmail(etAtualizaEmail.getText().toString().trim());
                    usuario.setTelefone(etAtualizaTelefone.getText().toString().trim());
                    firebase.getUsuarioReference().child(user.getUid()).child("carteira").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Double moeda = dataSnapshot.getValue(Double.class);
                            usuario.setCarteira(moeda);
                            firebase.getUsuarioReference().child(usuario.getId()).setValue(usuario);
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
                    finish();
                    startActivity(new Intent(EditarPerfilActivity.this, LoginActivity.class));
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