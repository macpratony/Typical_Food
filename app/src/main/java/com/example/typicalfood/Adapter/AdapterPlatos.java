package com.example.typicalfood.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.typicalfood.Entity.Platos;

import java.util.ArrayList;

public class AdapterPlatos extends BasePlatosAdapter<Platos> {

    public AdapterPlatos(@NonNull Context context, int resource, @NonNull ArrayList<Platos> platosList) {
        super(context, resource, platosList);
    }

    @Override
    protected String getTitulo(Platos item) {
        return item.getTitulo();
    }

    @Override
    protected String getFoto(Platos item) {
        return item.getFoto();
    }
}
