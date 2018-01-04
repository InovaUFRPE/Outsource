package com.outsource.inovaufrpe.usuario.servico.dominio;

import com.outsource.inovaufrpe.usuario.carteira.dominio.God;

public class Servico {
    private String id;
    private String idCriador;
    private String idPrestador;
    private String nome;
    private String descricao;
    private God preco;
    private God oferta;
    private String ofertante;
    private String data;
    private Double latitude;
    private Double longitude;
    private String estado;

    public Servico() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(String idCriador) {
        this.idCriador = idCriador;
    }

    public String getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(String idPrestador) {
        this.idPrestador = idPrestador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco.getMoeda();
    }

    public void setPreco(Double preco) {
        this.preco = new God(preco);
    }

    public Double getOferta() {
        return oferta.getMoeda();
    }

    public void setOferta(Double oferta) {
        this.oferta = new God(oferta);
    }

    public String getOfertante() {
        return ofertante;
    }

    public void setOfertante(String ofertante) {
        this.ofertante = ofertante;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

