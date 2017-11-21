package com.outsource.inovaufrpe.prestador.activities;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.dominio.Prestador;

public class CadastroActivity extends AppCompatActivity {
    private EditText etNome;
    private EditText etSobrenome;
    private EditText etEmail;
    private EditText etTelefone;
    private Button btCadastrar;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference usuarioReference = databaseReference.child("prestador");

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
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(etNome.getText().toString()).build();
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Prestador prestador = new Prestador();
                            prestador.setId(firebaseAuth.getCurrentUser().getUid());
                            prestador.setNome(etNome.getText().toString());
                            prestador.setUsername(etSobrenome.getText().toString());
                            prestador.setEmail(etEmail.getText().toString());
                            prestador.setTelefone(etTelefone.getText().toString().trim());

                            usuarioReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(prestador);
                            startActivity(new Intent(CadastroActivity.this, ConfiguracoesActivity.class));
                            finish();
                        } else {
                            Toast.makeText(CadastroActivity.this, "Falha no Cadastro" + task.getException(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }
}
