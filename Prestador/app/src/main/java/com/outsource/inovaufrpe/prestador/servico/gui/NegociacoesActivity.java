package com.outsource.inovaufrpe.prestador.servico.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Comentario;
import com.outsource.inovaufrpe.prestador.prestador.gui.MainActivity;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.ComentarioViewHolder;
import com.outsource.inovaufrpe.prestador.utils.FirebaseUtil;

public class NegociacoesActivity extends AppCompatActivity {

    RecyclerView recycleNegociacoes;
    private FirebaseRecyclerAdapter adapter;
    private ValueEventListener valueEventListener;
    private RecyclerView.LayoutManager mLayoutManager;
    String servicoId;
    String myUserID;
    String nomeSolicitante;
    String estadoID;
    Button cancelarNegociacao;
    private DatabaseReference databaseReference;
    private CardView cardNegociacao;
    FirebaseUtil fu = new FirebaseUtil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negociacoes);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        myUserID = intent.getStringExtra("myUserID");
        nomeSolicitante = intent.getStringExtra("nomeUsuario");
        estadoID = intent.getStringExtra("estadoID");
        cancelarNegociacao = findViewById(R.id.btCancelarNegociacao);

        setTitle("Visualizar negociação");

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeSolicitante);

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
                viewHolder.precoSugerido.setText(CardFormat.dinheiroFormat(model.getvalor().toString()));
                viewHolder.tvComentario.setText(model.getTexto());
                viewHolder.tvTempo.setText(CardFormat.tempoFormat(model.getTempo()));
                cardNegociacao = viewHolder.cardServico;

                if (model.getAutor().equals(myUserID)) {
                    setarMarginCard(cardNegociacao, 100, 16);
                    cardNegociacao.setCardBackgroundColor(getResources().getColor(R.color.colorIsabelline));
                } else {
                    setarMarginCard(cardNegociacao, 16, 100);
                }
            }
        };

        cancelarNegociacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("servico").child(estadoID).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Servico servico = dataSnapshot.getValue(Servico.class);
                        servico.setOfertante("");
                        servico.setOferta(servico.getPreco());
                        fu.moverServico(databaseReference.child("servico").child(estadoID).child(servicoId),
                                databaseReference.child("servico").child(EstadoServico.ABERTA.getValue()).child(servicoId),
                                EstadoServico.ABERTA.getValue());
                        databaseReference.child("servico").child(estadoID).child(servicoId).setValue(servico);
                        databaseReference.child("servico").child(estadoID).child(servicoId).child("idPrestador").removeValue();
                        startActivity(new Intent(NegociacoesActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        recycleNegociacoes.setAdapter(adapter);
    }

    private void setarMarginCard(CardView card, int left, int right) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        layoutParams.setMargins(left, 8, right, 8);
        card.requestLayout();
    }
}
