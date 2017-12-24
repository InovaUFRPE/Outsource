package com.outsource.inovaufrpe.prestador.carteira.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.carteira.dominio.God;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Prestador;
import com.outsource.inovaufrpe.prestador.prestador.gui.MainActivity;

public class AdicionarMoedaActivity extends AppCompatActivity {
    EditText etAdicionarMoedaID;
    Button btConfirmarID;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_moeda);
        etAdicionarMoedaID = findViewById(R.id.etAdicionarMoedaID);
        btConfirmarID = findViewById(R.id.btConfirmarID);

        btConfirmarID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmar();
            }
        });
    }

    public void confirmar() {
        adicionaMoeda();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void adicionaMoeda() {
        databaseReference.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Prestador usuario = dataSnapshot.getValue(Prestador.class);
                God moeda = new God(usuario.getCarteira());
                moeda.adicionar(Double.valueOf(etAdicionarMoedaID.getText().toString()));
                usuario.setCarteira(moeda.getMoeda());
                databaseReference.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).setValue(usuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
