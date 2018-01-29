package com.outsource.inovaufrpe.prestador.prestador.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPerfilFragment extends Fragment {
    ImageButton btnConfig;
    TextView nomeUsuario;
    TextView emailUsuario;
    TextView telefoneUsuario;
    RatingBar avaliarPerfil;
    TextView numServicosAtendidos;
    TextView numAvaliacoes;


    public MainPerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        nomeUsuario = view.findViewById(R.id.tvNomePerfil);
        emailUsuario = view.findViewById(R.id.tvEmailPerfil);
        telefoneUsuario = view.findViewById(R.id.tvTelefonePerfil);
        avaliarPerfil = view.findViewById(R.id.rbAvaliarPerfil);
        numServicosAtendidos = view.findViewById(R.id.num_servicos_atendidos);
        numAvaliacoes = view.findViewById(R.id.num_avaliacoes);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference usuarioReference = databaseReference.child("prestador").child(usuarioAtual.getUid());
        usuarioReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Prestador prestador = dataSnapshot.getValue(Prestador.class);
                        String nomeCompleto = prestador.getNome()+" "+prestador.getSobrenome();
                        nomeUsuario.setText(nomeCompleto);
                        emailUsuario.setText(prestador.getEmail());
                        telefoneUsuario.setText(prestador.getTelefone());
                        numAvaliacoes.setText(String.valueOf(prestador.getPesoNota()));
                        numServicosAtendidos.setText(String.valueOf(prestador.getListaServicos().size()));
                        if(prestador.getPesoNota() == 0){
                            avaliarPerfil.setRating(0);
                        }else{
                            avaliarPerfil.setRating((float)prestador.getNota()/prestador.getPesoNota());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


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
