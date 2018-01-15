package com.outsource.inovaufrpe.prestador.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;
import com.outsource.inovaufrpe.prestador.servico.gui.VisualizarServicoActivity;

import java.util.List;

/**
 * Created by Pichau on 06/01/2018.
 */

public class ServicoDistanciaAdapter extends RecyclerView.Adapter {

    private List<Servico> servicos;
    private Context context;
    private CardFormat cardFormat;
    private RelativeLayout barraTipoServico;


    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }


    public ServicoDistanciaAdapter(List<Servico> servicos, Context context, OnItemClicked onClick){
        this.servicos = servicos;
        this.context = context;
        cardFormat = new CardFormat();
        this.onClick = onClick;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_servico,parent,false);
        ServicoListHolder holder = new ServicoListHolder(view);
        barraTipoServico = view.findViewById(R.id.tipo_servico_bar);


        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        ServicoListHolder viewHolder = (ServicoListHolder) holder;
        Servico servico = servicos.get(position);
        viewHolder.mainLayout.setVisibility(View.VISIBLE);
        viewHolder.linearLayout.setVisibility(View.VISIBLE);
        viewHolder.titulo.setText(servico.getNome());
        viewHolder.status.setText(servico.getEstado());
        viewHolder.valor.setText(cardFormat.dinheiroFormat(servico.getPreco().toString()));
        viewHolder.data.setText(cardFormat.dataFormat(servico.getData()));
        viewHolder.setOnClickListener(new ServicoListHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onClick.onItemClick(position);
            }
        });

        Resources res = viewHolder.itemView.getResources();
        if (servico.isUrgente()) {
            viewHolder.barraTipoServico.setBackgroundColor(res.getColor(R.color.colorDanger));
        } else {
            viewHolder.barraTipoServico.setBackgroundColor(res.getColor(R.color.colorGreen));
        }

    }

    @Override
    public int getItemCount() {
        return servicos.size();
    }
}
