package com.outsource.inovaufrpe.usuario.solicitante.dominio;

import com.outsource.inovaufrpe.usuario.servico.dominio.Servico;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicollas on 05/11/2017.
 */

//Classe representando um usuario do tipo cliente no sistema

public class Usuario {
    private String id;
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String nascimento;
    private int nota;
    private List<Servico> listaServicos = new ArrayList<Servico>();

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

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public List<Servico> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<Servico> listaServicos) {
        this.listaServicos = listaServicos;
    }
}
