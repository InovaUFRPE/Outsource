package com.outsource.inovaufrpe.prestador.conversa.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outsource.inovaufrpe.prestador.R;

/**
 * Created by Pichau on 28/01/2018.
 */

public class ConversaViewHolder extends RecyclerView.ViewHolder {

    public TextView tvnomeServico;
    public TextView tvtempo;
    public TextView tvMensagem;
    public RelativeLayout tipoServico;

    public ConversaViewHolder(View v) {
        super(v);

        tvnomeServico = v.findViewById(R.id.tvnomeServicoID);
        tvtempo = v.findViewById(R.id.tvTempoID);
        tvMensagem = v.findViewById(R.id.tvMensagemID);
        tipoServico = v.findViewById(R.id.tipo_servico_bar);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

    }

    private ConversaViewHolder.ClickListener mClickListener;

    public void setOnClickListener(ConversaViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }


}
