package com.outsource.inovaufrpe.usuario.servico.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outsource.inovaufrpe.usuario.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicosAndamentoFragment extends Fragment {


    public ServicosAndamentoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_servicos_andamento, container, false);
    }

}