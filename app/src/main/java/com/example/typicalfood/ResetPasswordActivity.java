package com.example.typicalfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.concurrent.Executors;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mResetPassword;
    private Button mButtonResetPassword;

    private String email = "";

    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        mResetPassword = (EditText) findViewById(R.id.editextRecuperarContrasena);
        mButtonResetPassword = (Button) findViewById(R.id.btnResetPassword);

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mResetPassword.getText().toString().trim();
                if(!email.isEmpty()){
                    mProgressBar.setVisibility(View.VISIBLE);
                    mResetPassword.setVisibility(View.GONE);
                    mButtonResetPassword.setVisibility(View.GONE);
                    ressetPassword();
                }else{
                    Toast.makeText(ResetPasswordActivity.this, "El campo email está vacio", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void ressetPassword(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mProgressBar.setVisibility(View.GONE);
                    mResetPassword.setVisibility(View.VISIBLE);
                    mButtonResetPassword.setVisibility(View.VISIBLE);
                    Toast.makeText(ResetPasswordActivity.this, "Se ha enviado un correo para restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }else{
                    mProgressBar.setVisibility(View.GONE);
                    mResetPassword.setVisibility(View.VISIBLE);
                    mButtonResetPassword.setVisibility(View.VISIBLE);
                    Toast.makeText(ResetPasswordActivity.this, "El correo no consta en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
   /* private void closeTeclado(){
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }*/

}