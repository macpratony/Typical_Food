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


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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

    private AdapterFavorito adapter;
    private RecyclerView recyclerView;
    private List<FavoritosPlatos> platosList;
    private List<FavoritosPlatos> listPlate = new ArrayList<>();
    private Interfaz mInterfaz;
    private Activity actividad;
    private TextView txtMensaje;
    private TextView txtMensaje2;


    protected ViewModelFavorites viewModel;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

            recyclerView = view.findViewById(R.id.recicleFav);
            txtMensaje = view.findViewById(R.id.txtMensajeFavorito);
            txtMensaje2 = view.findViewById(R.id.txtMensajeFavorito2);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progressBarFavorito);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

            mAuth = FirebaseAuth.getInstance();
            mFirestore = FirebaseFirestore.getInstance();
            if(mAuth.getCurrentUser() != null) {
                viewModel = new ViewModelProvider(this).get(ViewModelFavorites.class);
            }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
         progressBar.setVisibility(View.VISIBLE);
         recyclerView.setVisibility(View.GONE);
        txtMensaje.setVisibility(View.GONE);
        txtMensaje2.setVisibility(View.GONE);

        if(mAuth.getCurrentUser() != null){
            viewModel.getFavPlatos().observe(getViewLifecycleOwner(), new Observer<List<FavoritosPlatos>>() {
                @Override
                public void onChanged(List<FavoritosPlatos> favoritosPlatosList) {
                    platosList = new ArrayList<>();
                    platosList = favoritosPlatosList;

                    if(platosList.size() == 0){

                        recyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        txtMensaje.setVisibility(View.VISIBLE);
                        txtMensaje2.setVisibility(View.VISIBLE);

                    }else{
                        progressBar.setVisibility(View.GONE);
                        txtMensaje.setVisibility(View.GONE);
                        txtMensaje2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        adapter = new AdapterFavorito(getContext(), R.layout.item_platos_provincia, (ArrayList<FavoritosPlatos>) platosList);
                        recyclerView.setAdapter(adapter);

                        adapter.setOnclickListener(view -> {

                            mInterfaz.enviarPlatosFavoritos(platosList.get(recyclerView.getChildAdapterPosition(view)));

                        });
                    }

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
    }
}