package com.outsource.inovaufrpe.usuario.servico.gui;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.carteira.dominio.God;
import com.outsource.inovaufrpe.usuario.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Comentario;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Critica;
import com.outsource.inovaufrpe.usuario.utils.CardFormat;
import com.outsource.inovaufrpe.usuario.utils.CriticaViewHolder;
import com.outsource.inovaufrpe.usuario.utils.FirebaseAux;
import com.outsource.inovaufrpe.usuario.utils.FirebaseUtil;
import com.outsource.inovaufrpe.usuario.utils.NotaMedia;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

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
    EditText comentario;
    Button cancelarNegociacao;
    Button btNegociar;
    Button btAceitarOferta;
    Button btConcluir;
    String servicoId;
    String estadoId;
    TextView nomeUsuario;
    RatingBar avaliarPerfil;
    FirebaseAuth firebaseAuth;
    Servico servico;
    DatabaseReference databaseReferenceServico;
    ValueEventListener listenerServico;
    String nomeSolicitante;
    String nomeServico;
    CardFormat cardFormat = new CardFormat();
    int peso;
    int somatorio;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        setTitle(getString(R.string.visualizar_servico));
        view = findViewById(R.id.ly);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        estadoId = intent.getStringExtra("estado");
        nomeServico = intent.getStringExtra("nomeServico");
        tituloID = findViewById(R.id.tvNomeServico);
        valorID = findViewById(R.id.tvPrecoServico);
        descricaoID = findViewById(R.id.tvDescricaoServico);
        tvNomePessoa = findViewById(R.id.tvNomePessoa);
        tvNotaPessoa = findViewById(R.id.tvNotaPessoa);
        tvEstadoServicoID = findViewById(R.id.tvEstadoServicoID);
        databaseReferenceServico = FirebaseDatabase.getInstance().getReference().child("servico");
        tvNomeOfertante = findViewById(R.id.tvNomeOfertante);
        tvOferta = findViewById(R.id.tvPrecoOrcamento);
        LinearLayout prestadorLayout = findViewById(R.id.layoutPessoa);
        TextView tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);
        LinearLayout negociacaoLayout = findViewById(R.id.layoutNegociacoes);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(nomeServico);

        firebaseAuth = FirebaseAuth.getInstance();

        btNegociar = findViewById(R.id.btnNegociar);
        btConcluir = findViewById(R.id.btnConcluirServico);
        btAceitarOferta = findViewById(R.id.btAceitarNegociacao);

        btNegociar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogNegociacao();

                final DecimalFormat df = new DecimalFormat("####0.00");
                precoServico.setText(df.format(Float.parseFloat(servico.getOferta().toString())).replace(".", ","));

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adicionarComentario(comentario.getText().toString(), Double.valueOf(precoServico.getText().toString().trim().replace(",", ".")));
                        if (df.format(Float.parseFloat(precoServico.getText().toString().replace(",", "."))).equals(servico.getOferta())) {
                            encerraDialog();
                        } else {
                            try {
                                negociar();
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO.getValue());
                            } catch (Exception e) {
                                Toast.makeText(VisualizarServicoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        btAceitarOferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())) {
                    databaseReferenceServico.child(estadoId).child(servicoId).child("preco").setValue(servico.getOferta()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                descontar();
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

        btConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concluir();
            }
        });

        prestadorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogVisualizarPerfil();
            }
        });

        negociacaoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisualizarServicoActivity.this, NegociacoesActivity.class);
                intent.putExtra("servicoID", servicoId);
                intent.putExtra("myUserID",  servico.getIdCriador());
                intent.putExtra("nomePrestador",  tvNomePessoa.getText().toString());
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

        if (estadoId.equals(EstadoServico.ABERTA.getValue())) {
            tvEstadoServicoID.setText(R.string.aberta);
            prestadorLayout.setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
        } else if (estadoId.equals(EstadoServico.NEGOCIACAO.getValue())) {
            tvEstadoServicoID.setText(R.string.em_negociacao);
            prestadorLayout.setVisibility(View.GONE);
            tituloLayoutPessoa.setText(R.string.executado_por);
            findViewById(R.id.layoutBtnConcluir).setVisibility(View.GONE);
        } else if (estadoId.equals(EstadoServico.ANDAMENTO.getValue())) {
            tvEstadoServicoID.setText(R.string.em_andamento);
            tituloLayoutPessoa.setText(R.string.executado_por);
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnNegociar).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnAceitarOferta).setVisibility(View.GONE);

        } else {
            tituloLayoutPessoa.setText(R.string.executado_por);
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
        }

    }

    private void concluir() {
        //TODO: CORRIGIR A AVALIAÇÃO DE USUARIO TARDIA
        databaseReferenceServico.child(estadoId).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("concluido")) {
                    if (dataSnapshot.child("concluido").getValue().toString().equals(firebaseAuth.getCurrentUser().getUid())) {
                        Toast.makeText(VisualizarServicoActivity.this, "Você já marcou este serviço como concluido", Toast.LENGTH_SHORT).show();
                    } else {
                        criarDialogAvaliarUsuario();
                        atualizarEstadoServico(servico.getEstado(), EstadoServico.CONCLUIDA.getValue());
                        aplicarTaxa();
                        databaseReferenceServico.child(estadoId).child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                    }
                } else {
                    criarDialogAvaliarUsuario();
                    databaseReferenceServico.child(estadoId).child(servicoId).child("dataf").setValue(new Timestamp(new Date().getTime()).toString());
                    databaseReferenceServico.child(estadoId).child(servicoId).child("concluido").setValue(firebaseAuth.getCurrentUser().getUid());
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

    @Override
    protected void onPause() {
        super.onPause();
        databaseReferenceServico.child(estadoId).child(servicoId).removeEventListener(listenerServico);
    }

    private void encerraDialog() {
        this.dialog.dismiss();
    }

    private void descontar() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                God carteira = new God(dataSnapshot.child("usuario").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").getValue(Double.class));
                carteira.subtrair(dataSnapshot.child("servico").child("negociacao").child(servicoId).child("oferta").getValue(Double.class));
                databaseReference.child("usuario").child(firebaseAuth.getCurrentUser().getUid()).child("carteira").setValue(carteira.getMoeda());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void aplicarTaxa(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                God.setFundos(dataSnapshot.child("servico").child("andamento").child(servicoId).child("oferta").getValue(Double.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void negociar() {
        servico.setOferta(Double.valueOf(precoServico.getText().toString().replace(",",".")));
        databaseReferenceServico.child(estadoId).child(servicoId).child("oferta").setValue(servico.getOferta()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });
        databaseReferenceServico.child(estadoId).child(servicoId).child("ofertante").setValue(firebaseAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    throw new DatabaseException(task.getException().getMessage());
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
                    tituloID.setText(servico.getNome());
                    valorID.setText(cardFormat.dinheiroFormat(servico.getPreco().toString()));
                    tvEstadoServicoID.setText(servico.getEstado());
                    descricaoID.setText(servico.getDescricao());
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
        databaseReferenceServico.child(estadoId).child(servicoId).addValueEventListener(listenerServico);
    }

    private void dadosUsuario() {
        DatabaseReference databaseReferenceSolicitante = FirebaseDatabase.getInstance().getReference().child("prestador");
        databaseReferenceSolicitante.child(servico.getIdPrestador()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                if (servico.getOfertante() != null) {
                    dadosNegociacao();
                }
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
            tvNomeOfertante.setText(tvNomePessoa.getText());
        }
        tvOferta.setText(cardFormat.dinheiroFormat(servico.getOferta().toString()));
    }

    private void atualizarEstadoServico(String estadoAtual, String estadoDestino) {
        if (this.dialog != null) {
            this.dialog.dismiss();
        }
        FirebaseUtil fu = new FirebaseUtil();
        try {
            if (!estadoAtual.equals(estadoDestino)) {
                fu.moverServico(databaseReferenceServico.child(estadoAtual).child(servicoId), databaseReferenceServico.child(estadoDestino).child(servicoId), estadoDestino);
            }

        } catch (DatabaseException e) {
            Toast.makeText(VisualizarServicoActivity.this, "Falha na solicitacao" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Snackbar.make(view, "Serviço em processo de " + estadoDestino, Snackbar.LENGTH_LONG).show();
    }

    private void criarDialogNegociacao() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        @SuppressLint("InflateParams") View view1 = getLayoutInflater().inflate(R.layout.dialog_negociacao_servico, null);

        precoServico = view1.findViewById(R.id.etPrecoServico);
        comentario = view1.findViewById(R.id.etComentarios);
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
        @SuppressLint("InflateParams") View v1 = getLayoutInflater().inflate(R.layout.dialog_visualizar_perfil, null);
        FirebaseRecyclerAdapter adapter;

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
                intent.putExtra("prestadorNome", tvNomePessoa.getText().toString());
                intent.putExtra("prestadorID",servico.getIdPrestador());
                startActivity(intent);
            }
        });

        nomeUsuario = v1.findViewById(R.id.tvNomePerfil);
        avaliarPerfil = v1.findViewById(R.id.rbAvaliarServico);
        nomeUsuario.setText(tvNomePessoa.getText().toString());
        if (peso == 0){
            avaliarPerfil.setRating(0);
        }else {
            avaliarPerfil.setRating(Float.parseFloat(tvNotaPessoa.getText().toString()));
        }
        RecyclerView mRecyclerView = v1.findViewById(R.id.RecycleComentarioID);

        mRecyclerView.setFocusable(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(VisualizarServicoActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("feedback").child(servico.getIdPrestador()).orderByChild("data");

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
        final EditText edComentarioAvaliacao = v1.findViewById(R.id.etComentarios);
        final RatingBar ratingBar = v1.findViewById(R.id.rbAvaliarServico);
        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Critica critica = new Critica();
                critica.setComentadorNome(nomeSolicitante);
                critica.setComentario(edComentarioAvaliacao.getText().toString());
                critica.setNota((int) ratingBar.getRating());
                DatabaseReference databaseReference = FirebaseAux.getInstancia().getDatabaseReference();
                NotaMedia notaMedia = new NotaMedia();
                notaMedia.adicionarNota(servico.getIdPrestador(),critica.getNota());
                databaseReference.child("feedback").child(servico.getIdPrestador()).child(databaseReference.child("feedback").child(servico.getIdPrestador()).push().getKey()).setValue(critica);
                dialog.dismiss();
            }
        });


        mBuilder.setView(v1);
        dialog = mBuilder.create();
        dialog.show();
    }

    private void adicionarComentario(String texto, Double valor) {
        Comentario comentario = new Comentario();
        Date data = new Date();
        String novaData = new Timestamp(data.getTime()).toString();
        comentario.setTempo(data.getTime());
        comentario.setTexto(texto);
        comentario.setAutor(firebaseAuth.getCurrentUser().getUid());
        comentario.setNomeAutor(firebaseAuth.getCurrentUser().getDisplayName());
        comentario.setvalor(valor);
        novaData = novaData.replace(".", "");
        DatabaseReference databaseReferenceComentario = FirebaseDatabase.getInstance().getReference().child("comentario");
        databaseReferenceComentario.child(servicoId).child(novaData).setValue(comentario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(!task.isSuccessful()){
                    Toast.makeText(VisualizarServicoActivity.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        databaseReferenceServico.child(estadoId).child(servicoId).removeValue();
        FirebaseAux.getInstancia().getDatabaseReference().child("usuario").child(FirebaseAux.getInstancia().getFirebaseAuth().getCurrentUser().getUid())
                .child("servicos").child(servicoId).removeValue();
    }
}
