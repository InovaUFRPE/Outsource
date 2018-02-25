package com.outsource.inovaufrpe.usuario.servico.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.usuario.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pichau on 30/01/2018.
 */

public class OfertaViewHolder extends RecyclerView.ViewHolder {

    public TextView tvNomeOfertante;
    public TextView tvValorOferta;
    public TextView tvTempo;
    public CircleImageView cimFoto;

    public OfertaViewHolder(View v) {
        super(v);

        tvNomeOfertante = v.findViewById(R.id.tvNomeOfertanteID);
        tvValorOferta = v.findViewById(R.id.tvOfertaValorID);
        tvTempo = v.findViewById(R.id.tvTempoID);
        cimFoto = v.findViewById(R.id.ivFotoPrestador);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });

    }

    private OfertaViewHolder.ClickListener mClickListener;

    public void setOnClickListener(OfertaViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }


}
