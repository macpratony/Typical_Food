package com.example.typicalfood.Administrador;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.typicalfood.Interface.Interfaz;
import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;
import com.example.typicalfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.RunnableFuture;

public class AdministradorFragment extends Fragment {
    private TextView welcomeUser;
    private LottieAnimationView lottieAnimationView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Interfaz mInterfaz;

    public static int MILISEGUNDOS_ESPERA = 1500;
    private Activity actividad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_administrador, container, false);

        welcomeUser = view.findViewById(R.id.textViewWelcome);
        lottieAnimationView = view.findViewById(R.id.plusImageView);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationLottie(lottieAnimationView, MILISEGUNDOS_ESPERA);
            }
        });


        return view;
    }

    public void getDataUser(){
        if(mAuth.getCurrentUser() != null){
            String id = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String nombre = documentSnapshot.getString("name");
                        welcomeUser.setText("BIENVENIDO "+nombre);

                    }
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "error de conexion", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void animationLottie(LottieAnimationView lottie, int tiempoEspera){
        lottie.playAnimation();
        lottie.setSpeed(1.5f);
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mInterfaz.agregarPlatos();
            }
        },tiempoEspera);

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