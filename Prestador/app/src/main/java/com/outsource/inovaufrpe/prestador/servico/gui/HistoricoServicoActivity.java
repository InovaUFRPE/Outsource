package com.outsource.inovaufrpe.prestador.servico.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.outsource.inovaufrpe.prestador.servico.adapter.ServicoListHolder;

public class HistoricoServicoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter adapter;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    String nomeSolicitante;
    String solicitanteID;

    public HistoricoServicoActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_servico);
        Intent intent = getIntent();
        nomeSolicitante = intent.getStringExtra("solicitanteNome");
        setTitle("Histórico de "+nomeSolicitante);
        solicitanteID = intent.getStringExtra("solicitanteID");

        mRecyclerView = findViewById(R.id.recycleID);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HistoricoServicoActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adaptador();

    }
    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference("visualizacao").child("concluido");
        Query query = databaseReference.orderByChild("idCriador").equalTo(solicitanteID);
        adapter = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, query) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, Servico model, int position) {
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
                    }

                });
                return viewHolder;
            }

        };

        mRecyclerView.setAdapter(adapter);
    }
}
