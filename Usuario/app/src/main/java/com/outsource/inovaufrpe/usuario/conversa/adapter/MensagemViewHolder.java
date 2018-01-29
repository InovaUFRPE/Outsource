package com.outsource.inovaufrpe.usuario.conversa.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.usuario.R;

public class MensagemViewHolder extends ViewHolder {

    public TextView nomeUsuario;
    public TextView precoSugerido;
    public TextView tvMensagem;
    public TextView tvTempo;
    public CardView cardServico;

    public MensagemViewHolder(View v) {
        super(v);

        nomeUsuario = v.findViewById(R.id.nomeUsuarioID);
        precoSugerido = v.findViewById(R.id.precoSugeridoID);
        tvMensagem = v.findViewById(R.id.tvMensagemID);
        tvTempo = v.findViewById(R.id.tvTempoID);
        cardServico = v.findViewById(R.id.cardLayout);

    }
}
