package com.example.typicalfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.typicalfood.ViewModel.ViewModelFavorites;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mResetPassword;
    private Button mButtonResetPassword;
    private Button mButtonReturn;
    private TextView mTextViewRespuesta;

    private String email;
    private String massage;

    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;

    private ViewModelFavorites viewModel;
    private List<DocumentSnapshot> listUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(ViewModelFavorites.class);

        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        mResetPassword = findViewById(R.id.editextRecuperarContrasena);
        mButtonResetPassword = findViewById(R.id.btnResetPassword);
        mTextViewRespuesta = findViewById(R.id.mensaje);
        mButtonReturn = findViewById(R.id.btnReturn);

        btnReset();
        btnReturn();


    }

    public void btnReset(){
        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnResetPassword();
            }
        });
    }

    public void btnReturn(){
        mButtonReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AutenticacionActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Metodo del click del boton de recuperar contraseña
    public void btnResetPassword(){
        viewModel.getListUser().observe(this, new Observer<List<DocumentSnapshot>>(){
                    @Override
                    public void onChanged(List<DocumentSnapshot> documentSnapshots) {
                        listUsers = documentSnapshots;
                        email = mResetPassword.getText().toString().trim();
                        List<Object> listEmail = new ArrayList<>();

                        for(int i = 0; i<listUsers.size(); i++ ){
                            Map<String, Object> idUser = listUsers.get(i).getData();
                            for (Map.Entry<String, Object> entrada : idUser.entrySet()) {
                                String key = entrada.getKey();
                                if(key.equals("email")){
                                    listEmail.add(entrada.getValue());
                                }
                            }
                        }
                        if(!email.isEmpty()){
                            if(listEmail.contains(email)){
                                mProgressBar.setVisibility(View.VISIBLE);
                                mResetPassword.setVisibility(View.GONE);
                                mButtonResetPassword.setVisibility(View.GONE);
                                mTextViewRespuesta.setVisibility(View.GONE);
                                mButtonReturn.setVisibility(View.GONE);
                                ressetPassword();
                            }else{
                                massage = getString(R.string.mensaje_email);
                                mTextViewRespuesta.setText(massage);
                            }
                        }else{
                            massage = getString(R.string.mensaje_email2);
                            mTextViewRespuesta.setText(massage);
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
                    mTextViewRespuesta.setText("");
                    mResetPassword.setVisibility(View.VISIBLE);
                    mButtonResetPassword.setVisibility(View.VISIBLE);
                    mButtonReturn.setVisibility(View.VISIBLE);
                    Intent i = new Intent(getApplicationContext(), AutenticacionActivity.class);
                    startActivity(i);
                    finish();
                    massage = getString(R.string.restablecer_contrasena);
                    Toast.makeText(ResetPasswordActivity.this, massage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}