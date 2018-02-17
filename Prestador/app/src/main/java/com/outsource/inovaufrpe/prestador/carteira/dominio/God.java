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

    public static Double getTAXA() {
        return TAXA;
    }

    public void adicionar(Double mais) {
        this.gods = this.gods + mais*(1 - getTAXA());
    }

    public static Double getFundos() {
        return fundos;
    }

    public static void setFundos(Double maisFundos) {
        fundos = (maisFundos * getTAXA());
    }
}
