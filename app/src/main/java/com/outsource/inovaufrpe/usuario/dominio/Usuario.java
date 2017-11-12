package com.outsource.inovaufrpe.usuario.dominio;

/**
 * Created by Nicollas on 05/11/2017.
 */

//Classe representando um usuario do tipo cliente no sistema

public class Usuario {
    private String id;
    private String nome;
    private String username;
    private String nascimento;
    private String numero;

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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
