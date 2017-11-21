package com.outsource.inovaufrpe.prestador;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outsource.inovaufrpe.prestador.adapter.TabAdapter;


public class MainServicosFragment extends android.support.v4.app.Fragment {

    public MainServicosFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_servicos, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new ServicosNovosFragment(), "Novos");
        adapter.addFragment(new ServicosAndamentoFragment(), "Em Andamento");
        adapter.addFragment(new ServicosConcluidosFragment(), "Concluídos");
        viewPager.setAdapter(adapter);
    }
}
