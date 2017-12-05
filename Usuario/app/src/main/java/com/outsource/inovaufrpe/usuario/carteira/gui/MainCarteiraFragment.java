package com.outsource.inovaufrpe.usuario.carteira.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainCarteiraFragment extends Fragment {
    TextView tvMoedaCarteiraID;
    Button btAdicionarMoedaID;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;


    public MainCarteiraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carteira, container, false);
        preencheDados();
        tvMoedaCarteiraID = (TextView)view.findViewById(R.id.tvMoedaCarteiraID);
        btAdicionarMoedaID = (Button) view.findViewById(R.id.btAdicionarMoedaID);
        btAdicionarMoedaID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AdicionarMoedaActivity.class));
            }
        });
        return view;
    }

    public void preencheDados(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference usuarioreference = databaseReference.child("usuario");
        final DatabaseReference firebasereference = usuarioreference.child(user.getUid());
        firebasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                DecimalFormat df = new DecimalFormat("####0.00");
                tvMoedaCarteiraID.setText(df.format(Float.parseFloat(String.valueOf(usuario.getCarteira()))).replace(".",","));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}