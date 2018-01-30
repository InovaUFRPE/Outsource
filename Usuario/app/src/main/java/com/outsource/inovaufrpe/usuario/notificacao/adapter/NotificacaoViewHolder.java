package com.outsource.inovaufrpe.usuario.notificacao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.usuario.R;

/**
 * Created by Pichau on 29/01/2018.
 */

public class NotificacaoViewHolder extends RecyclerView.ViewHolder {

    public TextView tvnomeServico;
    public TextView tvtempo;
    public TextView tvTextoNotificacao;

    public NotificacaoViewHolder(View v) {
        super(v);

        tvnomeServico = v.findViewById(R.id.tvnomeServicoID);
        tvtempo = v.findViewById(R.id.tvTempoID);
        tvTextoNotificacao = v.findViewById(R.id.tvTextoNotificacaoID);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

    }

    private NotificacaoViewHolder.ClickListener mClickListener;

    public void setOnClickListener(NotificacaoViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }

}
