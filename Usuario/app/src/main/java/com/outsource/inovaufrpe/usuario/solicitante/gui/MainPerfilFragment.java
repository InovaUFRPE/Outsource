package com.outsource.inovaufrpe.usuario.solicitante.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;

public class MainPerfilFragment extends Fragment {
    ImageButton btnConfig;
    TextView nomeUsuario;
    TextView emailUsuario;
    TextView telefoneUsuario;


    public MainPerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.tvNomePerfil);
        emailUsuario = view.findViewById(R.id.tvEmailPerfil);
        telefoneUsuario = view.findViewById(R.id.tvTelefonePerfil);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usuarioReference = databaseReference.child("usuario").child(usuarioAtual.getUid());
                usuarioReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario user = dataSnapshot.getValue(Usuario.class);
                        nomeUsuario.setText(user.getNome()+ " " +user.getSobrenome());
                        emailUsuario.setText(user.getEmail());
                        telefoneUsuario.setText(user.getTelefone());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        nomeUsuario.setText(usuarioAtual.getDisplayName());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        btnConfig = getActivity().findViewById(R.id.imageButton);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConfiguracoesActivity.class));
            }
        });
    }
}
