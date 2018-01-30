package com.outsource.inovaufrpe.prestador.servico.dominio;

/**
 * Created by Pichau on 29/01/2018.
 */

public class Oferta {

    private String prestadorNome;
    private String prestadorId;
    private double ofertaValor;
    private long tempo;

    public String getPrestadorNome() {
        return prestadorNome;
    }

    public void setPrestadorNome(String prestadorNome) {
        this.prestadorNome = prestadorNome;
    }

    public String getPrestadorId() {
        return prestadorId;
    }

    public void setPrestadorId(String prestadorId) {
        this.prestadorId = prestadorId;
    }

    public double getOfertaValor() {
        return ofertaValor;
    }

    public void setOfertaValor(double ofertaValor) {
        this.ofertaValor = ofertaValor;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }
}
