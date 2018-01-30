package com.outsource.inovaufrpe.prestador.prestador.gui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.carteira.gui.MainCarteiraFragment;
import com.outsource.inovaufrpe.prestador.conversa.gui.ConversaActivity;
import com.outsource.inovaufrpe.prestador.notificacao.gui.NotificacaoActivity;
import com.outsource.inovaufrpe.prestador.servico.gui.MainServicosFragment;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";
    private FragmentManager fm = getSupportFragmentManager();
    private int mSelectedItem;
    private String ultimoFrag;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkGpsStatus()) {
            dialogErroGps();
        }

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
            startActivity(new Intent(this, NotificacaoActivity.class));
        }
        if (id == R.id.chatButton) {
            startActivity(new Intent(this, ConversaActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pressione mais uma vez para fechar o aplicativo", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private boolean checkGpsStatus() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void dialogErroGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seu GPS está desativado")
                .setMessage("Para ter a melhor experiência no Outsource, ative seu GPS nas configurações do aparelho")
                .setPositiveButton("Sair do app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                });
        builder.create();
        builder.show();
    }
}
