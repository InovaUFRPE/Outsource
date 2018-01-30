package com.outsource.inovaufrpe.prestador.carteira.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Prestador;

import java.text.DecimalFormat;

public class SaldoFragment extends android.support.v4.app.Fragment {
    TextView tvMoedaCarteiraID;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//    FirebaseDatabase firebaseDatabase;


    public SaldoFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_carteira, container, false);
        preencheDados();
        tvMoedaCarteiraID = view.findViewById(R.id.tvMoedaCarteiraID);
        return view;
    }

    public void preencheDados() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference usuarioreference = databaseReference.child("prestador");
        final DatabaseReference firebasereference = usuarioreference.child(user.getUid());
        firebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Prestador prestador = dataSnapshot.getValue(Prestador.class);
                DecimalFormat df = new DecimalFormat("####0.00");
                tvMoedaCarteiraID.setText(df.format(Float.parseFloat(String.valueOf(prestador.getCarteira()))).replace(".", ","));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
