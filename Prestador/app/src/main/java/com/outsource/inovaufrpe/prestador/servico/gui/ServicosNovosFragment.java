package com.outsource.inovaufrpe.prestador.servico.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.ServicoListHolder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ServicosNovosFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter adapter;

    DatabaseReference databaseReference;

    CardFormat cardFormat = new CardFormat();

    public ServicosNovosFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        View layout = inflater.inflate(R.layout.fragment_servicos_novos, container, false);
        mRecyclerView = layout.findViewById(R.id.recycleID);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        adaptador();

        return layout;
    }

    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference("servico").child("aberto");
        adapter = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, databaseReference.orderByChild("ordem-ref")) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                viewHolder.data.setText(cardFormat.dataFormat(model.getData()));
                viewHolder.valor.setText(cardFormat.dinheiroFormat(model.getPreco().toString()));

            }

            @Override
            public ServicoListHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ServicoListHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ServicoListHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it = new Intent(getActivity(), VisualizarServicoActivity.class);
                        Servico servico = (Servico) adapter.getItem(position);
                        it.putExtra("servicoID", servico.getId());
                        it.putExtra("estado", servico.getEstado());
                        startActivity(it);
                    }

                });
                return viewHolder;
            }

        };

        mRecyclerView.setAdapter(adapter);
    }

}
