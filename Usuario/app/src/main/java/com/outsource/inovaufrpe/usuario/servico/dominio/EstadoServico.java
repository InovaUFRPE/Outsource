package com.outsource.inovaufrpe.usuario.servico.dominio;

public enum EstadoServico {
    ABERTA("aberto"), NEGOCIACAO("negociacao"),ANDAMENTO("andamento"), CONCLUIDA("concluido");

    private final String estado;
    EstadoServico(String estado) { this.estado = estado; }
    public String getValue() { return estado; }
}
