package com.outsource.inovaufrpe.prestador.prestador.dominio;

public class Critica {
    private int nota;
    private String comentadorUID;
    private String comentadorNome;
    private String comentario;
    private String data;

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentadorUID() {
        return comentadorUID;
    }

    public void setComentadorUID(String comentadorUID) {
        this.comentadorUID = comentadorUID;
    }

    public String getComentadorNome() {
        return comentadorNome;
    }

    public void setComentadorNome(String comentadorNome) {
        this.comentadorNome = comentadorNome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


}
