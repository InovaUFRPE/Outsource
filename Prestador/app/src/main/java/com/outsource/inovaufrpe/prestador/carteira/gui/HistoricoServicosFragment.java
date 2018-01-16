package com.outsource.inovaufrpe.prestador.carteira.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outsource.inovaufrpe.prestador.R;

public class HistoricoServicosFragment extends Fragment {


    public HistoricoServicosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historico_servicos, container, false);
    }

}
