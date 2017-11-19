package com.outsource.inovaufrpe.usuario.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.dominio.Usuario;

public class CadastroActivity extends Activity {
    private EditText etNome;
    private EditText etSobrenome;
    private EditText etCpf;
    private EditText etEmail;
    private EditText etTelefone;
    private EditText etSenha;
    private Button btCadastrar;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference usuarioReference = databaseReference.child("usuario");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        etNome = (EditText) findViewById(R.id.etNomeID);
        etSobrenome = (EditText) findViewById(R.id.etSobrenomeID);
        etEmail = (EditText) findViewById(R.id.etEmailID);
        etTelefone = (EditText) findViewById(R.id.etTelefoneID);
        etSenha = (EditText) findViewById(R.id.etSenhaID);
        btCadastrar = (Button) findViewById(R.id.btCadastrarID);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });


    }

    private void cadastrar() {
        String email = etEmail.getText().toString();
        final String nome = etNome.getText().toString();
        firebaseAuth.createUserWithEmailAndPassword(email, etSenha.getText().toString()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    final FirebaseUser user = firebaseAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nome).build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Usuario usuario = new Usuario();
                                        usuario.setId(user.getUid());
                                        usuario.setNome(nome);
                                        usuario.setUsername(etSobrenome.getText().toString());
                                        usuario.setEmail(etEmail.getText().toString());
                                        usuario.setTelefone(etTelefone.getText().toString().trim());
                                        usuarioReference.child(user.getUid()).setValue(usuario);
                                        startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(CadastroActivity.this, "Falha no Cadastro" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CadastroActivity.this, "Falha no Cadastro" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
