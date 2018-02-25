package com.outsource.inovaufrpe.prestador.notificacao.dominio;

/**
 * Created by Pichau on 29/01/2018.
 */

//servicoId = intent.getStringExtra("servicoID");
//        estadoId = intent.getStringExtra("estado");
//        nomeServico = intent.getStringExtra("nomeServico");


public class Notificacao {

    private String servicoID;
    private String estado;
    private String nomeServico;
    private String textoNotificacao;
    private long tempo;
    private String ordemRef;
    private int tipoNotificacao;
    private boolean lido = false;

    public String getServicoID() {
        return servicoID;
    }

    public void setServicoID(String servicoID) {
        this.servicoID = servicoID;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public String getTextoNotificacao() {
        return textoNotificacao;
    }

    public void setTextoNotificacao(String textoNotificacao) {
        this.textoNotificacao = textoNotificacao;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }

    public String getOrdemRef() {
        return ordemRef;
    }

    public void setOrdemRef(String ordemRef) {
        this.ordemRef = ordemRef;
    }

    public int getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(int tipoNotificacao) {
        this.tipoNotificacao = tipoNotificacao;
    }

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }
}

