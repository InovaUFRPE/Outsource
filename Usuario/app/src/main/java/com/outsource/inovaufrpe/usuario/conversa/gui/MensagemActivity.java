package com.outsource.inovaufrpe.usuario.conversa.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.conversa.adapter.MensagemViewHolder;
import com.outsource.inovaufrpe.usuario.conversa.dominio.Conversa;
import com.outsource.inovaufrpe.usuario.conversa.dominio.Mensagem;
import com.outsource.inovaufrpe.usuario.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.usuario.servico.gui.VisualizarServicoActivity;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;
import com.outsource.inovaufrpe.usuario.utils.FirebaseUtil;
import com.outsource.inovaufrpe.usuario.utils.Utils;

import java.sql.Timestamp;
import java.util.Date;

public class MensagemActivity extends AppCompatActivity {

    private RecyclerView recycleNegociacoes;
    private FirebaseRecyclerAdapter adapter;
    private ValueEventListener valueEventListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton btenviar;
    private EditText etMensagem;
    String servicoId;
    String myUserID;
    String nomePrestador;
//    Button cancelarNegociacao;
    private DatabaseReference databaseReference;
    private CardView cardNegociacao;
    FirebaseUtil fu = new FirebaseUtil();
    private String prestadorID;
    private String estado;
    private String nomeServico;
    private String usuarioID;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        estado = intent.getStringExtra("estado");
        prestadorID = intent.getStringExtra("prestadorID");
        nomeServico = intent.getStringExtra("nomeServico");
        usuarioID = intent.getStringExtra("usuarioID");

        setTitle("Chat");

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeServico);

        LinearLayout layout = findViewById(R.id.layout_chatbox);

        recycleNegociacoes = findViewById(R.id.recycleNegociacoesID);

        if (estado.equals(EstadoServico.CONCLUIDA.getValue())) {
            layout.setVisibility(View.GONE);
            RelativeLayout aviso = findViewById(R.id.toastFixed);
            aviso.setVisibility(View.VISIBLE);
            TextView toastTV = findViewById(R.id.toastMessage);
            toastTV.setText("Este serviço foi concluído, não é possível enviar mensagens");
            recycleNegociacoes.setPadding(0,150,0,0);
        }else {
            btenviar = findViewById(R.id.btEnviarID);
            etMensagem = findViewById(R.id.etMensagemID);
            btenviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!etMensagem.getText().toString().equals("")) {
                        escreverMensagem(etMensagem.getText().toString());
                        etMensagem.setText("");
                    }
                }
            });
        }


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
                viewHolder.tvMensagem.setText(model.getTexto());
                viewHolder.tvTempo.setText(CardFormat.tempoFormat(model.getTempo()));
                cardNegociacao = viewHolder.cardServico;

                if (model.getAutor().equals(usuarioID)) {
                    setarMarginCard(cardNegociacao, 100, 16);
                    cardNegociacao.setCardBackgroundColor(getResources().getColor(R.color.colorIsabelline));
                } else {
                    setarMarginCard(cardNegociacao, 16, 100);
                }

                if (model.getvalor().isEmpty()) {
                    viewHolder.precoSugerido.setVisibility(View.GONE);
                } else {
                    viewHolder.precoSugerido.setText(CardFormat.dinheiroFormat(model.getvalor()));
                }
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recycleNegociacoes.smoothScrollToPosition(adapter.getItemCount());
            }
        });

        recycleNegociacoes.setAdapter(adapter);
    }

    private void escreverMensagem(String texto) {
        Mensagem mensagem = new Mensagem();
        Date data = new Date();
        String novaData = new Timestamp(data.getTime()).toString();
        mensagem.setTempo(data.getTime());
        mensagem.setTexto(texto);
        mensagem.setAutor(firebaseAuth.getCurrentUser().getUid());
        mensagem.setNomeAutor(firebaseAuth.getCurrentUser().getDisplayName());
        mensagem.setvalor("");
        novaData = novaData.replace(".", "");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("mensagem").child(servicoId).child(prestadorID).child(novaData).setValue(mensagem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(!task.isSuccessful()){
                    Utils.criarToast(MensagemActivity.this, "Ocorreu um erro, tente novamente");
                }
            }
        });

        Conversa conversa = new Conversa();
        conversa.setPrestadorID(prestadorID);
        conversa.setUsuarioID(usuarioID);
        conversa.setServicoID(servicoId);
        conversa.setEstadoServico(estado);
        conversa.setServicoNome(nomeServico);
        conversa.setTempo(data.getTime());
        conversa.setUltimaMensagem(mensagem.getTexto());
        conversa.setOrdemRef(new Timestamp(-1 * data.getTime()).toString());
        databaseReference.child("conversaUsuario").child(usuarioID).child(servicoId+prestadorID).setValue(conversa);
        conversa.setNotificacao(true);
        databaseReference.child("conversaPrestador").child(prestadorID).child(servicoId).setValue(conversa);

    }

    private void setarMarginCard(CardView card, int left, int right) {
        ViewGroup.MarginLayoutParams layoutParams =
                (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        layoutParams.setMargins(left, 8, right, 8);
        card.requestLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcoes_conversa_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.verServicoBtn) {
            Intent it = new Intent(this, VisualizarServicoActivity.class);
            it.putExtra("servicoID", servicoId);
            it.putExtra("nomeServico",nomeServico);
            it.putExtra("estado", estado);
            it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }
}
