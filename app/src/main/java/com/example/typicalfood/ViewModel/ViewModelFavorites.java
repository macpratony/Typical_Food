package com.example.typicalfood.ViewModel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.typicalfood.Entity.FavoritosPlatos;

import com.example.typicalfood.Pojo.UserPojo;
;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class ViewModelFavorites extends ViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private List<FavoritosPlatos> platosList  = new ArrayList<>();
    private List<FavoritosPlatos> listPlate  = new ArrayList<>();
    private String city;
    private int posicion;
    boolean exist = false;

    private MutableLiveData<List<FavoritosPlatos>> favPlatos;
    private MutableLiveData<List<DocumentSnapshot>> documentList;
    private MutableLiveData<List<DocumentSnapshot>> listUser;

    public MutableLiveData<List<DocumentSnapshot>> getListUser() {
        return listUser;
    }

    public MutableLiveData<List<DocumentSnapshot>> getDocumentList() {
        return documentList;
    }
    public LiveData<List<FavoritosPlatos>> getFavPlatos() {

        return favPlatos;
    }

    public ViewModelFavorites(){
        favPlatos = new MutableLiveData<>();
        documentList = new MutableLiveData<>();
        listUser = new MutableLiveData<>();

            existPlate();
            searchDish();
            serachEmail();
    }



    public void existPlate() {
        if (mAuth.getCurrentUser() != null){
            mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        UserPojo user = documentSnapshot.toObject(UserPojo.class);
                        List<DocumentReference> ref = user.getFavorites();

                        if (ref.size() > 0) {
                            for (int i = 0; i < ref.size(); i++) {
                                String r = ref.get(i).getPath();
                                String[] prueba = r.split("/");
                                city = prueba[1];
                                posicion = Integer.parseInt(prueba[3]);

                                //Provincias/Albacete/platos/0
                                //El array prueba contiene el texto de arriba separado por / siendo provincia la posicion 0 , etc
                                //Enviamos por parametro la provincia que corresponde a la posicion 1 y la posicion del plato que corresponde a la posicion 3 del array
                                //Como es un string se trasforma a Integer
                                getPlateFavorite(city, posicion);
                            }

                        }
                    } else {
                        System.out.println("No existe favoritos");
                    }

                }

            });
        }
    }

    public void getPlateFavorite(String city, int posicion){

        mFirestore.collection("Provincias").document(city).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ArrayList lista = (ArrayList) documentSnapshot.getData().get("platos");

                    Map<String, String> map = (Map<String,String>) lista.get(posicion);
                    String title =  map.get("titulo");
                    String description = map.get("descripcion");
                    String photo = map.get("foto");

                    platosList.add(new FavoritosPlatos(title,photo,description,city));
                    favPlatos.setValue(platosList);
                }else{
                    System.out.println("No existe nada en el documento");
                }
            }
        });

    }

    //Metodo que comprueba si existe la provincia a la que quiere añadir platos
    public void searchDish(){
        Task<QuerySnapshot> future = mFirestore.collection("Provincias").get();
        future.addOnSuccessListener(t->{
            documentList.setValue(t.getDocuments());
            //List<DocumentSnapshot> list = t.getDocuments();
        });

    }

    //Metodo que comprueba si existe el email en la base de datos para poder recuperar la contraseña
    public void serachEmail(){
        Task<QuerySnapshot> future = mFirestore.collection("Users").get();
        future.addOnSuccessListener(t->{
            listUser.setValue(t.getDocuments());
            //List<DocumentSnapshot> list = t.getDocuments();
        });
    }

}
