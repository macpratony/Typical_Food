package com.example.typicalfood.Administrador;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.typicalfood.Interface.Interfaz;
import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;
import com.example.typicalfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginAdminFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Interfaz mInterfaz;

    private String email;
    private String name;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);

        inicializar();
        comprobateUser();

        return view;
    }

    public void inicializar(){

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void comprobateUser(){
        if(mAuth.getCurrentUser() != null){
            String id = mAuth.getCurrentUser().getUid();
            mFirestore.collection("Users").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        String nombre = documentSnapshot.getString("name");
                        String correo = documentSnapshot.getString("email");
                        mInterfaz.accesAdministrator(nombre,correo);
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

}