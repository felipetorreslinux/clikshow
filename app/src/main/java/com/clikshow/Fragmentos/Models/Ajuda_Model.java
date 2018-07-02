package com.clikshow.Fragmentos.Models;

public class Ajuda_Model {
    private String titulo;
    private String texto;
    private int open_tab;

    public Ajuda_Model(String titulo, String texto, int open_tab) {
        this.titulo = titulo;
        this.texto = texto;
        this.open_tab = open_tab;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getOpen_tab() {
        return open_tab;
    }

    public void setOpen_tab(int open_tab) {
        this.open_tab = open_tab;
    }
}
