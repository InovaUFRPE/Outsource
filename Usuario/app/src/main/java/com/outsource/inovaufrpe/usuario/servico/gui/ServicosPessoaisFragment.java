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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.utils.ServicoListHolder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class ServicosPessoaisFragment extends Fragment {
//    FirebaseDatabase firebaseDatabase;

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter adapter;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

//    DinheiroFormat dinheiroFormat;

    public ServicosPessoaisFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) throws NullPointerException {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_pessoais, container, false);

        mRecyclerView = view.findViewById(R.id.RecycleID);

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
            protected void populateViewHolder(ServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                try {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    viewHolder.data.setText(dateFormat.format(dateFormat2.parse(model.getData())));

                } catch (ParseException e) {
                }
                DecimalFormat df = new DecimalFormat("####0.00");
                String s = "R$ " + df.format(Float.parseFloat(model.getPreco().toString())).replace(".", ",");
                viewHolder.valor.setText(s);

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
