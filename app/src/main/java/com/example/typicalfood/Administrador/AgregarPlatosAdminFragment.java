package com.example.typicalfood.Administrador;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.typicalfood.Main_Navigation_Drawer_Activity.NavigationDrawerActivity;
import com.example.typicalfood.R;

import com.example.typicalfood.RegistrarseActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class AgregarPlatosAdminFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private StorageReference mStorage;

    private EditText provincia;
    private EditText namePlato;
    private EditText descripcion;
    private ImageView imageViewPlato;
    private Button enviarPlato;
    private Button cancelarPlato;

    private Uri keyImage;
    private static final int GALLERY_INTENT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agregar_platos_admin, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        provincia = view.findViewById(R.id.nombreProvincia);
        namePlato = view.findViewById(R.id.nombrePlato);
        descripcion = view.findViewById(R.id.descripcionPlato);
        imageViewPlato = view.findViewById(R.id.fotoPlato);
        enviarPlato = view.findViewById(R.id.btnSubirPlato);
        cancelarPlato = view.findViewById(R.id.btncancelarPlato);

        cancelarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageViewPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String nombreProvincia = provincia.getText().toString();
        String nombrePlato = namePlato.getText().toString();
        String descrip = descripcion.getText().toString();

        if(requestCode == GALLERY_INTENT){
                Uri uri = data.getData();
                StorageReference filePath = mStorage.child("Fotos");
                final StorageReference fileName = filePath.child("file"+uri.getLastPathSegment());


                fileName.putFile(uri).addOnSuccessListener(taskSnapshot -> fileName.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("titulo", nombrePlato.toLowerCase());
                    map.put("descripcion", descrip);
                    map.put("foto", String.valueOf(uri1));
                    Uri descargarFoto = uri1;
                    keyImage = uri1;
                    
                    //Hay que obtener la ultima posicion del array y añadir el plato

                    Glide.with(getContext())
                            .load(descargarFoto)
                            .fitCenter()
                            .centerCrop()
                            .into(imageViewPlato);


                    mFirestore.collection("Provincias").document(nombreProvincia).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Plato creado correctamente", Toast.LENGTH_SHORT).show();


                        }
                    });

                }));

        }else{
            Toast.makeText(getContext(), "No se subio la foto ", Toast.LENGTH_SHORT).show();
        }


    }
}