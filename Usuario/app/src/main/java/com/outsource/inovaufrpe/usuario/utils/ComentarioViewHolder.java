package com.outsource.inovaufrpe.usuario.utils;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.outsource.inovaufrpe.usuario.R;

/**
 * Created by Pichau on 06/01/2018.
 */

public class ComentarioViewHolder extends ViewHolder {

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
