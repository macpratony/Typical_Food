package com.example.typicalfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private final int TIEMPO_ESPERA = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);

        ImageView logoImageView = findViewById(R.id.imageViewLog);
        TextView textSplash = findViewById(R.id.textViewSplash);

        logoImageView.setAnimation(animacion1);
        textSplash.setAnimation(animacion1);

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, NavigationDrawerActivity.class));
                finish();
            }
        },TIEMPO_ESPERA);

    }
}