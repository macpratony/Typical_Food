package com.example.typicalfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ScreenMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button buttonCuenta;
    private Button buttonRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_main);

        mAuth = FirebaseAuth.getInstance();

        buttonCuenta = (Button) findViewById(R.id.btnYaTengoCuenta);
        buttonRegistrarse = (Button) findViewById(R.id.btnRegistrarse);

        buttonCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScreenMainActivity.this, AutenticacionActivity.class));
            }
        });

        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScreenMainActivity.this, RegistrarseActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //PREGUNTAMOS SI EL USUARIO YA SE HA REGISTRADO SI ES ASI AL ABRIR LA APP LO LLEVA A ELEGIR UNA PROVINCIA
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(ScreenMainActivity.this, NavigationDrawerActivity.class));
            finish();
        }
    }


}