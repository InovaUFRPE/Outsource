package com.outsource.inovaufrpe.prestador.servico.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.ServicoListHolder;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosAndamentoFragment extends Fragment {

    private RecyclerView mRecyclerViewNegociacao;
    private RecyclerView mRecyclerViewAndamento;

    private FirebaseRecyclerAdapter adapter1;
    private FirebaseRecyclerAdapter adapter2;
    private RecyclerView.LayoutManager mLayoutManager1;
    private RecyclerView.LayoutManager mLayoutManager2;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ToggleButton btRecycleNegociacao;
    private ToggleButton btRecycleAndamento;


    public ServicosAndamentoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_servicos_andamento, container, false);

        mRecyclerViewNegociacao = view.findViewById(R.id.RecycleID);
        mRecyclerViewAndamento = view.findViewById(R.id.Recycle2ID);

        mLayoutManager1 = new LinearLayoutManager(getActivity());
        mLayoutManager2 = new LinearLayoutManager(getActivity());
        mRecyclerViewNegociacao.setLayoutManager(mLayoutManager1);
        mRecyclerViewAndamento.setLayoutManager(mLayoutManager2);
        btRecycleNegociacao = view.findViewById(R.id.btRecycleID);
        btRecycleAndamento = view.findViewById(R.id.btRecycle2ID);

        mRecyclerViewNegociacao.setVisibility(View.GONE);
        mRecyclerViewAndamento.setVisibility(View.GONE);


        btRecycleNegociacao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mRecyclerViewNegociacao.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerViewNegociacao.setVisibility(View.GONE);
                }
            }
        });

        btRecycleAndamento.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mRecyclerViewAndamento.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerViewAndamento.setVisibility(View.GONE);
                }
            }
        });

        adaptador();

        return view;
    }

    private void adaptador(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query queryNegociacao = databaseReference.child("servico").child("negociacao").orderByChild("idPrestador").equalTo(firebaseAuth.getCurrentUser().getUid());
        Query queryAndamento = databaseReference.child("servico").child("andamento").orderByChild("idPrestador").equalTo(firebaseAuth.getCurrentUser().getUid());
        adapter1 = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, queryNegociacao) {

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

                }catch (ParseException e){
                }
                DecimalFormat df = new DecimalFormat("####0.00");
                viewHolder.valor.setText("R$ " + df.format(Float.parseFloat(model.getOferta().toString())).replace(".",","));

            }

            @Override
            public ServicoListHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ServicoListHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ServicoListHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it = new Intent(getActivity(), VisualizarServicoActivity.class);
                        Servico servico = (Servico) adapter1.getItem(position);
                        it.putExtra("servicoID", servico.getId());
                        it.putExtra("estado", servico.getEstado());
                        startActivity(it);
                    }

                });
                return viewHolder;
            }

        };

        adapter2 = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, queryAndamento) {

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

                }catch (ParseException e){
                }
                DecimalFormat df = new DecimalFormat("####0.00");
                viewHolder.valor.setText("R$ " + df.format(Float.parseFloat(model.getOferta().toString())).replace(".",","));

            }

            @Override
            public ServicoListHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ServicoListHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ServicoListHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it = new Intent(getActivity(), VisualizarServicoActivity.class);
                        Servico servico = (Servico) adapter2.getItem(position);
                        it.putExtra("servicoID", servico.getId());
                        it.putExtra("estado", servico.getEstado());
                        startActivity(it);
                    }

                });
                return viewHolder;
            }

        };

        mRecyclerViewNegociacao.setAdapter(adapter1);
        mRecyclerViewAndamento.setAdapter(adapter2);
    }


}
