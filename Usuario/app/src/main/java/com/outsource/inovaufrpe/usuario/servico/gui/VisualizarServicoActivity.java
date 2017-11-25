package com.outsource.inovaufrpe.usuario.servico.gui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.outsource.inovaufrpe.usuario.R;

public class VisualizarServicoActivity extends AppCompatActivity {

    AlertDialog dialog;
    View view;
    Button solicNovoOrca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_servico);
        setTitle("Visualizar serviço");
        view = findViewById(R.id.ly);

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
