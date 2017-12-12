package com.outsource.inovaufrpe.prestador.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.prestador.R;

/**
 * Created by Pichau on 12/12/2017.
 */

public class CriticaViewHolder extends RecyclerView.ViewHolder {
    public View mainLayout;
    public View linearLayout;
    View mView;
    public TextView tvComentador;
    public TextView tvNota;
    public TextView tvComentario;

    public CriticaViewHolder(View itemView) {
        super(itemView);
        mView = itemView;

        mainLayout = mView.findViewById(R.id.card_critica);
        linearLayout = mView.findViewById(R.id.critica_card);
        tvComentador = mView.findViewById(R.id.tvComentadorID);
        tvNota = mView.findViewById(R.id.tvNotaID);
        tvComentario = mView.findViewById(R.id.tvComentarioID);

    }
}
