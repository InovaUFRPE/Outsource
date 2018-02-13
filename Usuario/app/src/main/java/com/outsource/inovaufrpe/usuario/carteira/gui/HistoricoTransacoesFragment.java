package com.outsource.inovaufrpe.usuario.carteira.gui;


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
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;
import com.outsource.inovaufrpe.usuario.utils.HistoricoServicoListHolder;

public class HistoricoTransacoesFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private FirebaseRecyclerAdapter adapter;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public HistoricoTransacoesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        Query query = databaseReference.orderByChild("idCriador").equalTo(firebaseAuth.getCurrentUser().getUid());
        adapter = new FirebaseRecyclerAdapter<Servico, HistoricoServicoListHolder>(Servico.class, R.layout.row_historico_negociacoes, HistoricoServicoListHolder.class, query) {

            @Override
            protected void populateViewHolder(HistoricoServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.nomeServico.setText(model.getNome());
                viewHolder.data.setText(CardFormat.dataFormat(model.getDataf(),"dd/MM"));
                viewHolder.preco.setText(CardFormat.dinheiroFormat(model.getPreco().toString()));

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
