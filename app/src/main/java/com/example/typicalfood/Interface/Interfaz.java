package com.example.typicalfood.Interface;

import com.example.typicalfood.Adapter.AdapterPlatos;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Entity.Platos;

public interface Interfaz {

    public AdapterPlatos getAllProvince(String ciudad);
    public void enviarPlatos(Platos platos, String ciudad);
    public void enviarPlatosFavoritos(FavoritosPlatos platos);
    public void regresar();


    public void accesAdministrator(String nombre, String correo);
    public void agregarPlatos();



    }
