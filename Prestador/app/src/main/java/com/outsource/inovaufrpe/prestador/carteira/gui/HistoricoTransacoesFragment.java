package com.outsource.inovaufrpe.prestador.carteira.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.HistoricoServicoListHolder;

public class HistoricoTransacoesFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private FirebaseRecyclerAdapter adapter;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public HistoricoTransacoesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historico_transacoes, container, false);

        mRecyclerView = view.findViewById(R.id.RecycleID);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adaptador();
        return view;
    }

    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference("visualizacao").child("concluido");
        Query query = databaseReference.orderByChild("idPrestador").equalTo(firebaseAuth.getCurrentUser().getUid());
        adapter = new FirebaseRecyclerAdapter<Servico, HistoricoServicoListHolder>(Servico.class, R.layout.row_historico_negociacoes, HistoricoServicoListHolder.class, query) {

            @Override
            protected void populateViewHolder(HistoricoServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.nomeServico.setText(model.getNome());
                viewHolder.data.setText(CardFormat.dataFormat(model.getDataf(),"dd/MM"));
                String s = "+ " + CardFormat.dinheiroFormat(model.getPreco().toString());
                viewHolder.preco.setText(s);

            }

            @Override
            public HistoricoServicoListHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final HistoricoServicoListHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                return viewHolder;
            }

        };

        mRecyclerView.setAdapter(adapter);
    }
}
