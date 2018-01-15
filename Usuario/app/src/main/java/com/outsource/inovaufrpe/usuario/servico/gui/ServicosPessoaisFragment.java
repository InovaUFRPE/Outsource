package com.outsource.inovaufrpe.usuario.servico.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;
import com.outsource.inovaufrpe.usuario.utils.ServicoListHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosPessoaisFragment extends Fragment {
//    FirebaseDatabase firebaseDatabase;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter adapter;
    private TextView tvNenhumServico;


    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    CardFormat cardFormat = new CardFormat();

    public ServicosPessoaisFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_pessoais, container, false);

        mRecyclerView = view.findViewById(R.id.RecycleID);

        tvNenhumServico = view.findViewById(R.id.nenhum_servico);
        String s = getContext().getString(R.string.nenhum_servico) + " novo";
        tvNenhumServico.setText(s);
        tvNenhumServico.setVisibility(View.GONE);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton bCriarServico = view.findViewById(R.id.inserirBtn);

        bCriarServico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), CadastroServicoActivity.class));
            }
        });

        adaptador();

        return view;

    }

    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("servico").child("aberto");
        Query query = databaseReference.orderByChild("idCriador").equalTo(firebaseAuth.getCurrentUser().getUid());
        adapter = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, query) {

            @Override
            protected void populateViewHolder(final ServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                viewHolder.data.setText(cardFormat.dataFormat(model.getData()));
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
                        Servico servico = (Servico) adapter.getItem(position);
                        it.putExtra("nomeServico", servico.getNome());
                        it.putExtra("servicoID", servico.getId());
                        it.putExtra("estado", servico.getEstado());
                        startActivity(it);
                    }

                });
                return viewHolder;
            }

        };

        //verifica se tem algum servico pessoal
        if (adapter.getItemCount() > 0) {
            tvNenhumServico.setVisibility(View.GONE);
        } else {
            tvNenhumServico.setVisibility(View.VISIBLE);
        }

        mRecyclerView.setAdapter(adapter);
    }
}
