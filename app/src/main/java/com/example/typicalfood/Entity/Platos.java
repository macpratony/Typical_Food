package com.example.typicalfood.Entity;

import java.io.Serializable;

public class Platos implements Serializable {

    private String titulo;
    private String foto;
    private String descripcion;

    public Platos() {
    }

    public Platos(String titulo,  String foto, String descripcion) {
        this.titulo = titulo;
        this.foto = foto;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
