package com.outsource.inovaufrpe.prestador.servico.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.google.firebase.database.DatabaseReference;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Critica;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.CriticaViewHolder;
import com.outsource.inovaufrpe.prestador.utils.FirebaseUtil;

import java.text.DecimalFormat;

public class VisualizarServicoActivity extends AppCompatActivity {

    AlertDialog dialog;
    View view;
    TextView tituloID;
    TextView valorID;
    TextView descricaoID;
    TextView tituloLayoutPessoa;
    TextView tvNomeSolicitante;
    TextView tvNotaSolicitante;
    TextView tvNomeOfertante;
    TextView tvOferta;
    TextView tvEstadoServicoID;
    TextView tvTituloNegociacao;
    Button solicNovoOrca;
    EditText precoServico;
    Button cancelarNegociacao;
    Button btNegociar;
    Button btAceitarOferta;
    Button btConcluir;
    String servicoId;
    String estadoId;
    FirebaseAuth firebaseAuth;
    Servico servico;
    DatabaseReference databaseReferenceServico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        estadoId = intent.getStringExtra("estado");
        setTitle("Visualizar serviço");
        view = findViewById(R.id.ly);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceServico = FirebaseDatabase.getInstance().getReference().child("servico");

        tituloID = findViewById(R.id.tvNomeServico);
        valorID = findViewById(R.id.tvPrecoServico);
        descricaoID = findViewById(R.id.tvDescricaoServico);
        tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);
        tvNomeSolicitante = findViewById(R.id.tvNomePessoa);
        tvNotaSolicitante = findViewById(R.id.tvNotaPessoa);
        tvNomeOfertante = findViewById(R.id.tvNomePrestador);
        tvOferta = findViewById(R.id.tvPrecoOrcamento);
        tvEstadoServicoID = findViewById(R.id.tvEstadoServicoID);
        tvTituloNegociacao = findViewById(R.id.tvTituloNegociacaoID);
        btNegociar = findViewById(R.id.btnNegociar);
        btConcluir = findViewById(R.id.btnConcluirServico);
        btAceitarOferta = findViewById(R.id.btAceitarNegociacao);
        LinearLayout solicitanteLayout = findViewById(R.id.layoutPessoa);

        tituloLayoutPessoa.setText("Solicitado por");

        dadosServico();

        btNegociar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogPersonalizado();
                DecimalFormat df = new DecimalFormat("####0.00");
                precoServico.setText(df.format(Float.parseFloat(servico.getOferta().toString())).replace(".",","));

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(precoServico.getText().toString().equals(servico.getOferta())){
                            if(!servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())){
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.ANDAMENTO.getValue());
                            }else{
                            encerraDialog();
                        }
                        }else{
                            try {
                                negociar();
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO.getValue());
                            }catch (DatabaseException e){
                                Toast.makeText(VisualizarServicoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        btConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                concluir();
                atualizarEstadoServico(servico.getEstado(), EstadoServico.CONCLUIDA.getValue());
            }
        });

        //BOTAO QUE VAI VIRAR SWIPE
        Button btnSolict = findViewById(R.id.btnSolicitarOrcamento);
        btnSolict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                criarDialogPersonalizado();
                DecimalFormat df = new DecimalFormat("####0.00");
                precoServico.setText(df.format(Float.parseFloat(servico.getPreco().toString())).replace(".",","));

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(precoServico.getText().toString().equals(servico.getPreco())){
                            descontar();
                            databaseReferenceServico.child(servico.getEstado()).child("preco").setValue(servico.getOferta());
                            atualizarEstadoServico(servico.getEstado(), EstadoServico.ANDAMENTO.getValue());
                        }else{
                            try {
                                negociar();
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO.getValue());
                            }catch (DatabaseException e){
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
                    databaseReferenceServico.child(servico.getEstado()).child("preco").setValue(servico.getOferta());
                    atualizarEstadoServico(servico.getEstado(), EstadoServico.ANDAMENTO.getValue());
                }else{
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


        if (estadoId.equals(EstadoServico.ABERTA.getValue())) {
                tvEstadoServicoID.setText("Aberta");
                findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
                findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
                findViewById(R.id.layoutBtnConcluir).setVisibility(View.GONE);
        }
        else if (estadoId.equals(EstadoServico.NEGOCIACAO.getValue())) {
            tvEstadoServicoID.setText("Em negociacao");
            tvTituloNegociacao.setText("Ultima Oferta:");
            findViewById(R.id.layoutBtnConcluir).setVisibility(View.GONE);
            findViewById(R.id.btnSolicitarOrcamento).setVisibility(View.GONE);

        }else if(estadoId.equals(EstadoServico.ANDAMENTO.getValue())){
            tvEstadoServicoID.setText("Em andamento");
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnNegociar).setVisibility(View.GONE);
            findViewById(R.id.btnSolicitarOrcamento).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnAceitarOferta).setVisibility(View.GONE);

        }else{
            tvEstadoServicoID.setText("Finalizada");
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
            findViewById(R.id.btnSolicitarOrcamento).setVisibility(View.GONE);

        }

    }

    private void concluir() {

    }

    private void encerraDialog() {
        this.dialog.dismiss();
    }

    private void descontar(){
    //AQUIHEITOR
    }

    private void dadosNegociacao() {

        if(servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())){
            tvNomeOfertante.setText(firebaseAuth.getCurrentUser().getDisplayName());
        }else{
            tvNomeOfertante.setText(tvNomeSolicitante.getText());
        }
        DecimalFormat df = new DecimalFormat("####0.00");
        tvOferta.setText("R$ "+ df.format(Float.parseFloat(servico.getOferta().toString())).replace(".",","));
    }

    private void negociar() throws DatabaseException {
        servico.setOferta(Double.valueOf(precoServico.getText().toString().replace(",",".")));
        databaseReferenceServico.child(estadoId).child(servicoId).child("oferta").setValue(servico.getOferta()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });
        databaseReferenceServico.child(estadoId).child(servicoId).child("ofertante").setValue(firebaseAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });

    }

    private void dadosServico() {
        databaseReferenceServico.child(estadoId).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servico = dataSnapshot.getValue(Servico.class);
                if (servico != null) {
                    tituloID.setText(servico.getNome());
                    DecimalFormat df = new DecimalFormat("####0.00");
                    valorID.setText("R$ " + df.format(Float.parseFloat(servico.getPreco().toString())).replace(".", ","));
                    descricaoID.setText(servico.getDescricao());
                    dadosUsuario();
                }else{
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void dadosUsuario(){
        DatabaseReference databaseReferenceUsuario = FirebaseDatabase.getInstance().getReference().child("usuario");
        databaseReferenceUsuario.child(servico.getIdCriador()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvNomeSolicitante.setText(dataSnapshot.child("nome").getValue(String.class));
                tvNotaSolicitante.setText(String.valueOf(dataSnapshot.child("nota").getValue(Integer.class)));
                if(servico.getOfertante() != null){
                    dadosNegociacao();
                }
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
            if(estadoAtual.equals(EstadoServico.ABERTA.getValue())){
                databaseReferenceServico.child(estadoAtual).child(servicoId).child("idPrestador").setValue(firebaseAuth.getCurrentUser().getUid());
            }
            if (!estadoAtual.equals(estadoDestino)) {
                fu.moverServico(databaseReferenceServico.child(estadoAtual).child(servicoId), databaseReferenceServico.child(estadoDestino).child(servicoId), estadoDestino);
            }

        } catch (DatabaseException e) {
            Toast.makeText(VisualizarServicoActivity.this, "Falha na solicitacao" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Snackbar.make(view, "Serviço em processo de " + estadoDestino, Snackbar.LENGTH_LONG).show();
    }

    private void criarDialogPersonalizado() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_negociacao_servico, null);

        precoServico = view1.findViewById(R.id.etPrecoServico);
        EditText comentarios = view1.findViewById(R.id.etComentarios);
        cancelarNegociacao = view1.findViewById(R.id.btnCancelarNegociacao);
        solicNovoOrca = view1.findViewById(R.id.btnSolicitarNovoOrcamento);
        mBuilder.setView(view1);
        dialog = mBuilder.create();
        dialog.show();

    }

    /**
     * Método para chamar dialog de perfil
     */
    private void criarDialogVisualizarPerfil() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        View v1 = getLayoutInflater().inflate(R.layout.dialog_visualizar_perfil,null);
        FirebaseRecyclerAdapter adapter;

        TextView nomeUsuario = v1.findViewById(R.id.tvNomePerfil);
        RecyclerView mRecyclerView = (RecyclerView) v1.findViewById(R.id.RecycleComentarioID);
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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("feedback").child(servico.getIdCriador()).orderByChild("data");

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
        View v1 = getLayoutInflater().inflate(R.layout.dialog_avaliacao_usuario,null);

        Button btnConcluir = v1.findViewById(R.id.btnAvaliarConcluir);
        final EditText edComentarioAvaliacao = v1.findViewById(R.id.etComentarios);
        final RatingBar ratingBar = v1.findViewById(R.id.rbAvaliarServico);
        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get comentario: edComentarioAvaliacao.getText() retorna string;
                //get quantidade estrelas: ratingBar.getRating() retorna float;
            }
        });

        mBuilder.setView(v1);
        dialog = mBuilder.create();
        dialog.show();

    }
}
