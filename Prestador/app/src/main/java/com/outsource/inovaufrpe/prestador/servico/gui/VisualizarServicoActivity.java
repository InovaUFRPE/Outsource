package com.outsource.inovaufrpe.prestador.servico.gui;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.google.firebase.database.DatabaseReference;
import com.outsource.inovaufrpe.prestador.servico.dominio.EstadoServico;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.utils.FirebaseUtil;

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
    Button btConcluir;
    String servicoId;
    String estadoId;
    //Remover gambiarra dps
    String estadoTemp;
    FirebaseAuth firebaseAuth;
    Servico servico;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        Intent intent = getIntent();
        servicoId = intent.getStringExtra("servicoID");
        estadoId = intent.getStringExtra("estado");
        if(estadoId.equals(EstadoServico.NEGOCIACAO.getValue())){
            estadoTemp = EstadoServico.ANDAMENTO.getValue();
        }else{
            estadoTemp = estadoId;
        }

        setTitle("Visualizar serviço");
        view = findViewById(R.id.ly);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

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


        tituloLayoutPessoa.setText("Solicitado por");

        dadosServico();

        btNegociar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarDialogPersonalizado();

                precoServico.setText(servico.getOferta());

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(precoServico.getText().toString().equals(servico.getOferta())){
                            if(!servico.getOfertante().equals(firebaseAuth.getCurrentUser().getUid())){
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.ANDAMENTO);
                            }else{
                            encerraDialog();
                        }
                        }else{
                            try {
                                negociar();
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO);
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
                atualizarEstadoServico(servico.getEstado(), EstadoServico.CONCLUIDA);
            }
        });

        //BOTAO QUE VAI VIRAR SWIPE
        Button btnSolict = findViewById(R.id.btnSolicitarOrcamento);
        btnSolict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                criarDialogPersonalizado();

                precoServico.setText(servico.getPreco());

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(precoServico.getText().toString().equals(servico.getPreco())){
                            descontar();
                            atualizarEstadoServico(servico.getEstado(), EstadoServico.ANDAMENTO);
                        }else{
                            try {
                                negociar();
                                atualizarEstadoServico(servico.getEstado(), EstadoServico.NEGOCIACAO);
                            }catch (DatabaseException e){
                                Toast.makeText(VisualizarServicoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
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
        tvOferta.setText(servico.getOferta());
    }

    private void negociar() throws DatabaseException {
        servico.setOferta(precoServico.getText().toString());
        databaseReference.child("servico").child(estadoTemp).child(servicoId).child("oferta").setValue(servico.getOferta()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    throw new DatabaseException(task.getException().getMessage());
                }
            }
        });
        databaseReference.child("servico").child(estadoTemp).child(servicoId).child("ofertante").setValue(firebaseAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
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
                valorID.setText(servico.getPreco());
                descricaoID.setText(servico.getDescricao());
                dadosUsuario();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void dadosUsuario(){

        databaseReference.child("usuario").child(servico.getIdCriador()).addListenerForSingleValueEvent(new ValueEventListener() {
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

    //TIRAR GAMBIARRA DESSE METODO
    private void atualizarEstadoServico(String estadoAnterior,EstadoServico estadoDestino) {
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
        if(estadoDestino.equals(EstadoServico.NEGOCIACAO)){
            filhoDestino = EstadoServico.ANDAMENTO.getValue();
        }else if(estadoDestino.equals(EstadoServico.NEGOCIACAO)){
            databaseReference.child("servico").child(filhoAnterior).child(servicoId).child("estado").setValue(EstadoServico.ANDAMENTO.getValue());
        }
        try {

            if(estadoAnterior.equals(EstadoServico.ABERTA.getValue())){
                databaseReference.child("servico").child(filhoAnterior).child(servicoId).child("idPrestador").setValue(firebaseAuth.getCurrentUser().getUid());
            }
            if (!estadoAnterior.equals(estadoDestino.getValue())){
                fu.moverServico(databaseReference.child("servico").child(filhoAnterior).child(servicoId), databaseReference.child("servico").child(filhoDestino).child(servicoId), estadoDestino);
            }

        }catch (DatabaseException e) {
            Toast.makeText(VisualizarServicoActivity.this, "Falha na solicitacao" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Snackbar.make(view, "Serviço em processo de "+ estadoDestino.getValue(), Snackbar.LENGTH_LONG).show();
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
}
