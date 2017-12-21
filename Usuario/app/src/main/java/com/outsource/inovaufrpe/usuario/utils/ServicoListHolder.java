package com.outsource.inovaufrpe.usuario.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.usuario.R;

public class ServicoListHolder extends RecyclerView.ViewHolder {
    public View mainLayout;
    public View linearLayout;
    public TextView titulo;
    public TextView valor;
    public TextView status;
    public TextView solicitante;
    public TextView data;

    public ServicoListHolder(final View itemView) {
        super(itemView);
        titulo = itemView.findViewById(R.id.tituloID);
        status = itemView.findViewById(R.id.statusID);
        valor = itemView.findViewById(R.id.valorID);
        data = itemView.findViewById(R.id.tDataServicoID);
//        solicitante = (TextView) itemView.findViewById(R.id.solicitanteID);
        mainLayout = itemView.findViewById(R.id.card_view);
        linearLayout = itemView.findViewById(R.id.servico_card);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

        View view = itemView.findViewById(R.id.servico_card);
    }

    private ServicoListHolder.ClickListener mClickListener;

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(ServicoListHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
