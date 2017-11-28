package com.outsource.inovaufrpe.usuario.servico.dominio;

/**
 * Created by Pichau on 21/11/2017.
 */

public enum EstadoServico {
    ABERTA("aberto"), NEGOCIACAO("negociacao"),ANDAMENTO("andamento"), CONCLUIDA("concluido");

    private final String estado;
    EstadoServico(String estado) { this.estado = estado; }
    public String getValue() { return estado; }
}
