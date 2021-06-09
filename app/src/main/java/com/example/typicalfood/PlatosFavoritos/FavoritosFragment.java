package com.example.typicalfood.PlatosFavoritos;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.typicalfood.Adapter.AdapterFavorito;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Interface.Interfaz;

import com.example.typicalfood.Pojo.UserPojo;
import com.example.typicalfood.R;

import com.example.typicalfood.ViewModel.ViewModelFavorites;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.util.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FavoritosFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private DocumentReference mReference;

    private AdapterFavorito adapter;
    private RecyclerView recyclerView;
    private List<FavoritosPlatos> platosList = new ArrayList<>();
    private List<FavoritosPlatos> listPlate = new ArrayList<>();
    private Interfaz mInterfaz;
    private Activity actividad;

    protected ViewModelFavorites viewModel;

    private String city;
    private int posicion;
    private boolean isFavorite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

            recyclerView = view.findViewById(R.id.recicleFav);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mAuth = FirebaseAuth.getInstance();
            mFirestore = FirebaseFirestore.getInstance();
            if(mAuth.getCurrentUser() != null){
                viewModel = new ViewModelProvider(this).get(ViewModelFavorites.class);
            }



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAuth.getCurrentUser() != null){
            viewModel.getFavPlatos().observe(getViewLifecycleOwner(), new Observer<List<FavoritosPlatos>>() {
                @Override
                public void onChanged(List<FavoritosPlatos> favoritosPlatosList) {

                    platosList = favoritosPlatosList;
                    adapter = new AdapterFavorito(getContext(), R.layout.item_platos_provincia, (ArrayList<FavoritosPlatos>) platosList);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnclickListener(view -> {

                        mInterfaz.enviarPlatosFavoritos(platosList.get(recyclerView.getChildAdapterPosition(view)));

                    });
                }
            });

        }else{
            adapter = new AdapterFavorito(getContext(), R.layout.item_platos_provincia, (ArrayList<FavoritosPlatos>) platosList);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.actividad = (Activity)context;
            mInterfaz = (Interfaz) this.actividad;
        } else {
            throw new RuntimeException();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
}