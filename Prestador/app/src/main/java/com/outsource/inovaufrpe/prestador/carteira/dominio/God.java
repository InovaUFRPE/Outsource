package com.outsource.inovaufrpe.prestador.carteira.dominio;

public class God {
    private Double gods;
    private static final Double TAXA = Double.valueOf("0.1");
    private static Double fundos;

    public God(Double moeda) {
        this.gods = moeda;
        if (fundos == null){
            fundos = Double.valueOf(0);
        }

    }

    public Double getMoeda() {
        return this.gods;
    }

    private static Double getTAXA() {
        return TAXA;
    }

    public void adicionar(Double mais) {
        this.gods = this.gods + (mais*(1-TAXA));
    }
    public void subtrair(Double menos) {
        this.gods = this.gods - menos;
    }
    public void multiplicar(Double vezes) {
        this.gods = this.gods * vezes;
    }
    public void dividir(Double divisor) {
        this.gods = this.gods / divisor;
    }

    public Double getFundos() {
        return fundos;
    }

    public static void setFundos(Double maisFundos) {
        fundos += (maisFundos * getTAXA());
    }
}
