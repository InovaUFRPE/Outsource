package com.outsource.inovaufrpe.usuario.solicitante.dominio;

import com.outsource.inovaufrpe.usuario.carteira.dominio.God;

import java.util.ArrayList;
import java.util.List;


public class Usuario {
    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String nascimento;
    private float nota;
    private List<String> listaServicos = new ArrayList<>();
    private God carteira;

    public Usuario() {
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

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public List<String> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<String> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public Double getCarteira() {
        return carteira.getMoeda();
    }

    public void setCarteira(Double carteira) {
        this.carteira = new God(carteira);
    }
}
