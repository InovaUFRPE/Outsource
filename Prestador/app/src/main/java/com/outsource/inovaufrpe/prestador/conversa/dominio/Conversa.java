package com.outsource.inovaufrpe.prestador.conversa.dominio;

/**
 * Created by Pichau on 27/01/2018.
 */

public class Conversa {

    private String conversaID;
    private String usuarioID;
    private String prestadorID;
    private String servicoID;
    private String estadoServico;
    private String servicoNome;
    private String ultimaMensagem;
    private long tempo;
    private String ordemRef;
    private boolean notificacao;
    private boolean lido = false;

    public String getConversaID() {
        return conversaID;
    }

    public void setConversaID(String conversaID) {
        this.conversaID = conversaID;
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getPrestadorID() {
        return prestadorID;
    }

    public void setPrestadorID(String prestadorID) {
        this.prestadorID = prestadorID;
    }

    public String getServicoID() {
        return servicoID;
    }

    public void setServicoID(String servicoID) {
        this.servicoID = servicoID;
    }

    public String getEstadoServico() {
        return estadoServico;
    }

    public void setEstadoServico(String estadoServico) {
        this.estadoServico = estadoServico;
    }

    public String getServicoNome() {
        return servicoNome;
    }

    public void setServicoNome(String servicoNome) {
        this.servicoNome = servicoNome;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
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

    public boolean isNotificacao() {
        return notificacao;
    }

    public void setNotificacao(boolean notificacao) {
        this.notificacao = notificacao;
    }

    public boolean isLido() {
        return lido;
    }

    public void setLido(boolean lido) {
        this.lido = lido;
    }
}

