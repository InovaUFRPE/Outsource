package com.outsource.inovaufrpe.prestador.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.outsource.inovaufrpe.prestador.prestador.gui.LoginActivity;

public final class Sessao {
    private static Sessao instancia;
    private SharedPreferences preferencias;
    private SharedPreferences.Editor editor;
    private Context contexto;

    private static final String PREFERENCIA = "Sessao";
    private static final String FILTRO_URGENCIA = "Filtro_Urgencia";
    private static final String FILTRO_RANGE = "Filtro_Range";
    private static final String FILTRO_TERMOS = "Filtro_Termos";
    private static final String SESSAO_ATIVA = "Sessao_Ativa";

    private boolean urgencia;
    private int range;
    private String filtro;


    public static synchronized Sessao getInstancia(Context context) {
        if (instancia == null) {
            instancia = new Sessao(context.getApplicationContext());
        }
        return instancia;
    }

    private Sessao(Context context) {
        this.contexto = context;
        preferencias = this.contexto.getSharedPreferences(PREFERENCIA, Context.MODE_PRIVATE);
        editor = preferencias.edit();
        editor.apply();
        getSessao();
    }

    public void salvarSessao() {
        editor.putBoolean(FILTRO_URGENCIA, this.urgencia);
        editor.putInt(FILTRO_RANGE, this.range);
        editor.putString(FILTRO_TERMOS, this.filtro);
        editor.putBoolean(SESSAO_ATIVA, true);
        editor.commit();
    }

    public void encerraSessao() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(contexto, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        contexto.startActivity(intent);
    }

    public boolean isUrgencia() {
        return urgencia;
    }

    public void setUrgencia(boolean urgencia) {
        this.urgencia = urgencia;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public boolean getSessaoAtiva(){
        return PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean(SESSAO_ATIVA,false);
    }

    public void getSessao(){
        this.filtro = PreferenceManager.getDefaultSharedPreferences(contexto).getString(FILTRO_TERMOS,"");
        this.range = PreferenceManager.getDefaultSharedPreferences(contexto).getInt(FILTRO_RANGE,10);
        this.urgencia = PreferenceManager.getDefaultSharedPreferences(contexto).getBoolean(SESSAO_ATIVA,true);
    }

}
