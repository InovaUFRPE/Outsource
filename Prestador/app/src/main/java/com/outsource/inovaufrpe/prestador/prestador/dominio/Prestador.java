package com.outsource.inovaufrpe.prestador.prestador.dominio;

import com.outsource.inovaufrpe.prestador.carteira.dominio.God;
import com.outsource.inovaufrpe.prestador.servico.dominio.Servico;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pichau on 19/11/2017.
 */

public class Prestador {
    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String nascimento;
    private float dinheiro;
    private List<Servico> listaServicos = new ArrayList<Servico>();
    private God carteira;

    public Prestador() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public String getUsername() {
        return sobrenome;
    }

    public void setUsername(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public List<Servico> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<Servico> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public float getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(float dinheiro) {
        this.dinheiro = dinheiro;
    }

    public Double getCarteira() {
        return carteira.getMoeda();
    }

    public void setCarteira(Double carteira) {
        this.carteira = new God(carteira);
    }

}
