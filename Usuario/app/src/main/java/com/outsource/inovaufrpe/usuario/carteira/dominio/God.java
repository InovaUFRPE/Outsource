package com.outsource.inovaufrpe.usuario.carteira.dominio;

public class God {
    private Double gods;
    private final Double taxa = Double.valueOf("0.1");
    private Double fundos;

    public God(Double moeda) {
        this.gods = moeda;
        this.fundos = 0.0;

    }

    public Double getMoeda() {
        return this.gods;
    }

    private Double getTaxa() {
        return taxa;
    }

    public void adicionar(Double mais) {
        this.gods = this.gods + mais;
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

    public Double aplicarTaxa(String valor) {
        Double novoValor = Double.valueOf(valor);
        return novoValor + (novoValor * this.getTaxa());
    }

    public Double getFundos() {
        return fundos;
    }

    public Double setFundos(Double fundos) {
        this.fundos += (fundos * this.getTaxa());
        return getFundos();
    }
}
