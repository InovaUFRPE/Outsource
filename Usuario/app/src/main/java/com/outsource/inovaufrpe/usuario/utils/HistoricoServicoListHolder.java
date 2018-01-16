package com.outsource.inovaufrpe.usuario.utils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.usuario.R;

public class HistoricoServicoListHolder extends RecyclerView.ViewHolder {

    public TextView data;
    public TextView nomeServico;
    public TextView preco;

    public HistoricoServicoListHolder(final View itemView) {
        super(itemView);
        data = itemView.findViewById(R.id.tvDataServico);
        nomeServico = itemView.findViewById(R.id.tvNomeServico);
        preco = itemView.findViewById(R.id.tvPrecoServico);
    }
}
