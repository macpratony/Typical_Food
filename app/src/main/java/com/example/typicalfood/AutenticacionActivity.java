package com.example.typicalfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AutenticacionActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApi googleApi;
    private SignInButton signInButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private EditText editTextEmail;
    private EditText editTextContrasena;
    private Button btnLogin;
    private TextView mTextViewRespuesta;
    private TextView mRecuperarContrasena;

    private String email = "";
    private String password = "";


    private static final int SIGN_IN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText)findViewById(R.id.emailEditext);
        editTextContrasena = (EditText)findViewById(R.id.contrasenaEditext);
        mTextViewRespuesta = (TextView) findViewById(R.id.textViewRespuesta);
        mRecuperarContrasena = (TextView) findViewById(R.id.recuperarContrasena);

        mRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AutenticacionActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin = (Button)findViewById(R.id.btnAcceder);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = editTextEmail.getText().toString().trim();
                password = editTextContrasena.getText().toString().trim();

                if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(AutenticacionActivity.this, "Introduzca email y contraseña", Toast.LENGTH_SHORT).show();
                }else if(!email.isEmpty() && password.isEmpty()){
                    Toast.makeText(AutenticacionActivity.this, "Introduzca una contraseña", Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty() && !password.isEmpty()){
                    Toast.makeText(AutenticacionActivity.this, "Introduzca un email", Toast.LENGTH_SHORT).show();
                }else{

                    loginUser();
                }
            }
        });
    }

    //METODO QUE VERIFICA EL ACCESO MEDIANTE USUARIO Y CONTRASEÑA
    public void loginUser(){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               // FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                               // Toast.makeText(getApplicationContext(), "Correo o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                                mTextViewRespuesta.setText("Correo o contraseña incorrecto");
                            }
                        }
                    });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        //PREGUNTAMOS SI EL USUARIO YA SE HA REGISTRADO SI ES ASI AL ABRIR LA APP LO LLEVA A ELEGIR UNA PROVINCIA
//        if(mAuth.getCurrentUser() != null){
//            startActivity(new Intent(AutenticacionActivity.this, ProvinciasActivity.class));
//            finish();
//        }
//    }

    /*metodo que permite recuperar informacion de la base de datos
     private DatabaseReference mDatabase;
     mDatabase = FirebaseDatabase.getInstance().getReference();

    private void getInfoUser(){
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/
}