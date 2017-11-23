package com.outsource.inovaufrpe.usuario.solicitante.gui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.gui.ConfiguracoesActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPerfilFragment extends Fragment {
    Button btnConfig;


    public MainPerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        btnConfig = (Button) getActivity().findViewById(R.id.bt2);
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ConfiguracoesActivity.class));
            }
        });
    }
}
