package com.outsource.inovaufrpe.prestador.carteira.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.prestador.adapter.TabAdapter;


public class MainCarteiraFragment extends android.support.v4.app.Fragment {

    public MainCarteiraFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carteira, container, false);

        ViewPager viewPager = view.findViewById(R.id.pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabAdapter adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new SaldoFragment(), "Saldo");
        adapter.addFragment(new HistoricoServicosFragment(), "Histórico");
        viewPager.setAdapter(adapter);
    }
}