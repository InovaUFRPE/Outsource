package com.outsource.inovaufrpe.usuario.servico.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.adapter.ServicoListHolder;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;


public class ServicosAndamentoFragment extends Fragment {

    private RecyclerView mRecyclerViewAndamento;

    private FirebaseRecyclerAdapter adapter2;

    private RelativeLayout lyNenhumServico;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public ServicosAndamentoFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        View view = inflater.inflate(R.layout.fragment_servicos_andamento, container, false);

        lyNenhumServico = view.findViewById(R.id.nenhum_servico);
        TextView tvNenhumServico = view.findViewById(R.id.tvNenhumServico);
        String s = getText(R.string.nenhum_servico) + " em andamento";
        tvNenhumServico.setText(s);

        mRecyclerViewAndamento = view.findViewById(R.id.Recycle2ID);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity());
        mRecyclerViewAndamento.setLayoutManager(mLayoutManager2);

        adaptador();

        return view;

    }

    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query queryAndamento = databaseReference.child("visualizacao").child("andamento").orderByChild("idCriador").equalTo(firebaseAuth.getCurrentUser().getUid());

        adapter2 = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, queryAndamento) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, Servico model, int position) {
                getItemCount();
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                viewHolder.data.setText(CardFormat.dataFormat(model.getData(),"dd/MM/yyyy"));
                viewHolder.valor.setText(CardFormat.dinheiroFormat(model.getPreco().toString()));

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
                        Servico servico = (Servico) adapter2.getItem(position);
                        it.putExtra("nomeServico", servico.getNome());
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
                    lyNenhumServico.setVisibility(View.VISIBLE);
                }else{
                    lyNenhumServico.setVisibility(View.GONE);
                }
                return super.getItemCount();
            }

        };

        mRecyclerViewAndamento.setAdapter(adapter2);
    }

}
