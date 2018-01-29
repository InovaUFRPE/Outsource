package com.outsource.inovaufrpe.prestador.servico.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outsource.inovaufrpe.prestador.R;

public class ServicoListHolder extends RecyclerView.ViewHolder {
    public View mainLayout;
    public View linearLayout;
    View mView;
    public TextView titulo;
    public TextView valor;
    public TextView status;
    public TextView data;
    public RelativeLayout barraTipoServico;



    public ServicoListHolder(final View itemView) {
        super(itemView);

        titulo = itemView.findViewById(R.id.tituloID);
        status = itemView.findViewById(R.id.statusID);
        valor = itemView.findViewById(R.id.valorID);
        data = itemView.findViewById(R.id.tDataServicoID);
//        solicitante = (TextView) itemView.findViewById(R.id.solicitanteID);
        mainLayout = itemView.findViewById(R.id.card_view);
        linearLayout = itemView.findViewById(R.id.servico_card);
        barraTipoServico = itemView.findViewById(R.id.tipo_servico_bar);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });


    }

    private ServicoListHolder.ClickListener mClickListener;

    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }

}
