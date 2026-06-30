package com.example.typicalfood.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.typicalfood.Entity.FavoritosPlatos;

import java.util.ArrayList;

public class AdapterFavorito extends BasePlatosAdapter<FavoritosPlatos> {

    public AdapterFavorito(@NonNull Context context, int resource, @NonNull ArrayList<FavoritosPlatos> platosList) {
        super(context, resource, platosList);
    }

    @Override
    protected String getTitulo(FavoritosPlatos item) {
        return item.getTitulo();
    }

    @Override
    protected String getFoto(FavoritosPlatos item) {
        return item.getFoto();
    }
}
