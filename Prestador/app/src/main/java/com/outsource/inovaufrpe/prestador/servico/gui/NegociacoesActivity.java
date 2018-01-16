package com.outsource.inovaufrpe.prestador.servico.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Comentario;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.ComentarioViewHolder;

public class NegociacoesActivity extends AppCompatActivity {

    RecyclerView recycleNegociacoes;
    private FirebaseRecyclerAdapter adapter;
    private ValueEventListener valueEventListener;
    private RecyclerView.LayoutManager mLayoutManager;
    String servicoId;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacoes);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        recycleNegociacoes = findViewById(R.id.recycleNegociacoesID);

        recycleNegociacoes.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recycleNegociacoes.setLayoutManager(mLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("comentario").child(servicoId).orderByKey();
        adapter = new FirebaseRecyclerAdapter<Comentario, ComentarioViewHolder>(Comentario.class, R.layout.card_comentario_negociacao, ComentarioViewHolder.class, query) {
            @Override
            protected void populateViewHolder(ComentarioViewHolder viewHolder, Comentario model, int position) {
                viewHolder.nomeUsuario.setText(model.getNomeAutor());
                viewHolder.precoSugerido.setText(CardFormat.dinheiroFormat(CardFormat.dinheiroRefactor(model.getvalor().toString())));
                viewHolder.tvComentario.setText(model.getTexto());
                viewHolder.tvTempo.setText(CardFormat.tempoFormat(model.getTempo()));
            }
        };

        recycleNegociacoes.setAdapter(adapter);


    }
}
