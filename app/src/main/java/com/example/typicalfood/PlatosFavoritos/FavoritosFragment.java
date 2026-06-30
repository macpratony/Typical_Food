package com.example.typicalfood.PlatosFavoritos;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.typicalfood.Adapter.AdapterFavorito;
import com.example.typicalfood.Base.BaseInterfazFragment;
import com.example.typicalfood.Entity.FavoritosPlatos;

import com.example.typicalfood.R;

import com.example.typicalfood.Utils.UIUtils;
import com.example.typicalfood.ViewModel.ViewModelFavorites;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FavoritosFragment extends BaseInterfazFragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private AdapterFavorito adapter;
    private RecyclerView recyclerView;
    private List<FavoritosPlatos> platosList;
    private List<FavoritosPlatos> listPlate = new ArrayList<>();
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
        UIUtils.styleProgressBar(progressBar);

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
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            txtMensaje.setVisibility(View.VISIBLE);
            txtMensaje2.setVisibility(View.VISIBLE);
            adapter = new AdapterFavorito(getContext(), R.layout.item_platos_provincia, (ArrayList<FavoritosPlatos>) platosList);
            recyclerView.setAdapter(adapter);
        }
    }
}
