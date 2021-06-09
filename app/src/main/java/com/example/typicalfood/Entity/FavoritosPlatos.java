package com.example.typicalfood.Entity;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavoritosPlatos implements Serializable{

    private String titulo;
    private String foto;
    private String descripcion;
    private String provincia;

    public FavoritosPlatos() {
    }

    public FavoritosPlatos(String titulo, String foto, String descripcion, String provincia) {
        this.titulo = titulo;
        this.foto = foto;
        this.descripcion = descripcion;
        this.provincia = provincia;
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
