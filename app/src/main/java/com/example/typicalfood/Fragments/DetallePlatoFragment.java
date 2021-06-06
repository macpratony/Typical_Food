package com.example.typicalfood.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.typicalfood.Adapter.AdapterFavorito;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.Interface.Interfaz;
import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;
import com.example.typicalfood.Pojo.UserPojo;
import com.example.typicalfood.R;
import com.example.typicalfood.ScreenMainActivity;

import com.example.typicalfood.ViewModel.ViewModelFavorites;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("ALL")
public class DetallePlatoFragment extends Fragment {

    private TextView titulo;
    private ImageView imagen;
    private TextView descripcion;
    private LottieAnimationView lottieAnimationView;
    private DocumentReference documentRef;
    private DocumentReference documentRef2;
    private Task<DocumentSnapshot> favorito;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private Platos platos = null;
    private String provincia;
    private AdapterFavorito adapterFavorito;
    private RecyclerView recyclerView;

    private Interfaz mInterfaz;
    private Activity actividad;
    private ArrayList<DocumentReference> ref = new ArrayList<DocumentReference>();


    private boolean isFavorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_plato, container, false);

        titulo = view.findViewById(R.id.tituloDetalle);
        imagen = view.findViewById(R.id.imagenDetalle);
        descripcion = view.findViewById(R.id.descripcionDetalle);
        lottieAnimationView = view.findViewById(R.id.likeImageView);


        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Bundle objetoPlato = getArguments();
        if(objetoPlato != null){
            platos = (Platos) objetoPlato.getSerializable("objeto");
            provincia = (String) objetoPlato.getSerializable("provincia");
            titulo.setText(platos.getTitulo().toUpperCase());
            Glide.with(getContext()).load(platos.getFoto()).into(imagen);
            descripcion.setText(platos.getDescripcion());

        }
        getPositionNamePlate();
        System.out.println(isFavorite+" *****************222222222222");
        if(isFavorite){
            lottieAnimationView.setImageResource(R.drawable.twitter_like2);
        }

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavorite =  likeAnimation(lottieAnimationView, R.raw.black_joy, isFavorite);
                System.out.println(isFavorite+" *****************");
            }
        });

        return view;
    }

    private boolean likeAnimation(LottieAnimationView imageView, int animation, boolean like){

        String titulo = platos.getTitulo();
        String foto = platos.getFoto();
        String descripcion = platos.getDescripcion();


        if(mAuth.getCurrentUser() != null){
            //documentRef = mFirestore.collection("Provincias").document(provincia); //Obtiene la referencia del plato al que le da like
            documentRef = getPositionNamePlate();

            if(documentRef != null) {
                if(!like){
                    mInterfaz.addReference(documentRef); //Envia la referencia a traves de la interfaz
                    imageView.setAnimation(animation);
                    imageView.playAnimation();

                }else {
                    mInterfaz.removeReference(documentRef);
                    imageView.setImageResource(R.drawable.twitter_like);
                }
            }
        }else{
            //Si no existe usuario registrado sale una ventana de alerta
            AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
            alerta.setTitle("INFORMATION")
                    .setMessage("Para agregar platos a favoritos es necesario que inicie sesión \n\n ¿Desea iniciar sesión?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(), ScreenMainActivity.class);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Toast.makeText(getContext(), "No se ha podido añadir el plato a favorito", Toast.LENGTH_SHORT).show();
                        }
                    });
            alerta.show();
        }
        return !like;
    }

    public DocumentReference getPositionNamePlate(){
        mFirestore.collection("Provincias").document(provincia).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ArrayList lista = (ArrayList) documentSnapshot.getData().get("platos");
                    for(int i=0; i< lista.size(); i++){

                        Map<String, String> map = (Map<String,String>) lista.get(i);
                        for (Map.Entry<String, String> listPlatos : map.entrySet()) {
                            if(platos.getTitulo().toLowerCase().equals(listPlatos.getValue().toLowerCase())){
                                documentRef2 = mFirestore.document("Provincias/"+provincia+"/platos/"+i); //Obtiene la ruta completa del plato
                                String path = "Provincias/"+provincia+"/platos/"+i;
                                isFavorite = existPlate(path);
                            }

                        }
                    }
                }else{
                    System.out.println("No existe nada en el documento");
                }
            }
        });
        return documentRef2;
    }



    public boolean existPlate(String docRef){

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               if(documentSnapshot.exists()){
                   UserPojo user = documentSnapshot.toObject(UserPojo.class);
                   List<DocumentReference> ref = user.getFavorites();

                   if(ref.size() > 0){
                       for (int i= 0; i < ref.size(); i++){
                           String r = ref.get(i).getPath();
                           if(docRef!=null){
                               if(docRef.equals(r)){
                                   isFavorite = true;
                               }
                           }
                        }
                   }
               }else{
                   Toast.makeText(getContext(), "no existe favoritos", Toast.LENGTH_SHORT).show();
               }
            }
        });

        return isFavorite;
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