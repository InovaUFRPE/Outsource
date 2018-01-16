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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.ServicoListHolder;

public class ServicosConcluidosFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter adapter;
    private TextView tvNenhumServico;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    CardFormat cardFormat = new CardFormat();

    public ServicosConcluidosFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_servicos_concluidos, container, false);

        mRecyclerView = layout.findViewById(R.id.recycleID);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        tvNenhumServico = layout.findViewById(R.id.nenhum_servico);
        String s = getContext().getString(R.string.nenhum_servico) + " concluído";
        tvNenhumServico.setText(s);
        tvNenhumServico.setVisibility(View.GONE);

        adaptador();

        return layout;
    }

    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference("servico").child("concluido");
        Query query = databaseReference.orderByChild("idPrestador").equalTo(firebaseAuth.getCurrentUser().getUid());
        adapter = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, query) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                viewHolder.data.setText(cardFormat.dataFormat(model.getData(),"dd/MM/yyyy"));
                viewHolder.valor.setText(cardFormat.dinheiroFormat(model.getOferta().toString()));

                if (model.isUrgente()) {
                    viewHolder.barraTipoServico.setBackgroundColor(getResources().getColor(R.color.colorDanger));
                } else {
                    viewHolder.barraTipoServico.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                }

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
                        it.putExtra("nomeServico", servico.getNome());
                        startActivity(it);
                    }

                });
                return viewHolder;
            }

            @Override
            public int getItemCount(){
                if (super.getItemCount() < 1) {
                    tvNenhumServico.setVisibility(View.VISIBLE);
                }else{
                    tvNenhumServico.setVisibility(View.GONE);
                }
                return super.getItemCount();
            }

        };

        mRecyclerView.setAdapter(adapter);
    }

}