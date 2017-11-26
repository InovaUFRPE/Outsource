package com.outsource.inovaufrpe.prestador.servico.dominio;

import java.util.Date;
import java.util.Map;

/**
 * Created by Pichau on 21/11/2017.
 */

public class Servico {
    private String id;
    private String idCriador;
    private String nome;
    private String descricao;
    private String preco;
    private String data;
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

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

