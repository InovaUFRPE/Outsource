package com.outsource.inovaufrpe.usuario.solicitante.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;

public class MainPerfilFragment extends Fragment {
    ImageButton btnConfig;
    TextView nomeUsuario;
    TextView emailUsuario;
    TextView telefoneUsuario;
    RatingBar avaliarPerfil;

    public MainPerfilFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.tvNomePerfil);
        emailUsuario = view.findViewById(R.id.tvEmailPerfil);
        telefoneUsuario = view.findViewById(R.id.tvTelefonePerfil);
        avaliarPerfil = view.findViewById(R.id.rbAvaliarPerfil);
        FirebaseAux firebase = FirebaseAux.getInstancia();
        DatabaseReference usuarioReference = firebase.getUsuarioReference().child(firebase.getUser().getUid());
        usuarioReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Usuario user = dataSnapshot.getValue(Usuario.class);
                        String nomeCompleto = user.getNome() + " " + user.getSobrenome();
                        nomeUsuario.setText(nomeCompleto);
                        emailUsuario.setText(user.getEmail());
                        telefoneUsuario.setText(user.getTelefone());
                        if (user.getPesoNota() == 0){
                            avaliarPerfil.setRating(0);
                        }
                        else {
                            avaliarPerfil.setRating((float)user.getNota()/user.getPesoNota());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        nomeUsuario.setText(firebase.getUser().getDisplayName());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        btnConfig = getActivity().findViewById(R.id.imageButton);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditarPerfilActivity.class));
            }
        });
    }
}
