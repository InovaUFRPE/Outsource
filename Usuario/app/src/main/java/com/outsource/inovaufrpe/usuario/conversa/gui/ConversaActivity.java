package com.outsource.inovaufrpe.usuario.conversa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.outsource.inovaufrpe.usuario.conversa.dominio.Conversa;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;

public class ConversaActivity extends AppCompatActivity {

    RecyclerView recycleConversas;
    private FirebaseRecyclerAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference databaseReference;
    private String servicoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);
        Intent intent = getIntent();
        servicoID = intent.getStringExtra("servicoID");
        recycleConversas = findViewById(R.id.rvConversasID);
        recycleConversas.setHasFixedSize(true);
        setTitle("Conversas");

        mLayoutManager = new LinearLayoutManager(this);
        recycleConversas.setLayoutManager(mLayoutManager);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query;
        if(servicoID != null){
            query = databaseReference.child("conversaUsuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("servicoID").equalTo(servicoID);
        }else{
            query = databaseReference.child("conversaUsuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("ordemRef");
        }
        adapter = new FirebaseRecyclerAdapter<Conversa, ConversaViewHolder>(Conversa.class, R.layout.card_conversa, ConversaViewHolder.class, query) {

            @Override
            protected void populateViewHolder(ConversaViewHolder viewHolder, Conversa model, int position) {
                viewHolder.tvnomeServico.setText(model.getServicoNome());
                viewHolder.tvMensagem.setText(model.getUltimaMensagem());
                viewHolder.tvtempo.setText(CardFormat.tempoFormat(model.getTempo()));


            }

            @Override
            public ConversaViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final ConversaViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new ConversaViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent it = new Intent(ConversaActivity.this, MensagemActivity.class);
                        Conversa conversa = (Conversa) adapter.getItem(position);
                        it.putExtra("servicoID", conversa.getServicoID());
                        it.putExtra("prestadorID", conversa.getPrestadorID());
                        it.putExtra("nomeServico", conversa.getServicoNome());
                        it.putExtra("usuarioID", conversa.getUsuarioID());
                        it.putExtra("estado", conversa.getEstadoServico());
                        conversa.setLido(true);
                        startActivity(it);
                    }

                });
                return viewHolder;
            }
        };

        recycleConversas.setAdapter(adapter);
    }
}
