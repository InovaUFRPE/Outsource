package com.outsource.inovaufrpe.usuario.servico.gui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.carteira.dominio.God;
import com.outsource.inovaufrpe.usuario.conversa.gui.ConversaActivity;
import com.outsource.inovaufrpe.usuario.conversa.gui.MensagemActivity;
import com.outsource.inovaufrpe.usuario.notificacao.dominio.Notificacao;
import com.outsource.inovaufrpe.usuario.servico.adapter.OfertaViewHolder;
import com.outsource.inovaufrpe.usuario.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.usuario.servico.dominio.Oferta;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.servico.dominio.ServicoView;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Critica;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;
import com.outsource.inovaufrpe.usuario.utils.CriticaViewHolder;
import com.outsource.inovaufrpe.usuario.utils.FirebaseUtil;
import com.outsource.inovaufrpe.usuario.utils.NotaMedia;
import com.outsource.inovaufrpe.usuario.utils.Utils;
import com.outsource.inovaufrpe.usuario.utils.swipebutton.SwipeButton;
import com.outsource.inovaufrpe.usuario.utils.swipebutton.interfaces.OnActiveListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisualizarServicoActivity extends AppCompatActivity {

    AlertDialog dialog;
    View view;
    TextView tituloID;
    TextView valorID;
    TextView descricaoID;
    TextView tvNomePessoa;
    TextView tvNotaPessoa;
    TextView tvNomeOfertante;
    TextView tvEstadoServicoID;
    TextView tvOferta;
    Button solicNovoOrca;
    EditText precoServico;
    EditText mensagem;
    Button cancelarNegociacao;
    SwipeButton swipeConcluir;
    Button btConversas;
    String servicoId;
    String estadoId;
    TextView nomeUsuario;
    RatingBar avaliarPerfil;
    FirebaseAuth firebaseAuth;
    Servico servico;
    ServicoView servicoView;
    DatabaseReference databaseReference;
    ValueEventListener listenerServico;
    String nomeSolicitante;
    String nomeServico;
    String uriFotoPrestador;
    int peso;
    int somatorio;
    LinearLayout prestadorLayout;
    TextView tituloLayoutPessoa;
    LinearLayout layoutOfertas;
    LinearLayout negociacaoLayout;
    RecyclerView rvOfertas;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        setTitle(getString(R.string.visualizar_servico));
        view = findViewById(R.id.ly);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        nomeServico = intent.getStringExtra("nomeServico");
        tituloID = findViewById(R.id.tvNomeServico);
        estadoId = intent.getStringExtra("estado");
        valorID = findViewById(R.id.tvPrecoServico);
        descricaoID = findViewById(R.id.tvDescricaoServico);
        tvNomePessoa = findViewById(R.id.tvNomePessoa);
        tvNotaPessoa = findViewById(R.id.tvNotaPessoa);
        tvEstadoServicoID = findViewById(R.id.tvEstadoServicoID);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        tvNomeOfertante = findViewById(R.id.tvNomeOfertanteID);
        tvOferta = findViewById(R.id.tvOfertaValorID);
        prestadorLayout = findViewById(R.id.layoutPessoa);
        tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);
        layoutOfertas = findViewById(R.id.layoutOfertasID);
        rvOfertas = findViewById(R.id.rvOfertasID);
        rvOfertas.setLayoutManager(new LinearLayoutManager(this));
        negociacaoLayout = findViewById(R.id.layoutNegociacoes);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeServico);

        firebaseAuth = FirebaseAuth.getInstance();

        swipeConcluir = findViewById(R.id.btnConcluirServico);

        btConversas = findViewById(R.id.btConversasID);

        swipeConcluir.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                concluir();
            }
        });

        prestadorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogVisualizarPerfil(servico.getIdPrestador(),tvNomePessoa.getText().toString());
            }
        });

        btConversas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizarServicoActivity.this, ConversaActivity.class);
                intent.putExtra("servicoID", servicoId);
                startActivity(intent);
            }
        });

        databaseReference.child("usuario").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nomeSolicitante = dataSnapshot.child("nome").getValue(String.class)+" "+dataSnapshot.child("sobrenome").getValue(String.class);
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

    private void concluir() {
        databaseReference.child("servico").child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("concluido")) {
                    if (dataSnapshot.child("concluido").getValue().toString().equals(firebaseAuth.getCurrentUser().getUid())) {
                        Utils.criarToast(VisualizarServicoActivity.this, "Você já marcou este serviço como concluido");
                    } else {
                        databaseReference.child("servico").child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                        databaseReference.child("visualizacao").child(estadoId).child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                        criarDialogAvaliarUsuario(true);
                    }
                } else {
                    criarDialogAvaliarUsuario(false);
                    databaseReference.child("servico").child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                    databaseReference.child("servico").child(servicoId).child("concluido").setValue(firebaseAuth.getCurrentUser().getUid());
                    databaseReference.child("visualizacao").child(estadoId).child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                    swipeConcluir.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void encerraDialog() {
        this.dialog.dismiss();
    }

    private void descontar(final Oferta oferta) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                God carteira = new God(dataSnapshot.child("usuario").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").getValue(Double.class));
                if (carteira.getMoeda() >= oferta.getOfertaValor()){
                    carteira.subtrair(oferta.getOfertaValor());
                    databaseReference.child("usuario").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").setValue(carteira.getMoeda());
                    servicoView.setIdPrestador(oferta.getPrestadorId());
                    servicoView.setPreco(oferta.getOfertaValor());
                    servico.setPreco(oferta.getOfertaValor());
                    servico.setIdPrestador(oferta.getPrestadorId());
                    databaseReference.child("servico").child(servicoId).setValue(servico);
                    databaseReference.child("visualizacao").child(estadoId).child(servicoId).setValue(servicoView);
                    atualizarEstadoServico(estadoId, EstadoServico.ANDAMENTO.getValue());

                }else {
                    Utils.criarToast(VisualizarServicoActivity.this,"O saldo na carteira é insuficiente.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void aceitarOferta(final Oferta oferta){
        databaseReference.child("servico").child(servicoId).setValue(servico).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    descontar(oferta);
                    enviarNotificacao(0,oferta.getPrestadorId());
                    databaseReference.child("oferta").child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                String id = data.getKey();
                                if(id!= null && !id.equals(servico.getIdPrestador())){
                                    enviarNotificacao(2,id);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }else{
                    Utils.criarToast(VisualizarServicoActivity.this, "Ocorreu um erro, tente novamente");
                }
            }
        });
    }

    private void dadosServico() {
        listenerServico = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servico = dataSnapshot.getValue(Servico.class);
                if (servico != null) {
                    estadoId = servico.getEstado();
                    tituloID.setText(servico.getNome());
                    if (servico.getPreco() == 0) {
                        valorID.setText("A comb.");
                    } else {
                        valorID.setText(CardFormat.dinheiroFormat(String.valueOf(servico.getPreco())));
                    }
                    tvEstadoServicoID.setText(estadoId);
                    descricaoID.setText(servico.getDescricao());
                    estadoId = servico.getEstado();
                    definirLayout();
                    if (servico.getIdPrestador() != null) {
                        dadosUsuario();
                    }
                } else {
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("servico").child(servicoId).addValueEventListener(listenerServico);
        databaseReference.child("visualizacao").child(estadoId).child(servicoId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicoView = dataSnapshot.getValue(ServicoView.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void definirLayout() {
        if (estadoId.equals(EstadoServico.CONCLUIDA.getValue())) {
            layoutOfertas.setVisibility(View.GONE);
            tituloLayoutPessoa.setText(R.string.executado_por);
            negociacaoLayout.setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
        } else if (estadoId.equals(EstadoServico.ANDAMENTO.getValue())) {
            layoutOfertas.setVisibility(View.GONE);
            tvEstadoServicoID.setText(R.string.em_andamento);
            tituloLayoutPessoa.setText(R.string.executado_por);
            negociacaoLayout.setVisibility(View.GONE);
        } else {
            mostrarOfertas();
            tvEstadoServicoID.setText(R.string.aberta);
            prestadorLayout.setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
        }
    }

    private void mostrarOfertas() {
        Query query = databaseReference.child("oferta").child(servicoId).orderByChild("ofertaValor");
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Oferta, OfertaViewHolder>(Oferta.class, R.layout.card_perfil_negociacao, OfertaViewHolder.class, query) {

            @Override
            protected void populateViewHolder(OfertaViewHolder viewHolder, Oferta model, int position) {
                viewHolder.tvNomeOfertante.setText(model.getPrestadorNome());
                viewHolder.tvValorOferta.setText(CardFormat.dinheiroFormat(String.valueOf(model.getOfertaValor())));
                viewHolder.tvTempo.setText(CardFormat.tempoFormat(model.getTempo()));
            }

            @Override
            public OfertaViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
                final OfertaViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new OfertaViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(VisualizarServicoActivity.this);
                        builder.setTitle(R.string.opcao_oferta)
                                .setItems(R.array.opcoes_oferta, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                aceitarOferta(getItem(position));
                                                break;
                                            case 1:
                                                criarDialogVisualizarPerfil(getItem(position).getPrestadorId(), getItem(position).getPrestadorNome());
                                                break;
                                            case 2:
                                                Intent it = new Intent(VisualizarServicoActivity.this, MensagemActivity.class);
                                                it.putExtra("servicoID", servicoId);
                                                it.putExtra("prestadorID", getItem(position).getPrestadorId());
                                                it.putExtra("nomeServico", nomeServico);
                                                it.putExtra("usuarioID", firebaseAuth.getCurrentUser().getUid());
                                                it.putExtra("estado", estadoId);
                                                startActivity(it);
                                                break;
                                        }
                                    }
                                });
                        builder.create();
                        builder.show();
                    }
                });
                return viewHolder;
            }
        };
        rvOfertas.setAdapter(adapter);
    }

    private void dadosUsuario() {
        databaseReference.child("prestador").child(servico.getIdPrestador()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.child("nome").getValue(String.class) + " " + dataSnapshot.child("sobrenome").getValue(String.class);
                tvNomePessoa.setText(s);
                somatorio = dataSnapshot.child("nota").getValue(int.class);
                peso = dataSnapshot.child("pesoNota").getValue(int.class);
                uriFotoPrestador = dataSnapshot.child("foto").getValue(String.class);
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
        try {
            if (estadoAtual.equals(EstadoServico.ABERTA.getValue())) {
                databaseReference.child("visualizacao").child(estadoAtual).child(servicoId).child("idPrestador").setValue(servico.getIdPrestador());
            }
            if (!estadoAtual.equals(estadoDestino)) {
                fu.moverServico(databaseReference.child("visualizacao").child(estadoAtual).child(servicoId), databaseReference.child("visualizacao").child(estadoDestino).child(servicoId), estadoDestino);
                databaseReference.child("servico").child(servicoId).child("estado").setValue(estadoDestino);
                //databaseReference.child("conversaPrestador").child(servico.getIdPrestador()).child(servicoId+servico.getIdPrestador()).child("estadoServico").setValue(estadoDestino);
                //databaseReference.child("conversaUsuario").child(servico.getIdCriador()).child(servicoId+servico.getIdCriador()).child("estadoServico").setValue(estadoDestino);
            }

        } catch (DatabaseException e) {
            Utils.criarToast(VisualizarServicoActivity.this, "Falha na solicitacao" + e.getMessage());
        }
        Snackbar.make(view, "Serviço em processo de " + estadoDestino, Snackbar.LENGTH_LONG).show();
    }

    private void enviarNotificacao(int tiponotificacao, String idPrestador){
        long data = new Date().getTime();
        Notificacao notificacao = new Notificacao();
        notificacao.setServicoID(servico.getId());
        notificacao.setNomeServico(servico.getNome());
        notificacao.setEstado(servico.getEstado());
        notificacao.setTempo(data);
        notificacao.setOrdemRef(new Timestamp(data*-1).toString());
        switch (tiponotificacao){
            case 0: //Oferta aceita
                notificacao.setTextoNotificacao("Um usuário aceitou a sua oferta!");
                break;
            case 1://Conclusão
                notificacao.setTextoNotificacao("O prestador concluiu um dos seus serviços!");
                break;
            case 2://Oferta recusada
                notificacao.setTextoNotificacao("O usuário aceitou a oferta de outro Prestador.");
                break;
            case 3://Servico Concluido
                notificacao.setTextoNotificacao("O usuario marcou o servico como Concluido!");
                break;
        }
        notificacao.setTipoNotificacao(tiponotificacao);
        databaseReference.child("notificacao").child("prestador").child(idPrestador).push().setValue(notificacao);

    }


    private void criarDialogNegociacao() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.dialog_negociacao_servico, null);

        precoServico = view1.findViewById(R.id.etPrecoServico);
        mensagem = view1.findViewById(R.id.etMensagens);
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
    private void criarDialogVisualizarPerfil(final String idPrestador, final String nomePrestador) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        @SuppressLint("InflateParams") final View v1 = getLayoutInflater().inflate(R.layout.dialog_visualizar_perfil, null);
        FirebaseRecyclerAdapter adapter;

        nomeUsuario = v1.findViewById(R.id.tvNomePerfil);
        avaliarPerfil = v1.findViewById(R.id.rbAvaliarServico);

        databaseReference.child("prestador").child(idPrestador).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nomeUsuario.setText(dataSnapshot.child("nome").getValue(String.class) + dataSnapshot.child("sobrenome").getValue(String.class));
                somatorio = dataSnapshot.child("nota").getValue(int.class);
                peso = dataSnapshot.child("pesoNota").getValue(int.class);
                uriFotoPrestador = dataSnapshot.child("foto").getValue(String.class);

                if(uriFotoPrestador != null && !uriFotoPrestador.isEmpty()) {
                    Picasso.with(VisualizarServicoActivity.this).load(Uri.parse(uriFotoPrestador)).centerCrop().fit().into((CircleImageView) v1.findViewById(R.id.profile_image));
                }

                //TODO configurar nota para ofertante
                if (peso == 0){
                    avaliarPerfil.setRating(0);
                }else {
                    avaliarPerfil.setRating((float)somatorio / peso);
                }
                /*if (servico.getOfertante() != null) {
                    dadosNegociacao();
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                intent.putExtra("prestadorNome", nomePrestador);
                intent.putExtra("prestadorID",idPrestador);
                startActivity(intent);
            }
        });


        RecyclerView mRecyclerView = v1.findViewById(R.id.RecycleComentarioID);
        mRecyclerView.setFocusable(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisualizarServicoActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Query query = databaseReference.child("feedback").child("prestador").child(idPrestador).orderByChild("data");

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
    private void criarDialogAvaliarUsuario(final boolean conclusao) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        @SuppressLint("InflateParams") View v1 = getLayoutInflater().inflate(R.layout.dialog_avaliacao_usuario, null);

        Button btnConcluir = v1.findViewById(R.id.btnAvaliarConcluir);
        final EditText edComentarioAvaliacao = v1.findViewById(R.id.etMensagens);
        final RatingBar ratingBar = v1.findViewById(R.id.rbAvaliarServico);
        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Critica critica = new Critica();
                critica.setComentadorNome(nomeSolicitante);
                critica.setComentario(edComentarioAvaliacao.getText().toString());
                critica.setNota((int) ratingBar.getRating());
                NotaMedia notaMedia = new NotaMedia();
                notaMedia.adicionarNota(servico.getIdPrestador(),critica.getNota());
                databaseReference.child("feedback").child("prestador").child(servico.getIdPrestador()).child(databaseReference.child("feedback").child("prestador").child(servico.getIdPrestador()).push().getKey()).setValue(critica);
                enviarNotificacao(3,servico.getIdPrestador());
                if(conclusao) {
                    atualizarEstadoServico(servico.getEstado(), EstadoServico.CONCLUIDA.getValue());
                }
                dialog.dismiss();
            }
        });


        mBuilder.setView(v1);
        dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (estadoId.equals(EstadoServico.ABERTA.getValue()) || estadoId.equals(EstadoServico.NEGOCIACAO.getValue())){
            getMenuInflater().inflate(R.menu.opcoes_servico_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.excluirServicoBtn) {
            excluirServico();
        } else {
            Intent intent = new Intent(this,EditarServicoActivity.class);
            intent.putExtra("servicoID",servicoId);
            intent.putExtra("estado",estadoId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void excluirServico(){
        databaseReference.child("servico").child(servicoId).removeValue();
        databaseReference.child("visualizacao").child(estadoId).child(servicoId).removeValue();
    }
}
