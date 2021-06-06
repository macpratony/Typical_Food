package com.example.typicalfood.PlatosFavoritos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


public class DetallePlatoFavoritoFragment extends Fragment {

    private TextView titulo;
    private ImageView imagen;
    private TextView descripcion;
    private LottieAnimationView lottieAnimationView;
    private boolean like = false;
    private DocumentReference favorito;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor editor;
    private FavoritosPlatos platos = null;
    private List<FavoritosPlatos> platosFavoritosList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_plato_favorito, container, false);
        titulo = view.findViewById(R.id.tituloDetalle);
        imagen = view.findViewById(R.id.imagenDetalle);
        descripcion = view.findViewById(R.id.descripcionDetalle);
        lottieAnimationView = view.findViewById(R.id.likeImageView);
        sharedPreferences = this.getActivity().getSharedPreferences("favorito", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        db = FirebaseFirestore.getInstance();

        Bundle objetoPlato = getArguments();


        if(objetoPlato != null){

            platos = (FavoritosPlatos) objetoPlato.getSerializable("objeto");
            titulo.setText(platos.getTitulo().toUpperCase());
            Glide.with(getContext()).load(platos.getFoto()).into(imagen);
            descripcion.setText(platos.getDescripcion());
        }

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like = likeAnimation(lottieAnimationView, R.raw.black_joy,like);

            }
        });

        return view;
    }

    private boolean likeAnimation(LottieAnimationView imageView, int animation, boolean like){
            String titulo = platos.getTitulo();
            String foto = platos.getFoto();
            String descripcion = platos.getDescripcion();

        platosFavoritosList.add(new FavoritosPlatos(titulo,foto,descripcion));

        if(!like){

            imageView.setAnimation(animation);
            imageView.playAnimation();


        }else{
            imageView.setImageResource(R.drawable.twitter_like);
            editor.remove("titulo");
            editor.remove("foto");
            editor.remove("descripcion");
            editor.apply();
        }
        return !like;
    }
}