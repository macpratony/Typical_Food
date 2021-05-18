package com.example.typicalfood.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.R;


public class DetallePlatoFragment extends Fragment {
    private TextView titulo;
    private ImageView imagen;
    private TextView descripcion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_plato, container, false);
        titulo = view.findViewById(R.id.tituloDetalle);
        imagen = view.findViewById(R.id.imagenDetalle);
        descripcion = view.findViewById(R.id.descripcionDetalle);

        Bundle objetoPlato = getArguments();
        Platos platos = null;
        if(objetoPlato != null){
            platos = (Platos) objetoPlato.getSerializable("objeto");
            titulo.setText(platos.getTitulo());
            Glide.with(getContext()).load(platos.getFoto()).into(imagen);
            descripcion.setText(platos.getDescripcion());
        }

        return view;
    }
}