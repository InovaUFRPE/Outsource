package com.outsource.inovaufrpe.usuario.servico.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosPessoaisFragment extends Fragment {
    TextView tNomeServicoID;
    TextView tDataServicoID;
    TextView tDonoServicoID;
    TextView tNotaDonoServicoID;
    TextView tPrecoServicoID;
    ListView lDados;
    private FloatingActionButton bCriarServico;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase;

    private List<Servico> listServico = new ArrayList<Servico>();
    private ArrayAdapter<Servico> arrayAdapterServico;


    public ServicosPessoaisFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException{
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_pessoais, container, false);

        bCriarServico = view.findViewById(R.id.inserirBtn);

        bCriarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext() ,CadastroServicoActivity.class));

//                startActivity(new Intent(view.getContext() ,VisualizarServicoActivity.class));
            }
        });

        /*inicializarFirebase();
        eventoDataBase();
        preencheDados();*/

        return view;
    }

    /*private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();

    }

    public void eventoDataBase(){
        try {
            databaseReference.child("servico").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listServico.clear();
                    for (DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                        Servico servico = objSnapshot.getValue(Servico.class);
                        tNomeServicoID.setText(servico.getNome());
                        tDataServicoID.setText(servico.getData());
                        tPrecoServicoID.setText(servico.getPreco());
                        listServico.add(servico);
                    }
                    arrayAdapterServico = new ArrayAdapter<Servico>(getActivity(),R.layout.fragment_servicos_pessoais,listServico);
                    lDados.setAdapter(arrayAdapterServico);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (NullPointerException e){
            Log.e("ServicosPessoaisFragment","Tabela serviços não possui elementos");
        }
    }

    public void preencheDados(){
        databaseReference.child("usuario").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                tDonoServicoID.setText(usuario.getNome());
                tNotaDonoServicoID.setText(usuario.getNota());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

}
