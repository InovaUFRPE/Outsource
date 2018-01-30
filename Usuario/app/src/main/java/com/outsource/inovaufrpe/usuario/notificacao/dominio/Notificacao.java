package com.outsource.inovaufrpe.usuario.notificacao.dominio;

/**
 * Created by Pichau on 29/01/2018.
 */


public class Notificacao {

    private String servicoID;
    private String estado;
    private String nomeServico;
    private String textoNotificacao;
    private long tempo;
    private int tipoNotificacao;

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

    public int getTipoNotificacao() {
        return tipoNotificacao;
    }

    public void setTipoNotificacao(int tipoNotificacao) {
        switch (tipoNotificacao){
            case 0: //Oferta aceita
                this.setTextoNotificacao("Um usuário aceitou a sua oferta!");
                break;
            case 1://Conclusão
                this.setTextoNotificacao("O prestador concluiu um dos seus serviços!");
                break;
            case 2://Oferta recusada
                this.setTextoNotificacao("O usuário aceitou a oferta de outro Prestador.");
                break;
            case 3:
                break;
        }
        this.tipoNotificacao = tipoNotificacao;
    }
}
