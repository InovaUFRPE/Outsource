package com.outsource.inovaufrpe.prestador.prestador.dominio;

public class Comentario {
    private String texto;
    private long tempo;
    private String autor;
    private String nomeAutor;
    private Double valor;

    public Comentario() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getTempo() {
        return tempo;
    }

    public void setTempo(long tempo) {
        this.tempo = tempo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public Double getvalor() {
        return valor;
    }

    public void setvalor(Double valor) {
        this.valor = valor;
    }

}