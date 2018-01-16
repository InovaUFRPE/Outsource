package com.outsource.inovaufrpe.usuario.solicitante.gui;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;

public class CadastroActivity extends Activity {
    private EditText etNome;
    private EditText etSobrenome;
    private EditText etEmail;
    private EditText etTelefone;
    private FirebaseAux firebase;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseAux.getInstancia();
        setContentView(R.layout.activity_cadastro);
        etNome = findViewById(R.id.etNomeID);
        etSobrenome = findViewById(R.id.etSobrenomeID);
        etEmail = findViewById(R.id.etEmailID);
        etTelefone = findViewById(R.id.etTelefoneID);
        Button btCadastrar = findViewById(R.id.btCadastrarID);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });


    }

    private void cadastrar() {
        user = firebase.getUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(etNome.getText().toString()).build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Usuario usuario = new Usuario();
                            usuario.setId(user.getUid());
                            usuario.setNome(etNome.getText().toString());
                            usuario.setSobrenome(etSobrenome.getText().toString());
                            usuario.setEmail(etEmail.getText().toString());
                            usuario.setTelefone(etTelefone.getText().toString().trim());
                            usuario.setCarteira(Double.valueOf("0"));
                            usuario.setNota(0);
                            usuario.setPesoNota(0);
                            firebase.getUsuarioReference().child(user.getUid()).setValue(usuario);
                            startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                            finish();


                        } else {
                            Toast.makeText(CadastroActivity.this, "Falha no Cadastro" + task.getException(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
}
