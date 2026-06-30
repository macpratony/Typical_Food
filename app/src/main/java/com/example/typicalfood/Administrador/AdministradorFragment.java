package com.example.typicalfood.Administrador;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.example.typicalfood.Base.BaseInterfazFragment;
import com.example.typicalfood.R;
import com.example.typicalfood.Utils.FirebaseUserHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdministradorFragment extends BaseInterfazFragment {
    private TextView welcomeUser;
    private LottieAnimationView lottieAnimationView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private String message;

    public static int MILISEGUNDOS_ESPERA = 1500;

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

        getDataUser();

        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationLottie(lottieAnimationView, MILISEGUNDOS_ESPERA);
            }
        });


        return view;
    }

    public void getDataUser(){
        FirebaseUserHelper.fetchCurrentUserInfo(mAuth, mFirestore, new FirebaseUserHelper.UserInfoCallback() {
            @Override
            public void onUserLoaded(String name, String email) {
                message = getString(R.string.admin5);
                welcomeUser.setText(message + " " + name);
            }

            @Override
            public void onError(Exception e) {
                message = getString(R.string.mensaje13);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
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
}
