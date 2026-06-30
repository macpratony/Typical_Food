package com.example.typicalfood.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.typicalfood.Adapter.AdapterFavorito;
import com.example.typicalfood.Base.BaseInterfazFragment;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.R;
import com.example.typicalfood.ScreenMainActivity;
import com.example.typicalfood.Utils.UIUtils;
import com.example.typicalfood.ViewModel.ViewModelFavorites;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


@SuppressWarnings("ALL")
public class DetallePlatoFragment extends BaseInterfazFragment {

    private TextView titulo;
    private ImageView imagen;
    private TextView descripcion;
    private LottieAnimationView lottieAnimationView;
    private DocumentReference documentRef2;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private Platos platos = null;
    private FavoritosPlatos plate = null;
    private String provincia;
    private AdapterFavorito adapterFavorito;
    private RecyclerView recyclerView;
    private ViewModelFavorites viewModel;
    private String tit;
    private List<FavoritosPlatos> platosList = new ArrayList<>();

    private ArrayList<DocumentReference> ref = new ArrayList<DocumentReference>();

    private String message;
    private ProgressBar mProgressBar;

    private boolean isFavorite = false;
    private boolean traductor = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_plato, container, false);

        titulo = view.findViewById(R.id.tituloDetalle);
        imagen = view.findViewById(R.id.imagenDetalle);
        descripcion = view.findViewById(R.id.descripcionDetalle);
        lottieAnimationView = view.findViewById(R.id.plusImageView);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = view.findViewById(R.id.progressBarDetalle);
        UIUtils.styleProgressBar(mProgressBar);

        String idioma = Locale.getDefault().getLanguage();

        Bundle objetoPlato = getArguments();
        if(objetoPlato != null){
            provincia = (String) objetoPlato.getSerializable("provincia");
            if(provincia != null){
                platos = (Platos) objetoPlato.getSerializable("objeto");
                titulo.setText(platos.getTitulo().toUpperCase());
                Glide.with(getContext()).load(platos.getFoto()).into(imagen);
                tit = platos.getTitulo();
                if(idioma.equals("en")){
                    translateDescription(platos.getDescripcion());
                }else{
                    descripcion.setText(platos.getDescripcion());
                }

            }else{
                plate = (FavoritosPlatos) objetoPlato.getSerializable("objeto");
                titulo.setText(plate.getTitulo().toUpperCase());
                Glide.with(getContext()).load(plate.getFoto()).into(imagen);
                descripcion.setText(plate.getDescripcion());
                provincia = plate.getProvincia();
                tit = plate.getTitulo();
                if(idioma.equals("en")){
                    translateDescription(plate.getDescripcion());
                }else{
                    descripcion.setText(plate.getDescripcion());
                }
            }
        }

        if(mAuth.getCurrentUser() != null){
            viewModel = new ViewModelProvider(this).get(ViewModelFavorites.class);
        }

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite =  likeAnimation(lottieAnimationView, R.raw.black_joy, isFavorite);
            }
        });

        return view;
    }

    public void translateDescription(String text){

        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.SPANISH)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();
        final Translator spanishEnglishTranslator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        spanishEnglishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                traductor = true;
                                textTraducido(spanishEnglishTranslator, traductor, text);

                            }
                        })
                .addOnFailureListener(
                        new com.google.android.gms.tasks.OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

    }

    public void textTraducido(Translator spanishEnglishTranslator, boolean traductor, String text){
        if(traductor){
            spanishEnglishTranslator.translate(text)
                    .addOnSuccessListener(
                            new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    descripcion.setText(o.toString());
                                    if(spanishEnglishTranslator != null){
                                        spanishEnglishTranslator.close();
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new com.google.android.gms.tasks.OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

        }

    }


    private boolean likeAnimation(LottieAnimationView imageView, int animation, boolean like){

        if(mAuth.getCurrentUser() != null){
            getPositionNamePlate(imageView, animation,like);

        }else{
            String title = getString(R.string.titulo);
            String msg = getString(R.string.mensaje_alert_dialog2);
            String yes = getString(R.string.mensaje_si);
            String no = getString(R.string.mensaje_no);

            UIUtils.showAlertDialog(getActivity(), title, msg, yes,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(), ScreenMainActivity.class);
                            startActivity(i);
                        }
                    },
                    no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            message = getString(R.string.mensaje_favorito);
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return !like;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAuth.getCurrentUser() != null){
            imagen.setVisibility(View.GONE);
            descripcion.setVisibility(View.GONE);
            lottieAnimationView.setVisibility(View.GONE);
            titulo.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            viewModel.getFavPlatos().observe(getViewLifecycleOwner(), new Observer<List<FavoritosPlatos>>() {

                @Override
                public void onChanged(List<FavoritosPlatos> favoritosPlatosList) {

                    platosList = favoritosPlatosList;

                    for(int i=0; i<platosList.size(); i++){
                        if(platosList.get(i).getTitulo().toLowerCase().equals(tit.toLowerCase())){
                            isFavorite = true;
                            break;
                        }
                    }
                    favoritePlate();
                }
            });

        }else{

        }
    }

    public void favoritePlate(){

        if(isFavorite){
            lottieAnimationView.setImageResource(R.drawable.twitter_like2);
        }else{
            lottieAnimationView.setImageResource(R.drawable.twitter_like);
        }
        imagen.setVisibility(View.VISIBLE);
        descripcion.setVisibility(View.VISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        titulo.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void getPositionNamePlate(LottieAnimationView imageView, int animation, boolean like){

        mFirestore.collection("Provincias").document(provincia).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ArrayList lista = (ArrayList) documentSnapshot.getData().get("platos");
                    List<String> titulos = new ArrayList<>();
                    for(int i=0; i< lista.size(); i++) {
                        Map<String, String> map = (Map<String, String>) lista.get(i);
                        for (Map.Entry<String, String> listPlatos : map.entrySet()) {
                            String clave = listPlatos.getKey();
                            String valor = listPlatos.getValue();

                            if (clave.equals("titulo")) {
                                titulos.add(valor.toLowerCase());
                            }
                        }

                        if((platos != null && titulos.contains(platos.getTitulo().toLowerCase())) || (plate != null && titulos.contains(plate.getTitulo().toLowerCase()))){
                            documentRef2 = mFirestore.document("Provincias/"+provincia+"/platos/"+i);

                            if(!like){
                                mFirestore.collection("Users")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .update("favorites", FieldValue.arrayUnion(documentRef2));
                                imageView.setAnimation(animation);
                                imageView.playAnimation();

                            }else {
                                mFirestore.collection("Users")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .update("favorites", FieldValue.arrayRemove(documentRef2));
                                imageView.setImageResource(R.drawable.twitter_like);
                            }
                            break;
                        }
                    }

                }else{
                    System.out.println("No existe nada en el documento");
                }
            }
        });

    }
}
