package com.outsource.inovaufrpe.prestador.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.dominio.Comentario;

import java.util.List;

/**
 * Created by Pichau on 06/01/2018.
 */

public class ComentarioViewHolder extends RecyclerView.ViewHolder {

        public TextView nomeUsuario;
        public TextView precoSugerido;
        public TextView tvComentario;
        public TextView tvTempo;

        public ComentarioViewHolder(View v) {
            super(v);

            nomeUsuario = v.findViewById(R.id.nomeUsuarioID);
            precoSugerido = v.findViewById(R.id.precoSugeridoID);
            tvComentario = v.findViewById(R.id.tvComentarioID);
            tvTempo = v.findViewById(R.id.tvTempoID);

        }
}
