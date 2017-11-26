package com.outsource.inovaufrpe.usuario.servico.dominio;

import java.util.Date;

/**
 * Created by Heitor on 19/11/2017.
 */

public class Servico {
    private String id;
    private String nome;
    private String descricao;
    private String preco;
    private String valorAtual;
    private Date data;
    private EstadoServico estado;

    public Servico() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public EstadoServico getEstado() {
        return estado;
    }

    public void setEstado(EstadoServico estado) {
        this.estado = estado;
    }

}
