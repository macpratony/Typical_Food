package com.example.typicalfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrarseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseFirestore mFirestore;

    //VARIABLES DE LA PANTALLA DE REGISTRO
    private EditText ediTextNombre;
    private EditText ediTextEmail;
    private EditText ediTextContrasena;
    private EditText ediTextConfirmarContrasena;
    private Button btnCrearCuenta;
    private Button btnAtras;

    //VARIABLES DE DATOS QUE SE VAN A REGISTRAR Y GUARDAR EN LA BASE DE DATOS
    private String nombre = "";
    private String email = "";
    private String password = "";
    private String confirmarContrasena = "";

    private Pattern pattern;
    private Matcher mather;

    private List<DocumentReference> listReference = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        ediTextNombre = (EditText) findViewById(R.id.nombre);
        ediTextEmail = (EditText) findViewById(R.id.email);
        ediTextContrasena = (EditText) findViewById(R.id.contrasena);
        ediTextConfirmarContrasena = (EditText) findViewById(R.id.confirmarContrasena);

        btnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);
        btnAtras = (Button)findViewById(R.id.btnCancelar);

        initialize();
        btnCreateAccount();
        btnReturn();

    }

    //Inicializar variables
    public void initialize(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirestore = FirebaseFirestore.getInstance();

        // Patrón para validar el email
       pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }

    public void btnCreateAccount(){
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = ediTextNombre.getText().toString();
                email = ediTextEmail.getText().toString().trim();
                password = ediTextContrasena.getText().toString();
                confirmarContrasena = ediTextConfirmarContrasena.getText().toString();

                if(nombre.isEmpty() && email.isEmpty() && password.isEmpty()){
                    Toast.makeText(RegistrarseActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }else if(nombre.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    Toast.makeText(RegistrarseActivity.this, "Debe de ingresar un nombre", Toast.LENGTH_SHORT).show();
                }else if(!nombre.isEmpty() && email.isEmpty() && !password.isEmpty()){
                    Toast.makeText(RegistrarseActivity.this, "Ingrese un email válido", Toast.LENGTH_SHORT).show();
                }else if(!nombre.isEmpty() && !email.isEmpty() && password.isEmpty()){
                    Toast.makeText(RegistrarseActivity.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                }else if(!nombre.isEmpty() && email.isEmpty() && password.isEmpty()){
                    Toast.makeText(RegistrarseActivity.this, "Ingrese un email y una contraseña", Toast.LENGTH_SHORT).show();
                }else if(!nombre.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    if(password.length() >= 6){
                        if(password.equals(confirmarContrasena)){
                            mather = pattern.matcher(email);
                            if (mather.find() == true) {
                                registerUserCloudFirestore();
                            } else {
                                Toast.makeText(RegistrarseActivity.this, "El email introducido no es válido", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(RegistrarseActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegistrarseActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    //METODO DE CANCELAR REGRESAR A LA PANTALLA ANTERIOR
    public void btnReturn(){
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ScreenMainActivity.class);
                startActivity(i);
            }
        });
    }

    //METODO DE CREAR NUEVO USUARIO
    public void registerUserCloudFirestore(){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", nombre);
                    map.put("email", email);
                    map.put("password", password);
                    map.put("favorites", listReference);

                    String id = mAuth.getCurrentUser().getUid();

                    mFirestore.collection("Users").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegistrarseActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegistrarseActivity.this, NavigationDrawerActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener(){

                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrarseActivity.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(RegistrarseActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}