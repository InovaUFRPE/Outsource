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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Prestador;

public class EditarPerfilActivity extends AppCompatActivity {
    private EditText etAtualizaNome;
    private EditText etAtualizaSobrenome;
    private EditText etAtualizaEmail;
    private EditText etAtualizaTelefone;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference usuarioReference = databaseReference.child("prestador");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        setTitle("Configurações");
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference firebasereference = usuarioReference.child(user.getUid());
        firebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Prestador usuario = dataSnapshot.getValue(Prestador.class);
                etAtualizaNome.setText(usuario.getNome());
                etAtualizaSobrenome.setText(usuario.getUsername());
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

    public void Atualizar(){
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.updateEmail(etAtualizaEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Prestador usuario = new Prestador();
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
                    Toast.makeText(EditarPerfilActivity.this,"Conta desativada com sucesso.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Logout(){
        Toast.makeText(EditarPerfilActivity.this, "Saiu com sucesso.", Toast.LENGTH_SHORT).show();
        firebaseAuth.signOut();
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

