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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.ServicoView;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.servico.adapter.ServicoListHolder;


public class ServicosAndamentoFragment extends Fragment {

//    private RecyclerView mRecyclerViewNegociacao;
    private RecyclerView mRecyclerViewAndamento;
    CardFormat cardFormat = new CardFormat();

//    private FirebaseRecyclerAdapter adapter1;
    private FirebaseRecyclerAdapter adapter2;

    private TextView tvNenhumServico;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public ServicosAndamentoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        View view = inflater.inflate(R.layout.fragment_servicos_andamento, container, false);

        tvNenhumServico = view.findViewById(R.id.nenhum_servico);
        tvNenhumServico.setVisibility(View.GONE);

//        mRecyclerViewNegociacao = view.findViewById(R.id.RecycleID);
        mRecyclerViewAndamento = view.findViewById(R.id.Recycle2ID);

//        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
//        mRecyclerViewNegociacao.setLayoutManager(mLayoutManager1);
        mRecyclerViewAndamento.setLayoutManager(mLayoutManager2);

        adaptador();

        /*Spinner spinner1 = view.findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String s;
                switch (pos) {
                    case 0:
                        mRecyclerViewNegociacao.setVisibility(View.GONE);
                        mRecyclerViewAndamento.setVisibility(View.VISIBLE);
                        s = getContext().getString(R.string.nenhum_servico) + " em execução";
                        tvNenhumServico.setText(s);
                        break;
                    case 1:
                        mRecyclerViewNegociacao.setVisibility(View.VISIBLE);
                        mRecyclerViewAndamento.setVisibility(View.GONE);
                        tvNenhumServico.setText(R.string.nenhum_servico);
                        tvNenhumServico.setVisibility(View.VISIBLE);
                        s = getContext().getString(R.string.nenhum_servico) + " em negociação";
                        tvNenhumServico.setText(s);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mRecyclerViewNegociacao.setVisibility(View.GONE);
                mRecyclerViewAndamento.setVisibility(View.GONE);
            }
        });*/

        return view;

    }

    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        Query queryNegociacao = databaseReference.child("vizualizacao").child("negociacao").orderByChild("idPrestador").equalTo(firebaseAuth.getCurrentUser().getUid());
        Query queryAndamento = databaseReference.child("vizualizacao").child("andamento").orderByChild("idPrestador").equalTo(firebaseAuth.getCurrentUser().getUid());

        /*adapter1 = new FirebaseRecyclerAdapter<ServicoView, ServicoListHolder>(ServicoView.class, R.layout.card_servico, ServicoListHolder.class, queryNegociacao) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, ServicoView model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                viewHolder.data.setText(cardFormat.dataFormat(model.getData(),"dd/MM/yyyy"));
                viewHolder.valor.setText(cardFormat.dinheiroFormat(model.getPreco().toString()));

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
                        ServicoView servico = (ServicoView) adapter1.getItem(position);
                        it.putExtra("servicoID", servico.getId());
                        it.putExtra("estado", servico.getEstado());
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

        };*/


        adapter2 = new FirebaseRecyclerAdapter<ServicoView, ServicoListHolder>(ServicoView.class, R.layout.card_servico, ServicoListHolder.class, queryAndamento) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, ServicoView model, int position) {
                if (this.getItemCount() < 0) {
                    tvNenhumServico.setVisibility(View.VISIBLE);
                    return;
                }
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                viewHolder.data.setText(cardFormat.dataFormat(model.getData(),"dd/MM/yyyy"));
                viewHolder.valor.setText(cardFormat.dinheiroFormat(model.getPreco().toString()));

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
                        ServicoView servico = (ServicoView) adapter2.getItem(position);
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


//        mRecyclerViewNegociacao.setAdapter(adapter1);
        mRecyclerViewAndamento.setAdapter(adapter2);
    }

}
