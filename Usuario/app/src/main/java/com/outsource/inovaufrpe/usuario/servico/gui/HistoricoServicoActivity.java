package com.outsource.inovaufrpe.usuario.servico.gui;

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
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;
import com.outsource.inovaufrpe.usuario.utils.ServicoListHolder;

public class HistoricoServicoActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter adapter;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    CardFormat cardFormat = new CardFormat();
    String nomePrestador;
    String prestadorId;

    public HistoricoServicoActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_servico);
        Intent intent = getIntent();
        nomePrestador = intent.getStringExtra("prestadorNome");
        setTitle("Histórico de "+nomePrestador);
        prestadorId = intent.getStringExtra("prestadorID");

        mRecyclerView = findViewById(R.id.recycleID);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HistoricoServicoActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        adaptador();

    }
    private void adaptador() {
        databaseReference = FirebaseDatabase.getInstance().getReference("servico").child("concluido");
        Query query = databaseReference.orderByChild("idPrestador").equalTo(prestadorId);
        adapter = new FirebaseRecyclerAdapter<Servico, ServicoListHolder>(Servico.class, R.layout.card_servico, ServicoListHolder.class, query) {

            @Override
            protected void populateViewHolder(ServicoListHolder viewHolder, Servico model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.titulo.setText(model.getNome());
                viewHolder.status.setText(model.getEstado());
                viewHolder.data.setText(cardFormat.dataFormat(model.getData(),"dd/MM"));
                viewHolder.valor.setText(cardFormat.dinheiroFormat(model.getOferta().toString()));

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
