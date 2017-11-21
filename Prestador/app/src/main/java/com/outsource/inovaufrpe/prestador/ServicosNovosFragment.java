package com.outsource.inovaufrpe.prestador;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outsource.inovaufrpe.prestador.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.ServicoListHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosNovosFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public ServicosNovosFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException{
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_servicos_novos, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycleID);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adaptador();

        return layout;
    }

    private void adaptador(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        adapter = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.linha_recycleview, ServicoListHolder.class, databaseReference.child("servico")) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.descricao.setText(model.getDescricao());
                DecimalFormat df = new DecimalFormat("####0.00");
                viewHolder.valor.setText("R$ "+ df.format(Float.parseFloat(model.getPreco())));

                viewHolder.aceitar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Voce aceitou", Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.negociar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Voce esta negociando", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            };

        mRecyclerView.setAdapter(adapter);
    }

}
