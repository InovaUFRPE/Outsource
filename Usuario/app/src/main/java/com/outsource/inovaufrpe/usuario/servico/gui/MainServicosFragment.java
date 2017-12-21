package com.outsource.inovaufrpe.usuario.servico.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.solicitante.adapter.TabAdapter;

public class MainServicosFragment extends android.support.v4.app.Fragment {

    public MainServicosFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_servicos, container, false);

        ViewPager viewPager = view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new ServicosPessoaisFragment(), getString(R.string.meus));
        adapter.addFragment(new ServicosAndamentoFragment(), getString(R.string.em_andamento));
        adapter.addFragment(new ServicosConcluidosFragment(), getString(R.string.concluidos));
        viewPager.setAdapter(adapter);
    }
}
