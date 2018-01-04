package com.outsource.inovaufrpe.prestador.prestador.gui;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Prestador;
import com.outsource.inovaufrpe.prestador.utils.FirebaseAux;

public class CadastroActivity extends AppCompatActivity {
    private EditText etNome;
    private EditText etSobrenome;
    private EditText etEmail;
    private EditText etTelefone;
    private FirebaseAux firebase;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        firebase = FirebaseAux.getInstancia();
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
                            Prestador prestador = new Prestador();
                            prestador.setId(user.getUid());
                            prestador.setNome(etNome.getText().toString());
                            prestador.setSobrenome(etSobrenome.getText().toString());
                            prestador.setEmail(etEmail.getText().toString());
                            prestador.setTelefone(etTelefone.getText().toString().trim());
                            prestador.setCarteira(Double.valueOf("0"));
                            firebase.getPrestadorReference().child(user.getUid()).setValue(prestador);
                            startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(CadastroActivity.this, "Falha no Cadastro" + task.getException(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }
}
