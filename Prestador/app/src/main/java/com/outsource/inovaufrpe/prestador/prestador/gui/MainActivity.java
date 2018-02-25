package com.outsource.inovaufrpe.prestador.prestador.gui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.outsource.inovaufrpe.prestador.R;
import com.outsource.inovaufrpe.prestador.carteira.gui.MainCarteiraFragment;
import com.outsource.inovaufrpe.prestador.conversa.gui.ConversaActivity;
import com.outsource.inovaufrpe.prestador.notificacao.gui.NotificacaoActivity;
import com.outsource.inovaufrpe.prestador.servico.gui.MainServicosFragment;
import com.outsource.inovaufrpe.prestador.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String SELECTED_ITEM = "arg_selected_item";
    private FragmentManager fm = getSupportFragmentManager();
    private int mSelectedItem;
    private String ultimoFrag;
    private boolean doubleBackToExitPressedOnce = false;
    private TextView contNotificacoes;
    private TextView contChat;
    private Query queryNotfy;
    private Query queryChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        if (!checkGpsStatus()) {
            dialogErroGps();
        }

// TODO: colocar um loading pra buscar essas infos...

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        queryChat = databaseReference.child("conversaUsuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("lido").equalTo(false);
        queryNotfy = databaseReference.child("notificacao").child("usuario").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).orderByChild("lido").equalTo(false);
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
        final View notificacoesMenuBtn = menu.findItem(R.id.notificacoesBtn).getActionView();
        final View chatMenuBtn = menu.findItem(R.id.chatButton).getActionView();

        contNotificacoes = notificacoesMenuBtn.findViewById(R.id.badgeTextView);
        contChat = chatMenuBtn.findViewById(R.id.badgeTextView);

        setMenuBadge(queryChat, contChat);
        setMenuBadge(queryNotfy, contNotificacoes);

        notificacoesMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, NotificacaoActivity.class);
                startActivity(it);
            }
        });

        chatMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, ConversaActivity.class);
                startActivity(it);
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.criarToast(this, "Pressione mais uma vez para fechar o aplicativo");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private boolean checkGpsStatus() {
        String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return (provider.contains("gps") || provider.contains("network"));
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
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void setMenuBadge(Query query, final TextView tv) {
        tv.setVisibility(View.GONE);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String out = ""+dataSnapshot.getChildrenCount();
                if (Integer.valueOf(out) > 9){
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("9+");
                } else if (Integer.valueOf(out) > 0) {
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(out);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
