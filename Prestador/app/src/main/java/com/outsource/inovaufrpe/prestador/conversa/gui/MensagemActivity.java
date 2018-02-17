package com.outsource.inovaufrpe.prestador.conversa.gui;

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
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.conversa.adapter.MensagemViewHolder;
import com.outsource.inovaufrpe.prestador.conversa.dominio.Conversa;
import com.outsource.inovaufrpe.prestador.conversa.dominio.Mensagem;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.prestador.servico.gui.VisualizarServicoActivity;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.FirebaseUtil;
import com.outsource.inovaufrpe.prestador.utils.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class MensagemActivity extends AppCompatActivity {

    private RecyclerView recycleNegociacoes;
    private FirebaseRecyclerAdapter adapter;
    private ValueEventListener valueEventListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton btenviar;
    private EditText etMensagem;
    private EditText etValor;
    String servicoId;
    String prestadorID;
    String usuarioID;
    String nomeServico;
    String estado;
//    Button cancelarNegociacao;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private CardView cardNegociacao;
    FirebaseUtil fu = new FirebaseUtil();
    private ArrayList<String> listaAux = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        prestadorID = intent.getStringExtra("prestadorID");
        nomeServico = intent.getStringExtra("nomeServico");
        usuarioID = intent.getStringExtra("usuarioID");
        estado = intent.getStringExtra("estado");
        setTitle("Chat");

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeServico);


        LinearLayout layout = findViewById(R.id.layout_chatbox);

        recycleNegociacoes = findViewById(R.id.recycleNegociacoesID);

        if (estado.equals(EstadoServico.CONCLUIDA.getValue())){
            layout.setVisibility(View.GONE);
            RelativeLayout aviso = findViewById(R.id.toastFixed);
            aviso.setVisibility(View.VISIBLE);
            TextView toastTV = findViewById(R.id.toastMessage);
            toastTV.setText("Este serviço foi concluído, não é possível enviar mensagens");
            recycleNegociacoes.setPadding(0,150,0,0);
        } else {
            etValor = findViewById(R.id.etValorID);
            if (estado.equals(EstadoServico.ANDAMENTO.getValue())) {
                etValor.setVisibility(View.GONE);
            }
            btenviar = findViewById(R.id.btEnviarID);
            etMensagem = findViewById(R.id.etMensagemID);
            btenviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!etMensagem.getText().toString().equals("")) {
                        escreverMensagem(etMensagem.getText().toString(), etValor.getText().toString());
                        etMensagem.setText("");
                        etValor.setText("");
                    }
                }
            });
        }

        recycleNegociacoes.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        recycleNegociacoes.setLayoutManager(mLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        Query query = databaseReference.child("mensagem").child(servicoId).child(prestadorID).orderByKey();

        adapter = new FirebaseRecyclerAdapter<Mensagem, MensagemViewHolder>(Mensagem.class, R.layout.card_mensagem_negociacao, MensagemViewHolder.class, query) {
            @Override
            protected void populateViewHolder(MensagemViewHolder viewHolder, Mensagem model, int position) {
                listaAux.add(String.valueOf(position));
                viewHolder.nomeUsuario.setText(model.getNomeAutor());
                viewHolder.tvMensagem.setText(model.getTexto());
                viewHolder.tvTempo.setText(CardFormat.tempoFormat(model.getTempo()));
                cardNegociacao = viewHolder.cardServico;

                if (model.getAutor().equals(prestadorID)) {
                    setarMarginCard(cardNegociacao, 100, 16);
                    cardNegociacao.setCardBackgroundColor(getResources().getColor(R.color.colorIsabelline));
                } else {
                    setarMarginCard(cardNegociacao, 16, 100);
                }

                if (model.getvalor().equals("")) {
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
        conversa.setServicoNome(nomeServico);
        conversa.setEstadoServico(estado);
        conversa.setTempo(data.getTime());
        conversa.setUltimaMensagem(mensagem.getTexto());
        conversa.setOrdemRef(new Timestamp(-1 * data.getTime()).toString());
        databaseReference.child("conversaPrestador").child(prestadorID).child(servicoId+prestadorID).setValue(conversa);
        conversa.setNotificacao(true);
        databaseReference.child("conversaUsuario").child(usuarioID).child(servicoId+prestadorID).setValue(conversa);
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
            startActivity(it);
        }
        return super.onOptionsItemSelected(item);
    }
}
