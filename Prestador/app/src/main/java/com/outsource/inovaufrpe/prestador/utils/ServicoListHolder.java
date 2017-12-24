package com.outsource.inovaufrpe.prestador.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;

public class ServicoListHolder extends RecyclerView.ViewHolder {
    public View mainLayout;
    public View linearLayout;
    View mView;
    Servico servico = new Servico();
    public TextView titulo;
    public TextView valor;
    public TextView status;
    public TextView solicitante;
    public TextView data;


    public ServicoListHolder(final View itemView) {
        super(itemView);
        mView = itemView;

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

        mView = itemView.findViewById(R.id.servico_card);
    }

    private ServicoListHolder.ClickListener mClickListener;

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(ServicoListHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }
}
