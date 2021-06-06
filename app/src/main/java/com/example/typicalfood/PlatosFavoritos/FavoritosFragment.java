package com.example.typicalfood.PlatosFavoritos;

import android.app.Activity;
import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.LiveData;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FavoritosFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private DocumentReference mReference;

    private AdapterFavorito adapter;
    private RecyclerView recyclerView;
    private List<FavoritosPlatos> platosList ;
    private ArrayList<FavoritosPlatos> listPlate;
    private Interfaz mInterfaz;
    private Activity actividad;

    private String city;
    private int posicion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        ViewModelFavorites viewModel = new ViewModelProvider(requireActivity()).get(ViewModelFavorites.class);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mAuth = FirebaseAuth.getInstance();
            mFirestore = FirebaseFirestore.getInstance();
            listPlate = new ArrayList<>();
            platosList = new ArrayList<>();
            //viewModel.getFavPlatos().observe(getViewLifecycleOwner(), platos ->{
            //    System.out.println(platos.size());
            //});



                //existPlate();

        return view;
    }


/*
    public void existPlate(){

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserPojo user = documentSnapshot.toObject(UserPojo.class);
                    List<DocumentReference> ref = user.getFavorites();

                    if(ref.size() > 0){
                        for (int i= 0; i < ref.size(); i++){
                            String r = ref.get(i).getPath();
                            String[] prueba = r.split("/");
                            for(int x=0; x<prueba.length; x++){
                                city = prueba[1];
                                posicion = Integer.parseInt(prueba[3]);
                                //Provincias/Albacete/platos/0
                                //El array prueba contiene el texto de arriba separado por / siendo provincia la posicion 0 , etc
                                //Enviamos por parametro la provincia que corresponde a la posicion 1 y la posicion del plato que corresponde a la posicion 3 del array
                                //Como es un string se trasforma a Integer

                            }

                            adapter = new AdapterFavorito(getContext(), R.layout.item_platos_provincia, listPlate);
                            recyclerView.setAdapter(adapter);

                        }
                    }
                }else{
                    Toast.makeText(getContext(), "no existe favoritos", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public List<FavoritosPlatos> getPlateFavorite(){

        mFirestore.collection("Provincias").document(city).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ArrayList lista = (ArrayList) documentSnapshot.getData().get("platos");

                        Map<String, String> map = (Map<String,String>) lista.get(posicion);
                        String title =  map.get("titulo");
                        String description = map.get("descripcion");
                        String photo = map.get("foto");
                        platosList.add(new FavoritosPlatos(title,description,photo));

                }else{
                    System.out.println("No existe nada en el documento");
                }
            }
        });

        return platosList;
    }
*/




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