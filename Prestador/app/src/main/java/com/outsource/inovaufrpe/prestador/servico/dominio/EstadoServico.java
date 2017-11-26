package com.outsource.inovaufrpe.prestador.servico.dominio;

/**
 * Created by Pichau on 21/11/2017.
 */

public enum EstadoServico {
    ABERTA("Aberto"), NEGOCIACAO("Negociação"),ANDAMENTO("Em andamento"), CONCLUIDA("Concluida");

    private final String estado;
    EstadoServico(String estado) { this.estado = estado; }
    public String getValue() { return estado; }
}
