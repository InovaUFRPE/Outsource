package com.outsource.inovaufrpe.prestador.utils;

final class Sessao {
    static String idUsuarioConectado;
    static String nomeUsuarioConectado;


    private Sessao() {

    }


    public static String getIdUsuarioConectado() {
        return idUsuarioConectado;
    }

    public static void setIdUsuarioConectado(String idUsuarioConectado) {
        idUsuarioConectado = idUsuarioConectado;
    }

    public static String getNomeUsuarioConectado() {
        return nomeUsuarioConectado;
    }

    public static void setNomeUsuarioConectado(String nomeUsuarioConectado) {
        nomeUsuarioConectado = nomeUsuarioConectado;
    }
}
