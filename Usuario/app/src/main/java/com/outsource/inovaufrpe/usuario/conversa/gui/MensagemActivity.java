package com.outsource.inovaufrpe.usuario.conversa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.conversa.dominio.Conversa;
import com.outsource.inovaufrpe.usuario.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.usuario.conversa.dominio.Mensagem;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.solicitante.gui.MainActivity;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;
import com.outsource.inovaufrpe.usuario.conversa.adapter.MensagemViewHolder;
import com.outsource.inovaufrpe.usuario.utils.FirebaseUtil;

import java.sql.Timestamp;
import java.util.Date;

public class MensagemActivity extends AppCompatActivity {

    RecyclerView recycleNegociacoes;
    private FirebaseRecyclerAdapter adapter;
    private ValueEventListener valueEventListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton btenviar;
    private EditText etMensagem;
    private EditText etValor;
    String servicoId;
    String myUserID;
    String nomePrestador;
    String estadoID;
    Button cancelarNegociacao;
    private DatabaseReference databaseReference;
    private CardView cardNegociacao;
    FirebaseUtil fu = new FirebaseUtil();
    private String prestadorID;
    private String nomeServico;
    private String usuarioID;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        prestadorID = intent.getStringExtra("prestadorID");
        nomeServico = intent.getStringExtra("nomeServico");
        usuarioID = intent.getStringExtra("usuarioID");

        setTitle("Chat");

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeServico);

        btenviar = findViewById(R.id.btEnviarID);
        etMensagem = findViewById(R.id.etMensagemID);
        etValor = findViewById(R.id.etValorID);
        recycleNegociacoes = findViewById(R.id.recycleNegociacoesID);

        recycleNegociacoes.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recycleNegociacoes.setLayoutManager(mLayoutManager);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("mensagem").child(servicoId).child(prestadorID).orderByKey();
        adapter = new FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder>(Mensagem.class, R.layout.card_mensagem_negociacao, MensagemViewHolder.class, query) {
            @Override
            protected void populateViewHolder(MensagemViewHolder viewHolder, Mensagem model, int position) {
                viewHolder.nomeUsuario.setText(model.getNomeAutor());
                viewHolder.precoSugerido.setText(CardFormat.dinheiroFormat(model.getvalor()));
                viewHolder.tvMensagem.setText(model.getTexto());
                viewHolder.tvTempo.setText(CardFormat.tempoFormat(model.getTempo()));
                cardNegociacao = viewHolder.cardServico;

                if (model.getAutor().equals(usuarioID)) {
                    setarMarginCard(cardNegociacao, 100, 16);
                    cardNegociacao.setCardBackgroundColor(getResources().getColor(R.color.colorIsabelline));
                } else {
                    setarMarginCard(cardNegociacao, 16, 100);
                }
            }
        };

        btenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escreverMensagem(etMensagem.getText().toString(), etValor.getText().toString());
                etMensagem.setText("");
                etValor.setText("");
            }
        });

//        cancelarNegociacao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                databaseReference.child("servico").child(estadoID).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Servico servico = dataSnapshot.getValue(Servico.class);
//                        servico.setOfertante("");
//                        servico.setOferta(servico.getPreco());
//                        fu.moverServico(databaseReference.child("servico").child(estadoID).child(servicoId),
//                                databaseReference.child("servico").child(EstadoServico.ABERTA.getValue()).child(servicoId),
//                                EstadoServico.ABERTA.getValue());
//                        databaseReference.child("servico").child(estadoID).child(servicoId).setValue(servico);
//                        databaseReference.child("servico").child(estadoID).child(servicoId).child("idPrestador").removeValue();
//                        startActivity(new Intent(MensagemActivity.this, MainActivity.class));
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });

        recycleNegociacoes.setAdapter(adapter);
    }

    private void escreverMensagem(String texto, String valor) {
        Mensagem mensagem = new Mensagem();
        Date data = new Date();
        String novaData = new Timestamp(data.getTime()).toString();
        mensagem.setTempo(data.getTime());
        mensagem.setTexto(texto);
        mensagem.setAutor(firebaseAuth.getCurrentUser().getUid());
        mensagem.setNomeAutor(firebaseAuth.getCurrentUser().getDisplayName());
        mensagem.setvalor(valor);
        novaData = novaData.replace(".", "");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("mensagem").child(servicoId).child(firebaseAuth.getCurrentUser().getUid()).child(novaData).setValue(mensagem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(!task.isSuccessful()){
                    Toast.makeText(MensagemActivity.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Conversa conversa = new Conversa();
        conversa.setPrestadorID(prestadorID);
        conversa.setUsuarioID(usuarioID);
        conversa.setServicoID(servicoId);
        conversa.setServicoNome(nomeServico);
        conversa.setTempo(data.getTime());
        conversa.setUltimaMensagem(mensagem.getTexto());
        conversa.setOrdemRef(new Timestamp(-1 * data.getTime()).toString());
        databaseReference.child("conversaUsuario").child(usuarioID).child(servicoId+prestadorID).setValue(conversa);
        conversa.setNotificacao(true);
        databaseReference.child("conversaPrestador").child(prestadorID).child(servicoId+prestadorID).setValue(conversa);

    }

    private void setarMarginCard(CardView card, int left, int right) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        layoutParams.setMargins(left, 8, right, 8);
        card.requestLayout();
    }
}
