package com.example.typicalfood.Interface;

import android.os.Bundle;

import com.example.typicalfood.Adapter.AdapterPlatos;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Entity.Platos;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;
import java.util.Map;

public interface Interfaz {

   // public void getData(FirebaseFirestore db );

    public AdapterPlatos getAllProvince(String ciudad);
    public void enviarPlatos(Platos platos, String ciudad);
    public void enviarPlatosFavoritos(FavoritosPlatos platos);
    public void regresar();

    //public void saveFavourite(DocumentReference mDocument, String id);
    //public void mapaFavorito(DocumentReference mReference, int posicion, String titulo);
    //public Map<String, Object> mapFavourite();

    public Task<Void> addReference(DocumentReference mReference);
    public Task<Void> removeReference(DocumentReference mReference);



    }
