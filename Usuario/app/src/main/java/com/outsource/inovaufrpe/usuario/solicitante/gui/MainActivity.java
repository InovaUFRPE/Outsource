package com.outsource.inovaufrpe.usuario.solicitante.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.outsource.inovaufrpe.usuario.R;
import com.outsource.inovaufrpe.usuario.carteira.gui.MainCarteiraFragment;
import com.outsource.inovaufrpe.usuario.servico.gui.CadastroServicoActivity;
import com.outsource.inovaufrpe.usuario.servico.gui.MainServicosFragment;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";
    private FragmentManager fm = getSupportFragmentManager();
    private int mSelectedItem;
    private String ultimoFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = navigation.getMenu().findItem(mSelectedItem);

        } else {
            MainServicosFragment frag1 = new MainServicosFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, frag1);
            ft.commit();
            selectedItem = navigation.getMenu().getItem(0);
        }
        selectFragment(selectedItem);
        fm.popBackStack();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    private void selectFragment(MenuItem item) {
        FragmentTransaction ft = fm.beginTransaction();

        switch (item.getItemId()) {
            case R.id.navigation_servicos:
                alterarFragment("0", ft, getServicosFragment());
                break;
            case R.id.navigation_carteira:
                alterarFragment("2", ft, getCarteiraFragment());
                break;
            case R.id.navigation_perfil:
                alterarFragment("3", ft, getPerfilFragment());
        }

        mSelectedItem = item.getItemId();
        updateToolbarText(item.getTitle());
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }

    private void monitorarPilha(FragmentManager fragmento) {
        if (fragmento.getBackStackEntryCount() >= 3) {
            fragmento.popBackStack();
        }
    }

    private void alterarFragment(String frag, FragmentTransaction ft, Fragment f) {
        if (!frag.equals(this.ultimoFrag)) {
            ft.replace(R.id.container, f, frag);
            ft.addToBackStack("pilha");
            monitorarPilha(fm);
            ft.commit();
            ultimoFrag = frag;
        }
    }

    private MainServicosFragment getServicosFragment() {
        return new MainServicosFragment();
    }
    private MainCarteiraFragment getCarteiraFragment() {
        return new MainCarteiraFragment();
    }
    private MainPerfilFragment getPerfilFragment() {
        return new MainPerfilFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notificacoesBtn) {
            Toast.makeText(MainActivity.this, "Notificações", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.inserirBtn){
            startActivity(new Intent(this,CadastroServicoActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}