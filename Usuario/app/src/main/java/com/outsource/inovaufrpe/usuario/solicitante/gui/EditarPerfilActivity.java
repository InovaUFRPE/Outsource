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

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;
import com.outsource.inovaufrpe.usuario.utils.Utils;

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
        final String nome = etAtualizaNome.getText().toString().trim();
        final String sobrenome = etAtualizaSobrenome.getText().toString().trim();
        final String email = etAtualizaEmail.getText().toString().trim();
        final String telefone = etAtualizaTelefone.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                final Usuario usuario = new Usuario();
                usuario.setId(user.getUid());
                usuario.setNome(nome);
                usuario.setSobrenome(sobrenome);
                usuario.setEmail(email);
                usuario.setTelefone(telefone);
                firebase.getUsuarioReference().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Double moeda = dataSnapshot.child("carteira").getValue(Double.class);
                        usuario.setCarteira(moeda);
                        usuario.setNota(dataSnapshot.child("nota").getValue(float.class));
                        usuario.setPesoNota(dataSnapshot.child("pesoNota").getValue(int.class));
                        firebase.getUsuarioReference().child(usuario.getId()).setValue(usuario);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                finish();
                startActivity(new Intent(EditarPerfilActivity.this, MainActivity.class));
            }
        });
    }


    public void Deletar() {
        firebase.getUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utils.criarToast(EditarPerfilActivity.this, "Conta desativada com sucesso.");
                    finish();
                    startActivity(new Intent(EditarPerfilActivity.this, LoginActivity.class));
                }
            }
        });
    }

    public void Logout() {
        Utils.criarToast(EditarPerfilActivity.this, "Saiu com sucesso.");
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
