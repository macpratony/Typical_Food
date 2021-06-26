package com.example.typicalfood.Administrador;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.example.typicalfood.R;
import com.example.typicalfood.ViewModel.ViewModelFavorites;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private ViewModelFavorites viewModel;
    private List<DocumentSnapshot> listDocument;

    private String nombreProvincia;
    private String nombrePlato;
    private String descrip;
    private AdministradorFragment admin;
    private FragmentTransaction fragmentTransaction;
    private ProgressBar mProgressBar;

    private String message;

    private Uri keyImage;
    private static final int GALLERY_INTENT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_agregar_platos_admin, container, false);

        provincia = view.findViewById(R.id.nombreProvincia);
        namePlato = view.findViewById(R.id.nombrePlato);
        descripcion = view.findViewById(R.id.descripcionPlato);
        imageViewPlato = view.findViewById(R.id.fotoPlato);
        enviarPlato = view.findViewById(R.id.btnSubirPlato);
        cancelarPlato = view.findViewById(R.id.btncancelarPlato);
        mProgressBar = view.findViewById(R.id.progressBarLoad);
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);

        initialize();
        savePlateFirebase();
        cancelSavePlateFirebase();
        loadPhoto();

        return view;
    }

    public void initialize(){
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        viewModel = new ViewModelProvider(this).get(ViewModelFavorites.class);

    }

    //Pulsar en la imagen para cargar desde el dispositivo
    public void loadPhoto(){
        imageViewPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }

    //Boton guardar plato en Firebase
    public void savePlateFirebase(){
        enviarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreProvincia = provincia.getText().toString();
                nombrePlato = namePlato.getText().toString();
                descrip = descripcion.getText().toString();

                Map<String, Object> map = new HashMap<>();
                map.put("titulo", nombrePlato.toLowerCase());
                map.put("descripcion", descrip);
                map.put("foto", String.valueOf(keyImage));

                if(nombreProvincia.equals("") || nombrePlato.equals("") || descrip.equals("") || keyImage == null){
                    message = getString(R.string.admin1);
                    Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                }else{
                    viewModel.getDocumentList().observe(getViewLifecycleOwner(), new Observer<List<DocumentSnapshot>>() {
                        @Override
                        public void onChanged(List<DocumentSnapshot> documentSnapshots) {
                            listDocument = documentSnapshots;
                            List<String> listaProvincia = new ArrayList<>();

                            for(int i = 0; i<listDocument.size(); i++){
                                String pro = listDocument.get(i).getId();
                                listaProvincia.add(pro);
                            }

                            if(listaProvincia.contains(nombreProvincia)){
                                savePlato( map);

                                admin = new AdministradorFragment();
                                fragmentTransaction = getParentFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.home_content, admin);
                                fragmentTransaction.commit();
                                message = getString(R.string.admin2);
                                Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                            }else{
                                message = getString(R.string.admin3);
                                Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //Guarda en la base de datos una vez que se está seguro que existe en la base de datos
    public void savePlato(Map<String, Object> map){
        DocumentReference mReference = mFirestore.collection("Provincias").document(nombreProvincia);
        mReference.update("platos", FieldValue.arrayUnion(map));
    }

    //Boton cancelar subida de plato a firebase
    public void cancelSavePlateFirebase(){
        cancelarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                admin = new AdministradorFragment();
                fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.home_content, admin);
                fragmentTransaction.commit();

            }
        });
    }

    //Metodo que se encarga de cargar la imagen desde el dispositivo subirlo a firebase y lyego retornar la uri de la foto
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT){
            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("Fotos");
            final StorageReference fileName = filePath.child("file"+uri.getLastPathSegment());
            mProgressBar.setVisibility(View.VISIBLE);
            enviarPlato.setVisibility(View.INVISIBLE);
            cancelarPlato.setVisibility(View.INVISIBLE);
                fileName.putFile(uri).addOnSuccessListener(taskSnapshot -> fileName.getDownloadUrl().addOnSuccessListener(uri1 -> {

                    Uri descargarFoto = uri1;
                    keyImage = uri1;

                    if(descargarFoto != null){
                        //Muestra la foto cargada en firebase
                        Glide.with(getContext())
                                .load(descargarFoto)
                                .fitCenter()
                                .centerCrop()
                                .into(imageViewPlato);
                    }
                    mProgressBar.setVisibility(View.GONE);
                    enviarPlato.setVisibility(View.VISIBLE);
                    cancelarPlato.setVisibility(View.VISIBLE);


                }));


        }else{
            message = getString(R.string.admin4);
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

}