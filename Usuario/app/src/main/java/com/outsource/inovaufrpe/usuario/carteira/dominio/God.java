package com.outsource.inovaufrpe.usuario.carteira.dominio;

import java.math.BigDecimal;

/**
 * Created by Heitor on 23/11/2017.
 */

public class God {
    private Long gods;

    public God(Long moeda) {
        this.gods = moeda;
    }

    public Long getMoeda() {
        return gods;
    }

    private void setMoeda(Long moeda) {
        this.gods = moeda;
    }

    public void adicionar(Long mais){
        this.gods = this.gods + mais;
    }
    public void subtrair(Long menos){
        this.gods = this.gods - menos;
    }
    public void multiplicar(Long vezes){
        this.gods = this.gods * vezes;
    }
    public void dividir(Long divisor){
        this.gods = this.gods / divisor;
    }
}
