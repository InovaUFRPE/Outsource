package com.outsource.inovaufrpe.usuario.servico.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.carteira.dominio.God;
import com.outsource.inovaufrpe.usuario.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;
import com.outsource.inovaufrpe.usuario.solicitante.dominio.Usuario;
import com.outsource.inovaufrpe.usuario.utils.DinheiroFormat;
import com.outsource.inovaufrpe.usuario.utils.FirebaseUtil;

import java.text.DecimalFormat;

public class VisualizarServicoActivity extends AppCompatActivity {

    AlertDialog dialog;
    View view;
    TextView tituloID;
    TextView valorID;
    TextView descricaoID;
    TextView tituloLayoutPessoa;
    TextView tvNomePrestador;
    TextView tvNotaPrestador;
    TextView tvNomeOfertante;
    TextView tvEstadoServicoID;
    TextView tvOferta;
    Button solicNovoOrca;
    EditText precoServico;
    Button cancelarNegociacao;
    Button btNegociar;
    Button btConcluir;
    String servicoId;
    String estadoId;
    //Gambiarra
    String estadoTemp;
    FirebaseAuth firebaseAuth;
    Servico servico;
    DatabaseReference databaseReference;
    DinheiroFormat dinheiroFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        setTitle("Visualizar serviço");
        view = findViewById(R.id.ly);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        estadoId = intent.getStringExtra("estado");
        if (estadoId.equals(EstadoServico.NEGOCIACAO.getValue())) {
            estadoTemp = EstadoServico.ANDAMENTO.getValue();
        } else {
            estadoTemp = estadoId;
        }
        tituloID = findViewById(R.id.tvNomeServico);
        valorID = findViewById(R.id.tvPrecoServico);
        descricaoID = findViewById(R.id.tvDescricaoServico);
        firebaseAuth = FirebaseAuth.getInstance();
        tvNomePrestador = findViewById(R.id.tvNomePessoa);
        tvNotaPrestador = findViewById(R.id.tvNotaPessoa);
        tvEstadoServicoID = findViewById(R.id.tvEstadoServicoID);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        tvNomeOfertante = findViewById(R.id.tvNomePrestador);
        tvOferta = findViewById(R.id.tvPrecoOrcamento);
        tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);

        /* ****************/

        dadosServico();

        btNegociar = findViewById(R.id.btnNegociar);
        btConcluir = findViewById(R.id.btnConcluirServico);

        btNegociar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogPersonalizado();
                DecimalFormat df = new DecimalFormat("####0.00");
                precoServico.setText("R$ "+ df.format(Float.parseFloat(servico.getOferta().toString())).replace(".",","));

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (precoServico.getText().toString().equals(servico.getOferta().toString())) {
                            if(!servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())){
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.ANDAMENTO);
                            }else{
                                encerraDialog();
                            }
                        } else {
                            try {
                                negociar();
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO);
                            } catch (DatabaseException e) {
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
                transacao();
                atualizarEstadoServico(servico.getEstado(), EstadoServico.CONCLUIDA);
            }
        });

        if (estadoId.equals(EstadoServico.ABERTA.getValue())) {
            tvEstadoServicoID.setText("Aberta");
            findViewById(R.id.layoutPessoa).setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
        } else if (estadoId.equals(EstadoServico.NEGOCIACAO.getValue())) {
            tvEstadoServicoID.setText("Em negociacao");
            tituloLayoutPessoa.setText("Executado por");
            findViewById(R.id.layoutBtnConcluir).setVisibility(View.GONE);
        } else if (estadoId.equals(EstadoServico.ANDAMENTO.getValue())) {
            tvEstadoServicoID.setText("Em andamento");
            tituloLayoutPessoa.setText("Executado por");
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnNegociar).setVisibility(View.GONE);
        } else {
            tituloLayoutPessoa.setText("Executado por");
            findViewById(R.id.layoutPessoa).setVisibility(View.GONE);
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
        }

    }

    private void encerraDialog() {
        this.dialog.dismiss();
    }

    private void negociar() throws DatabaseException {
        servico.setOferta(Double.valueOf(precoServico.getText().toString()));
        databaseReference.child("servico").child(estadoTemp).child(servicoId).child("oferta").setValue(servico.getOferta()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });
        databaseReference.child("servico").child(estadoTemp).child(servicoId).child("ofertante").setValue(firebaseAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });

    }

    private void dadosServico() {
        databaseReference.child("servico").child(estadoTemp).child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servico = dataSnapshot.getValue(Servico.class);
                tituloID.setText(servico.getNome());
                DecimalFormat df = new DecimalFormat("####0.00");
                valorID.setText("R$ "+ df.format(Float.parseFloat(servico.getPreco().toString())).replace(".",","));
                descricaoID.setText(servico.getDescricao());
                dadosUsuario();
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
                tvNomePrestador.setText(dataSnapshot.child("nome").getValue(String.class));
                tvNotaPrestador.setText(String.valueOf(dataSnapshot.child("nota").getValue(Integer.class)));
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

        if (servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())) {
            tvNomeOfertante.setText(firebaseAuth.getCurrentUser().getDisplayName());
        } else {
            tvNomeOfertante.setText(tvNomePrestador.getText());
        }
        DecimalFormat df = new DecimalFormat("####0.00");
        tvOferta.setText("R$ "+ df.format(Float.parseFloat(servico.getOferta().toString())).replace(".",","));
    }

    //TIRAR GAMBIARRA DESSE METODO
    private void atualizarEstadoServico(String estadoAnterior, EstadoServico estadoDestino) {
        if(this.dialog != null) {
            this.dialog.dismiss();
        }
        FirebaseUtil fu = new FirebaseUtil();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String filhoDestino = estadoDestino.getValue();
        String filhoAnterior = estadoAnterior;
        if(estadoAnterior.equals(EstadoServico.NEGOCIACAO.getValue())){
            if(estadoDestino.equals(EstadoServico.ANDAMENTO)){
                databaseReference.child("servico").child(filhoDestino).child(servicoId).child("estado").setValue(EstadoServico.ANDAMENTO.getValue());
                return;
            }
            filhoAnterior = EstadoServico.ANDAMENTO.getValue();
        }
        if (estadoDestino.equals(EstadoServico.NEGOCIACAO)) {
            filhoDestino = EstadoServico.ANDAMENTO.getValue();
        }else if(estadoDestino.equals(EstadoServico.NEGOCIACAO)){
            databaseReference.child("servico").child(filhoAnterior).child(servicoId).child("estado").setValue(EstadoServico.ANDAMENTO.getValue());
        }

        try {
            if (!estadoAnterior.equals(estadoDestino.getValue())) {
                fu.moverServico(databaseReference.child("servico").child(filhoAnterior).child(servicoId), databaseReference.child("servico").child(filhoDestino).child(servicoId), estadoDestino);
            }

        } catch (DatabaseException e) {
            Toast.makeText(VisualizarServicoActivity.this, "Falha na solicitacao" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Snackbar.make(view, "Serviço em processo de " + estadoDestino.getValue(), Snackbar.LENGTH_LONG).show();
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

    private void transacao(){
        databaseReference.child("usuario").child(servico.getIdCriador()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                usuario.setCarteira(usuario.getCarteira()-servico.getOferta());
                databaseReference.child("usuario").child(servico.getIdCriador()).setValue(usuario);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("god").child("gods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                God god = dataSnapshot.getValue(God.class);
                god.setFundos(servico.getOferta());
                databaseReference.child("god").child("gods").setValue(god);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
