package com.outsource.inovaufrpe.usuario.carteira.dominio;

/**
 * Created by Heitor on 23/11/2017.
 */

public class God {
    private Double gods;
    private static final Double TAXA = Double.valueOf("0.1");

    public God(Double moeda) {
        this.gods = moeda;
    }

    public Double getMoeda() {
        return this.gods;
    }

    private static Double getTAXA() {
        return TAXA;
    }

    public void adicionar(Double mais){
        this.gods = this.gods + mais;
    }
    public void subtrair(Double menos){
        this.gods = this.gods - menos;
    }

}
