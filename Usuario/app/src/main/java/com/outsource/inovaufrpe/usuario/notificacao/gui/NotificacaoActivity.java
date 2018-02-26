package com.outsource.inovaufrpe.usuario.notificacao.gui;

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
import com.outsource.inovaufrpe.usuario.conversa.adapter.ConversaViewHolder;
import com.outsource.inovaufrpe.usuario.notificacao.adapter.NotificacaoViewHolder;
import com.outsource.inovaufrpe.usuario.notificacao.dominio.Notificacao;
import com.outsource.inovaufrpe.usuario.servico.gui.VisualizarServicoActivity;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;

public class NotificacaoActivity extends AppCompatActivity {

    RecyclerView recycleNotificacao;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);
        setTitle(R.string.notificacoes);

        recycleNotificacao = findViewById(R.id.rvNotificacaoID);
        recycleNotificacao.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recycleNotificacao.setLayoutManager(mLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("notificacao").child("usuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("ordemRef");
        adapter = new FirebaseRecyclerAdapter<Notificacao, NotificacaoViewHolder>(Notificacao.class, R.layout.card_notificacao, NotificacaoViewHolder.class, query) {

            @Override
            protected void populateViewHolder(NotificacaoViewHolder viewHolder, Notificacao model, int position) {
                viewHolder.tvnomeServico.setText(model.getNomeServico());
                viewHolder.tvTextoNotificacao.setText(model.getTextoNotificacao());
                viewHolder.tvtempo.setText(CardFormat.tempoFormat(model.getTempo()));

                //ver tipo de notificação
                switch (model.getTipoNotificacao()) {
                    case 0://alguem ofertou
                        viewHolder.tipoNotificacao.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        break;
                    case 1://prestador concluiu servico
                        viewHolder.tipoNotificacao.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                        break;
                }

                //mudar fundo se a notificação não tiver lida
                if (!model.isLido()) {
                    viewHolder.cardNotificacao.setCardBackgroundColor(getResources().getColor(R.color.colorTextMuted));
                }
            }

            @Override
            public NotificacaoViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final NotificacaoViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new NotificacaoViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it = new Intent(NotificacaoActivity.this, VisualizarServicoActivity.class);
                        Notificacao notificacao = (Notificacao) adapter.getItem(position);
                        it.putExtra("servicoID", notificacao.getServicoID());
                        it.putExtra("nomeServico", notificacao.getNomeServico());
                        it.putExtra("estado", notificacao.getEstado());
                        if (!notificacao.isLido()) {
                            databaseReference.child("notificacao").child("usuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(notificacao.getNotificacaoID()).child("lido").setValue(true);
                        }
                        startActivity(it);
                    }

                });
                return viewHolder;
            }
        };


        recycleNotificacao.setAdapter(adapter);

        }
}
