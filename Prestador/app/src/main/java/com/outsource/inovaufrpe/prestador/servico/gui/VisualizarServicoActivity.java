package com.outsource.inovaufrpe.prestador.servico.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.outsource.inovaufrpe.prestador.R;
import com.google.firebase.database.DatabaseReference;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;

public class VisualizarServicoActivity extends AppCompatActivity {

    AlertDialog dialog;
    View view;
    Button solicNovoOrca;
    TextView tituloID;
    TextView valorID;
    TextView descricaoID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        Intent intent = getIntent();
        String servicoId = intent.getStringExtra("servicoID");
        setTitle("Visualizar serviço");
        view = findViewById(R.id.ly);

        tituloID = findViewById(R.id.tvNomeServico);
        valorID = findViewById(R.id.tvPrecoServico);
        descricaoID = findViewById(R.id.tvDescricaoServico);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("servico").child("aberto").child(servicoId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Servico servico = dataSnapshot.getValue(Servico.class);
                tituloID.setText(servico.getNome());
                valorID.setText(servico.getPreco());
                descricaoID.setText(servico.getDescricao());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        /* Escolher qual layout vai ser invisivel */
        findViewById(R.id.layoutPessoa).setVisibility(View.GONE);

        //Remover se quiser testar botão p solicitar orçamento
        findViewById(R.id.btnSolicitarOrcamento).setVisibility(View.GONE);

        findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
        /* ****************/

//        Trabalhar com esse botão que depois eu termino de implementar o swipe button
        Button btnSolict = findViewById(R.id.btnSolicitarOrcamento);
        btnSolict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                criarDialogPersonalizado();

                solicNovoOrca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        solicitarNovoOrçamento();
                    }
                });
            }
        });

        /** ESCONDER LAYOUTS

Visao prestador
        if (visualizar p negociar serviço - visao prestador) {
                TextView tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);
                tituloLayoutPessoa.setText("Solicitado por");
                findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
                findViewById(R.id.layoutBtnConcluir).setVisibility(View.GONE);
        }
        if (concluir servico - visao prestador) {
            TextView tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);
            tituloLayoutPessoa.setText("Solicitado por");
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnNegociar).setVisibility(View.GONE);
        }

Visao do solicitante
        if (visualizar servico - visao solicitante) {
            findViewById(R.id.layoutPessoa).setVisibility(View.GONE);
            findViewById(R.id.btnSolicitarNovoOrcamento).setVisibility(View.GONE);
            findViewById(R.id.layoutBotoesBottom).setVisibility(View.GONE);
        }
        if (concluir servico - visao solicitante) {
            TextView tituloLayoutPessoa = findViewById(R.id.tvTituloLayout);
            tituloLayoutPessoa.setText("Executado por");
            findViewById(R.id.layoutNegociacoes).setVisibility(View.GONE);
            findViewById(R.id.layoutBtnNegociar).setVisibility(View.GONE);
        }

        **/
    }

    private void solicitarNovoOrçamento() {
        this.dialog.hide();
        Snackbar.make(view, "Achei isso melhor que um toast, e ainda pode ter ação de desfazer, 'ok entendi', entre outras.....", Snackbar.LENGTH_LONG).show();
    }

    private void criarDialogPersonalizado() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(VisualizarServicoActivity.this);
        View view1 = getLayoutInflater().inflate(R.layout.dialog_negociacao_servico, null);

        EditText precoServico = view1.findViewById(R.id.etPrecoServico);
        EditText comentarios = view1.findViewById(R.id.etComentarios);
        Button cancelarNegociacao = view1.findViewById(R.id.btnCancelarNegociacao);
        solicNovoOrca = view1.findViewById(R.id.btnSolicitarNovoOrcamento);
        mBuilder.setView(view1);
        dialog = mBuilder.create();
        dialog.show();

    }
}
