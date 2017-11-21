package com.outsource.inovaufrpe.prestador.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.dominio.Servico;

/**
 * Created by Pichau on 21/11/2017.
 */

public class ServicoListHolder extends RecyclerView.ViewHolder {
    public View mainLayout;
    public View linearLayout;
    View mView;
    Servico servico = new Servico();
    public TextView titulo;
    public TextView descricao;
    public TextView valor;
    public Button aceitar;
    public Button negociar;

    View view;

    public ServicoListHolder(final View itemView) {
        super(itemView);
        mView = itemView;

        titulo = (TextView) itemView.findViewById(R.id.tituloID);
        descricao = (TextView) itemView.findViewById(R.id.descricaoID);
        valor = (TextView) itemView.findViewById(R.id.valorID);
        aceitar = (Button) itemView.findViewById(R.id.aceitarID);
        negociar = (Button) itemView.findViewById(R.id.negociarID);
        mainLayout = itemView.findViewById(R.id.mainLayout);
        linearLayout = itemView.findViewById(R.id.row_profile_lists);





        view = itemView.findViewById(R.id.row_profile_lists);
    }
}
