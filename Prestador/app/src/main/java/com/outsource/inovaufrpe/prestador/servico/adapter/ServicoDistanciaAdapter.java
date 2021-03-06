package com.outsource.inovaufrpe.prestador.servico.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.servico.dominio.ServicoView;
import com.outsource.inovaufrpe.prestador.utils.CardFormat;

import java.util.List;

/**
 * Created by Pichau on 06/01/2018.
 */

public class ServicoDistanciaAdapter extends RecyclerView.Adapter {

    private List<ServicoView> servicos;
    private Context context;
    private RelativeLayout barraTipoServico;


    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position);
    }


    public ServicoDistanciaAdapter(List<ServicoView> servicos, Context context, OnItemClicked onClick){
        this.servicos = servicos;
        this.context = context;
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
        ServicoView servico = servicos.get(position);
        viewHolder.mainLayout.setVisibility(View.VISIBLE);
        viewHolder.linearLayout.setVisibility(View.VISIBLE);
        viewHolder.titulo.setText(servico.getNome());
        viewHolder.status.setText(servico.getEstado());
        viewHolder.data.setText(CardFormat.dataFormat(servico.getData(),"dd/MM/yyyy"));
        viewHolder.setOnClickListener(new ServicoListHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onClick.onItemClick(position);
            }
        });

        if(servico.getPreco().doubleValue() == Double.parseDouble("0")){
            viewHolder.valor.setText("A comb.");
        }else {
            viewHolder.valor.setText(CardFormat.dinheiroFormat(servico.getPreco().toString()));
        }
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
