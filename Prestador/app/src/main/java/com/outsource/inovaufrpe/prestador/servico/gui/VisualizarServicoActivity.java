package com.outsource.inovaufrpe.prestador.servico.gui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.conversa.gui.MensagemActivity;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.carteira.dominio.God;
import com.outsource.inovaufrpe.prestador.conversa.dominio.Mensagem;
import com.outsource.inovaufrpe.prestador.notificacao.dominio.Notificacao;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Critica;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.prestador.adapter.CriticaViewHolder;
import com.outsource.inovaufrpe.prestador.utils.FirebaseAux;
import com.outsource.inovaufrpe.prestador.utils.FirebaseUtil;
import com.outsource.inovaufrpe.prestador.utils.NotaMedia;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

public class VisualizarServicoActivity extends AppCompatActivity {

    AlertDialog dialog;
    View view;
    TextView tituloID;
    TextView valorID;
    TextView descricaoID;
    TextView tituloLayoutPessoa;
    TextView tvNomePessoa;
    TextView tvNotaPessoa;
    TextView tvNomeOfertante;
    TextView tvOferta;
    TextView tvEstadoServicoID;
    Button solicNovoOrca;
    EditText precoServico;
    EditText mensagem;
    Button cancelarNegociacao;
    Button btNegociar;
    Button btMensagem;
    Button btAceitarOferta;
    Button btConcluir;
    String servicoId;
    String estadoId;
    FirebaseAuth firebaseAuth;
    Servico servico;
    DatabaseReference databaseReferenceServico;
    ValueEventListener listenerServico;
    String nomeSolicitante;
    String nomeServico;
    CardFormat cardFormat = new CardFormat();
    int peso;
    int somatorio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        estadoId = intent.getStringExtra("estado");
        nomeServico = intent.getStringExtra("nomeServico");
        setTitle(getString(R.string.visualizar_servico));
        view = findViewById(R.id.ly);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceServico = FirebaseDatabase.getInstance().getReference().child("servico");

        tituloID = findViewById(R.id.tvNomeServico);
        valorID = findViewById(R.id.tvPrecoServico);
        descricaoID = findViewById(R.id.tvDescricaoServico);
        tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);
        tvNomePessoa = findViewById(R.id.tvNomePessoa);
        tvNotaPessoa = findViewById(R.id.tvNotaPessoa);
        tvNomeOfertante = findViewById(R.id.tvNomeOfertante);
        tvOferta = findViewById(R.id.tvPrecoOrcamento);
        tvEstadoServicoID = findViewById(R.id.tvEstadoServicoID);
        btNegociar = findViewById(R.id.btnNegociar);
        btConcluir = findViewById(R.id.btnConcluirServico);
        btMensagem = findViewById(R.id.btMensagemID);
        btAceitarOferta = findViewById(R.id.btAceitarNegociacao);
        LinearLayout solicitanteLayout = findViewById(R.id.layoutPessoa);
        LinearLayout negociacaoLayout = findViewById(R.id.layoutNegociacoes);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeServico);

        tituloLayoutPessoa.setText(R.string.solicitado_por);

        dadosServico();

//        btNegociar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                criarDialogNegociacao();
//                final DecimalFormat df = new DecimalFormat("####0.00");
//                precoServico.setText(df.format(Float.parseFloat(servico.getOferta().toString())));
//
//                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if ((df.format(Float.parseFloat(precoServico.getText().toString().replace(",", ".")))).equals(servico.getOferta())) {
//                            encerraDialog();
//                        } else {
//                            try {
//                                negociar();
//                                atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO.getValue());
//                            } catch (DatabaseException e) {
//                                Toast.makeText(VisualizarServicoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//            }
//        });

        btConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concluir();
            }
        });

        //BOTAO QUE VAI VIRAR SWIPE
        Button btnSolict = findViewById(R.id.btnSolicitarOrcamento);
        btnSolict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                criarDialogNegociacao();
                final DecimalFormat df = new DecimalFormat("####0.00");
                precoServico.setText(df.format(Float.parseFloat(servico.getPreco().toString())));

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            negociar();
                            atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO.getValue());
                        } catch (DatabaseException e) {
                            Toast.makeText(VisualizarServicoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btAceitarOferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())) {
                    databaseReferenceServico.child(servicoId).child("preco").setValue(servico.getOferta()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.ANDAMENTO.getValue());
                            }else{
                                Toast.makeText(VisualizarServicoActivity.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(VisualizarServicoActivity.this, "Você quem realizou a ultima oferta!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        solicitanteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogVisualizarPerfil();
            }
        });

        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizarServicoActivity.this, MensagemActivity.class);
                intent.putExtra("servicoID", servicoId);
                intent.putExtra("prestadorID",  firebaseAuth.getCurrentUser().getUid());
                intent.putExtra("usuarioID", servico.getIdCriador());
                intent.putExtra("nomeServico",  servico.getNome());
                intent.putExtra("estado", servico.getEstado());
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("usuario").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nomeSolicitante = dataSnapshot.child("nome").getValue(String.class)+" "+dataSnapshot.child("sobrenome").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void concluir() {
        //CORRIGIR A AVALIZAÇÃO DE USUARIO TARDIAgh
        FirebaseDatabase.getInstance().getReference().child("vizualizacao").child(estadoId).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("concluido")) {
                    if (dataSnapshot.child("concluido").getValue().equals(firebaseAuth.getCurrentUser().getUid())) {
                        Toast.makeText(VisualizarServicoActivity.this, "Você já marcou este serviço como concluido", Toast.LENGTH_SHORT).show();
                    } else {
                        adicionar();
                        atualizarEstadoServico(servico.getEstado(), EstadoServico.CONCLUIDA.getValue());
                        databaseReferenceServico.child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                        FirebaseDatabase.getInstance().getReference().child("vizualizacao").child(estadoId).child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                        criarDialogAvaliarUsuario();
                    }
                } else {
                    criarDialogAvaliarUsuario();
                    databaseReferenceServico.child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                    databaseReferenceServico.child(servicoId).child("concluido").setValue(firebaseAuth.getCurrentUser().getUid());
                    FirebaseDatabase.getInstance().getReference().child("vizualizacao").child(estadoId).child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                    FirebaseDatabase.getInstance().getReference().child("vizualizacao").child(estadoId).child(servicoId).child("concluido").setValue(firebaseAuth.getCurrentUser().getUid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dadosServico();

    }

    private void definirLayout(){
        if (estadoId.equals(EstadoServico.ABERTA.getValue())) {
            tvEstadoServicoID.setText(R.string.aberta);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnConcluir).setVisibility(View.GONE);
        } else if (estadoId.equals(EstadoServico.NEGOCIACAO.getValue())) {
            tvEstadoServicoID.setText(R.string.em_negociacao);
            findViewById(R.id.layoutBtnConcluir).setVisibility(View.GONE);
            findViewById(R.id.btnSolicitarOrcamento).setVisibility(View.GONE);
            findViewById(R.id.btnNegociar).setVisibility(View.GONE);

        } else if (estadoId.equals(EstadoServico.ANDAMENTO.getValue())) {
            tvEstadoServicoID.setText(R.string.em_andamento);
            findViewById(R.id.layoutBtnNegociar).setVisibility(View.GONE);
            findViewById(R.id.btnSolicitarOrcamento).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnAceitarOferta).setVisibility(View.GONE);

        } else {
            tvEstadoServicoID.setText(R.string.finalizada);
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
            findViewById(R.id.btnSolicitarOrcamento).setVisibility(View.GONE);
        }
    }

    private void encerraDialog() {
        this.dialog.dismiss();
    }

    private void adicionar() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                God carteira = new God(dataSnapshot.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").getValue(Double.class));
                carteira.adicionar(dataSnapshot.child("vizualizacao").child("andamento").child(servicoId).child("oferta").getValue(Double.class));
                carteira.aplicarTaxa(dataSnapshot.child("vizualizacao").child("andamento").child(servicoId).child("oferta").getValue(Double.class));
                databaseReference.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").setValue(carteira.getMoeda());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dadosNegociacao() {
        if(servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())){
            tvNomeOfertante.setText("Você");
        }else{
            tvNomeOfertante.setText(tvNomePessoa.getText().toString());
        }
        tvOferta.setText(cardFormat.dinheiroFormat(servico.getOferta().toString()));
    }

    private void negociar() throws DatabaseException {
        servico.setOferta(Double.valueOf(precoServico.getText().toString().replace(",", ".")));
        DatabaseReference databaseReferenceVizualizacao = FirebaseDatabase.getInstance().getReference().child("vizualizacao");
        databaseReferenceServico.child(servicoId).child("oferta").setValue(servico.getOferta()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });
        databaseReferenceVizualizacao.child(estadoId).child(servicoId).child("oferta").setValue(servico.getOferta());
        databaseReferenceServico.child(servicoId).child("ofertante").setValue(firebaseAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });

    }

    private void dadosServico() {
        databaseReferenceServico.child(servicoId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servico = dataSnapshot.getValue(Servico.class);
                if (servico != null) {
                    tituloID.setText(servico.getNome());
                    valorID.setText(cardFormat.dinheiroFormat(String.valueOf(servico.getPreco())));
                    descricaoID.setText(servico.getDescricao());
                    servico.setIdPrestador(servico.getOfertante());
                    estadoId = servico.getEstado();
                    definirLayout();
                    dadosUsuario();
                } else {
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void dadosUsuario() {
        DatabaseReference databaseReferenceUsuario = FirebaseDatabase.getInstance().getReference().child("usuario");
        databaseReferenceUsuario.child(servico.getIdCriador()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.child("nome").getValue(String.class) + " " + dataSnapshot.child("sobrenome").getValue(String.class);
                tvNomePessoa.setText(s);
                somatorio = dataSnapshot.child("nota").getValue(int.class);
                peso = dataSnapshot.child("pesoNota").getValue(int.class);
                if(peso != 0) {
                    tvNotaPessoa.setText(String.format("%.02f", (float)somatorio / peso));
                }else{
                    tvNotaPessoa.setText("0.0");
                }
                /*if (servico.getOfertante() != null) {
                    dadosNegociacao();
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void atualizarEstadoServico(String estadoAtual, String estadoDestino) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        FirebaseUtil fu = new FirebaseUtil();
        DatabaseReference databaseReferenceVizualizacao = FirebaseDatabase.getInstance().getReference().child("vizualizacao");
        Toast.makeText(this, estadoAtual, Toast.LENGTH_SHORT).show();
        try {
            if (estadoAtual.equals(EstadoServico.ABERTA.getValue())) {
                databaseReferenceVizualizacao.child(estadoAtual).child(servicoId).child("idPrestador").setValue(firebaseAuth.getCurrentUser().getUid());
            }
            if (!estadoAtual.equals(estadoDestino)) {
                fu.moverServico(databaseReferenceVizualizacao.child(estadoAtual).child(servicoId), databaseReferenceVizualizacao.child(estadoDestino).child(servicoId), estadoDestino);
                databaseReferenceServico.child(servicoId).child("estado").setValue(estadoDestino);
            }

        } catch (DatabaseException e) {
            Toast.makeText(VisualizarServicoActivity.this, "Falha na solicitacao" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Snackbar.make(view, "Serviço em processo de " + estadoDestino, Snackbar.LENGTH_LONG).show();
    }

    private void enviarNotificacao(int tiponotificacao){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Notificacao notificacao = new Notificacao();
        notificacao.setServicoID(servico.getId());
        notificacao.setNomeServico(servico.getNome());
        notificacao.setEstado(servico.getEstado());
        notificacao.setTipoNotificacao(tiponotificacao);

        databaseReference.child("Notificacao").child("usuario").child(servico.getIdCriador()).push().setValue(notificacao);

    }

    private void criarDialogNegociacao() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_negociacao_servico, null);

        precoServico = view1.findViewById(R.id.etPrecoServico);
        //mensagem = view1.findViewById(R.id.etMensagens);
        cancelarNegociacao = view1.findViewById(R.id.btnCancelarNegociacao);
        solicNovoOrca = view1.findViewById(R.id.btnSolicitarNovoOrcamento);
        view1.findViewById(R.id.btnCancelarNegociacao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        mBuilder.setView(view1);
        dialog = mBuilder.create();
        dialog.show();

    }

    /**
     * Método para chamar dialog de perfil
     */
    private void criarDialogVisualizarPerfil() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        View v1 = getLayoutInflater().inflate(R.layout.dialog_visualizar_perfil, null);
        FirebaseRecyclerAdapter adapter;

        TextView nomeUsuario = v1.findViewById(R.id.tvNomePerfil);
        RatingBar avaliarPerfil = v1.findViewById(R.id.rbAvaliarServico);
        nomeUsuario.setText(tvNomePessoa.getText().toString());
        if (peso == 0){
            avaliarPerfil.setRating(0);
        }else{
            avaliarPerfil.setRating(Float.parseFloat(tvNotaPessoa.getText().toString().replace(",",".")));
        }

        RecyclerView mRecyclerView = v1.findViewById(R.id.RecycleComentarioID);
        ImageButton closeBtn = v1.findViewById(R.id.closeBtn);
        ImageButton chatBtn = v1.findViewById(R.id.chatButton);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizarServicoActivity.this,HistoricoServicoActivity.class);
                intent.putExtra("solicitanteNome",tvNomePessoa.getText().toString());
                intent.putExtra("ID",servico.getIdCriador());
                startActivity(intent);
            }
        });

        mRecyclerView.setFocusable(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisualizarServicoActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("feedback").child("usuario").child(servico.getIdCriador()).orderByChild("data");

        adapter = new FirebaseRecyclerAdapter<Critica, CriticaViewHolder>(Critica.class, R.layout.card_critica, CriticaViewHolder.class, query) {

            @Override
            protected void populateViewHolder(CriticaViewHolder viewHolder, Critica model, int position) {
                viewHolder.mainLayout.setVisibility(View.VISIBLE);
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.tvComentador.setText(model.getComentadorNome());
                viewHolder.tvNota.setText(String.valueOf(model.getNota()));
                viewHolder.tvComentario.setText(model.getComentario());

            }
        };

        mRecyclerView.setAdapter(adapter);
        mBuilder.setView(v1);
        dialog = mBuilder.create();
        dialog.show();
    }

    /**
     * Método para chamar dialog de avaliação pós-servico
     */
    private void criarDialogAvaliarUsuario() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        @SuppressLint("InflateParams") View v1 = getLayoutInflater().inflate(R.layout.dialog_avaliacao_usuario, null);

        Button btnConcluir = v1.findViewById(R.id.btnAvaliarConcluir);
        final EditText edComentarioAvaliacao = v1.findViewById(R.id.etComentario);
        final RatingBar ratingBar = v1.findViewById(R.id.rbAvaliarServico);
        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Critica critica = new Critica();
                critica.setComentadorNome(nomeSolicitante);
                critica.setComentario(edComentarioAvaliacao.getText().toString());
                critica.setNota((int) ratingBar.getRating());
                DatabaseReference databaseReference = FirebaseAux.getInstancia().getDatabaseReference();
                NotaMedia notaMedia = new NotaMedia();
                notaMedia.adicionarNota(servico.getIdCriador(),critica.getNota());
                databaseReference.child("feedback").child("usuario").child(servico.getIdCriador()).child(databaseReference.child("feedback").child("usuario").child(servico.getIdCriador()).push().getKey()).setValue(critica);
                dialog.dismiss();
            }
        });

        mBuilder.setView(v1);
        dialog = mBuilder.create();
        dialog.show();

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
        FirebaseDatabase.getInstance().getReference().child("mensagem").child(servicoId).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(novaData).setValue(mensagem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(!task.isSuccessful()){
                    Toast.makeText(VisualizarServicoActivity.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (estadoId.equals(EstadoServico.ANDAMENTO.getValue())){
            getMenuInflater().inflate(R.menu.cancelar_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cancelarBtn){
            cancelarServico();
        }
        return super.onContextItemSelected(item);
    }

    public void cancelarServico(){
        servico.setIdPrestador("");
        servico.setOfertante("");
        servico.setOferta(Double.valueOf(0));
        atualizarEstadoServico(estadoId,EstadoServico.ABERTA.getValue());
        finish();
        startActivity(new Intent(VisualizarServicoActivity.this, MainActivity.class));
    }*/
}
