package com.outsource.inovaufrpe.prestador.servico.gui;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.carteira.dominio.God;
import com.outsource.inovaufrpe.prestador.conversa.gui.MensagemActivity;
import com.outsource.inovaufrpe.prestador.notificacao.dominio.Notificacao;
import com.outsource.inovaufrpe.prestador.prestador.adapter.CriticaViewHolder;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Critica;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.prestador.servico.dominio.Oferta;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;
import com.outsource.inovaufrpe.prestador.utils.FirebaseAux;
import com.outsource.inovaufrpe.prestador.utils.FirebaseUtil;
import com.outsource.inovaufrpe.prestador.utils.NotaMedia;
import com.outsource.inovaufrpe.prestador.utils.Utils;
import com.outsource.inovaufrpe.prestador.utils.swipebutton.SwipeButton;
import com.outsource.inovaufrpe.prestador.utils.swipebutton.interfaces.OnActiveListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

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
    TextView tvValorOfertaID;
    TextView tvTempoOfertaID;
    Button solicNovoOrca;
    EditText precoServico;
    EditText mensagem;
    Button cancelarNegociacao;
    Button btNegociar;
    SwipeButton swipeConcluir;
    String servicoId;
    String estadoId;
    FirebaseAuth firebaseAuth;
    Servico servico;
    DatabaseReference databaseReference;
    ValueEventListener listenerServico;
    String nomeSolicitante;
    String nomeServico;
    int peso;
    int somatorio;
    String uriFotoUsuario;
    private Task<Void> oferta;


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
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
        swipeConcluir = findViewById(R.id.btnConcluirServico);
        tvValorOfertaID = findViewById(R.id.tvValorOfertaID);
        tvTempoOfertaID = findViewById(R.id.tvTempoOfertaID);
        LinearLayout solicitanteLayout = findViewById(R.id.layoutPessoa);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeServico);

        findViewById(R.id.layoutOfertaID).setVisibility(View.GONE);
        tituloLayoutPessoa.setText(R.string.solicitado_por);

        dadosServico();


        btNegociar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogNegociacao();
                final DecimalFormat df = new DecimalFormat("####0.00");
                precoServico.setText(servico.getPreco().toString());
                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Oferta oferta1 = new Oferta();
                        oferta1.setPrestadorNome(firebaseAuth.getCurrentUser().getDisplayName());
                        oferta1.setPrestadorId(firebaseAuth.getCurrentUser().getUid());
                        oferta1.setOfertaValor(Double.parseDouble(precoServico.getText().toString()));
                        oferta1.setTempo(new Date().getTime());
                        databaseReference.child("oferta").child(servicoId).child(oferta1.getPrestadorId()).setValue(oferta1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(VisualizarServicoActivity.this, "Orçamento enviado com sucesso!", Toast.LENGTH_SHORT).show();
                                checaOferta();
                            }
                        });
                        enviarNotificacao(0);
                        dialog.dismiss();
                    }
                });
            }
        });

        swipeConcluir.setOnActiveListener(new OnActiveListener() {
            @Override
            public void onActive() {
                concluir();
            }
        });

        solicitanteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogVisualizarPerfil();
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

    private void concluir() {
        databaseReference.child("servico").child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("concluido")) {
                    if (dataSnapshot.child("concluido").getValue().equals(firebaseAuth.getCurrentUser().getUid())) {
                        Utils.criarToast(VisualizarServicoActivity.this, "Você já marcou este serviço como concluido");
                    } else {
                        aplicarTaxa();
                        databaseReference.child("servico").child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                        databaseReference.child("visualizacao").child(estadoId).child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                        criarDialogAvaliarUsuario(true);
                    }
                } else {
                    aplicarTaxa();
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

    @Override
    protected void onResume() {
        super.onResume();
        dadosServico();

    }

    private void definirLayout(){
        if (estadoId.equals(EstadoServico.ABERTA.getValue())) {
            tvEstadoServicoID.setText(R.string.aberta);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutBtnNegociar).setVisibility(View.VISIBLE);
            findViewById(R.id.btnNegociar).setVisibility(View.VISIBLE);
            checaOferta();
        } else if (estadoId.equals(EstadoServico.ANDAMENTO.getValue())) {
            tvEstadoServicoID.setText(R.string.em_andamento);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.VISIBLE);
            findViewById(R.id.layoutBtnConcluir).setVisibility(View.VISIBLE);
            findViewById(R.id.btnConcluirServico).setVisibility(View.VISIBLE);
        } else {
            tvEstadoServicoID.setText(R.string.concluido);
        }
    }

    private void checaOferta() {
        databaseReference.child("oferta").child(servicoId).child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("ofertaValor")) {
                    findViewById(R.id.layoutOfertaID).setVisibility(View.VISIBLE);
                    tvValorOfertaID.setText(CardFormat.dinheiroFormat(String.valueOf(dataSnapshot.child("ofertaValor").getValue(long.class))));
                    tvTempoOfertaID.setText(CardFormat.tempoFormat(dataSnapshot.child("tempo").getValue(long.class)));
                }else{
                    databaseReference.child("oferta").child(servicoId).child(firebaseAuth.getCurrentUser().getUid()).removeEventListener(this);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void aplicarTaxa() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                God carteira = new God(dataSnapshot.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").getValue(Double.class));
                carteira.adicionar(dataSnapshot.child("visualizacao").child("andamento").child(servicoId).child("preco").getValue(Double.class));
                God.setFundos(dataSnapshot.child("visualizacao").child("andamento").child(servicoId).child("preco").getValue(Double.class));
                databaseReference.child("prestador").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").setValue(carteira.getMoeda());
                if (dataSnapshot.child("fundos").child("valor").getValue(double.class) == null) {
                    databaseReference.child("fundos").child("valor").setValue(God.getFundos());
                } else {
                    double fundos = God.getFundos()+dataSnapshot.child("fundos").child("valor").getValue(Double.class);
                    databaseReference.child("fundos").child("valor").setValue(fundos);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dadosServico() {
        databaseReference.child("servico").child(servicoId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servico = dataSnapshot.getValue(Servico.class);
                if (servico != null) {
                    tituloID.setText(servico.getNome());
                    if (servico.getPreco() == 0) {
                        valorID.setText("A comb.");
                    } else {
                        valorID.setText(CardFormat.dinheiroFormat(String.valueOf(servico.getPreco())));
                    }
                    descricaoID.setText(servico.getDescricao());
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
        databaseReference.child("usuario").child(servico.getIdCriador()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.child("nome").getValue(String.class) + " " + dataSnapshot.child("sobrenome").getValue(String.class);
                tvNomePessoa.setText(s);
                somatorio = dataSnapshot.child("nota").getValue(int.class);
                peso = dataSnapshot.child("pesoNota").getValue(int.class);
                uriFotoUsuario = dataSnapshot.child("foto").getValue(String.class);
                if(uriFotoUsuario != null && !uriFotoUsuario.isEmpty()) {
                    Picasso.with(VisualizarServicoActivity.this).load(Uri.parse(uriFotoUsuario)).centerCrop().fit().into((CircleImageView) findViewById(R.id.rlPessoa).findViewById(R.id.ivFotoSolicitante));
                }
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
            if (!estadoAtual.equals(estadoDestino)) {
                databaseReference.child("visualizacao").child(estadoAtual).child(servicoId).child("estado").setValue(estadoDestino);
                fu.moverServico(databaseReference.child("visualizacao").child(estadoAtual).child(servicoId), databaseReference.child("visualizacao").child(estadoDestino).child(servicoId), estadoDestino);
                databaseReference.child("servico").child(servicoId).child("estado").setValue(estadoDestino);
            }

        } catch (DatabaseException e) {
            Utils.criarToast(VisualizarServicoActivity.this, "Falha na solicitação");
        }
        Snackbar.make(view, "Serviço em processo de " + estadoDestino, Snackbar.LENGTH_LONG).show();
    }

    private void enviarNotificacao(int tiponotificacao){
        long data = new Date().getTime();
        Notificacao notificacao = new Notificacao();
        notificacao.setNotificacaoID(databaseReference.child("notificacao").child("prestador").child(firebaseAuth.getCurrentUser().getUid()).push().getKey());
        notificacao.setServicoID(servico.getId());
        notificacao.setNomeServico(servico.getNome());
        notificacao.setEstado(servico.getEstado());
        notificacao.setTempo(data);
        notificacao.setOrdemRef(new Timestamp(data*-1).toString());
        switch (tiponotificacao){
            case 0: //Oferta realizada
                notificacao.setTextoNotificacao("Alguém ofertou o seu serviço!");
                break;
            case 1://Conclusão
                notificacao.setTextoNotificacao("O prestador marcou um serviço como Concluido!");
                break;
            case 2://
                break;
            case 3:
                break;
        }
        notificacao.setTipoNotificacao(tiponotificacao);

        databaseReference.child("notificacao").child("usuario").child(servico.getIdCriador()).child(notificacao.getNotificacaoID()).setValue(notificacao);

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
        final TextView nenhumaAvaliacao = v1.findViewById(R.id.nenhumaAvaliacaoTV);

        nomeUsuario.setText(tvNomePessoa.getText().toString());
        if(uriFotoUsuario != null && !uriFotoUsuario.isEmpty()) {
            Picasso.with(VisualizarServicoActivity.this).load(Uri.parse(uriFotoUsuario)).centerCrop().fit().into((CircleImageView) v1.findViewById(R.id.profile_image));
        }
        if (peso == 0){
            avaliarPerfil.setRating(0);
        }else{
            avaliarPerfil.setRating(Float.parseFloat(tvNotaPessoa.getText().toString().replace(",",".")));
        }

        final RecyclerView mRecyclerView = v1.findViewById(R.id.RecycleComentarioID);
        ImageButton closeBtn = v1.findViewById(R.id.closeBtn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        mRecyclerView.setFocusable(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisualizarServicoActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Query query = databaseReference.child("feedback").child("usuario").child(servico.getIdCriador()).orderByChild("data");

        adapter = new FirebaseRecyclerAdapter<Critica, CriticaViewHolder>(Critica.class, R.layout.item_avaliacao_dialog, CriticaViewHolder.class, query) {

            @Override
            protected void populateViewHolder(CriticaViewHolder viewHolder, Critica model, int position) {
                viewHolder.linearLayout.setVisibility(View.VISIBLE);
                viewHolder.tvComentador.setText(model.getComentadorNome());
                viewHolder.tvNota.setText(String.valueOf(model.getNota()));
                viewHolder.tvComentario.setText(model.getComentario());

                if (getItemCount() == 0) {
                    nenhumaAvaliacao.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }

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
                enviarNotificacao(1);
                if(conclusao){
                    databaseReference.child("conversaUsuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(servico.getId()+servico.getIdPrestador()).child("estadoServico").setValue("concluido");
                    databaseReference.child("conversaPrestador").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(servico.getId()).child("estadoServico").setValue("concluido");
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
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.chatSolicitanteButton){
            Intent intent = new Intent(VisualizarServicoActivity.this, MensagemActivity.class);
            intent.putExtra("servicoID", servicoId);
            intent.putExtra("prestadorID",  firebaseAuth.getCurrentUser().getUid());
            intent.putExtra("usuarioID", servico.getIdCriador());
            intent.putExtra("nomeServico",  servico.getNome());
            intent.putExtra("estado", servico.getEstado());
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }
}
