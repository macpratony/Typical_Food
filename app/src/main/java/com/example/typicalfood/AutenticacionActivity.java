package com.example.typicalfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.typicalfood.Administrador.AdministradorFragment;
import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
        if(email.equals("marcoaph29@gmail.com") && password.equals("Administrador")){
            Intent i = new Intent(getApplicationContext(), AdministradorFragment.class);
            startActivity(i);
        }else {
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
    }

}