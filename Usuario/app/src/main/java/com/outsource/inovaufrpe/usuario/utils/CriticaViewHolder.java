package com.outsource.inovaufrpe.usuario.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.usuario.R;


public class CriticaViewHolder extends RecyclerView.ViewHolder {
    public View mainLayout;
    public View linearLayout;
    public TextView tvComentador;
    public TextView tvNota;
    public TextView tvComentario;

    public CriticaViewHolder(View itemView) {
        super(itemView);
        mainLayout = itemView.findViewById(R.id.card_critica);
        linearLayout = itemView.findViewById(R.id.critica_card);
        tvComentador = itemView.findViewById(R.id.tvComentadorID);
        tvNota = itemView.findViewById(R.id.tvNotaID);
        tvComentario = itemView.findViewById(R.id.tvComentarioID);
    }
}
