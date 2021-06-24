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
import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AutenticacionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextEmail;
    private EditText editTextContrasena;
    private Button btnLogin;
    private Button btnCancel;
    private TextView mTextViewRespuesta;
    private TextView mRecuperarContrasena;

    private String email = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.emailEditext);
        editTextContrasena = findViewById(R.id.contrasenaEditext);
        mTextViewRespuesta =  findViewById(R.id.textViewRespuesta);

        mRecuperarContrasena = findViewById(R.id.recuperarContrasena);
        btnLogin = findViewById(R.id.btnAcceder);
        btnCancel = findViewById(R.id.btnCancel);

        recoverPassword();
        btnLogin();
        btnCancel();

    }

    public void btnLogin(){
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

    public void recoverPassword(){
        mRecuperarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AutenticacionActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    public void btnCancel(){
        btnCancel.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScreenMainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //METODO QUE VERIFICA EL ACCESO MEDIANTE USUARIO Y CONTRASEÑA
    public void loginUser(){
        if(email.equals("marcoaph29@gmail.com") && password.equals("Administrador")){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                mTextViewRespuesta.setText("Correo o contraseña incorrecto");
                            }
                        }
                    });

        }else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                mTextViewRespuesta.setText("Correo o contraseña incorrecto");
                            }
                        }
                    });
        }
    }

}