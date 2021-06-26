package com.example.typicalfood.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Pojo.UserPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class ViewModelFavorites extends ViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private List<FavoritosPlatos> platosList  = new ArrayList<>();
    private List<FavoritosPlatos> listPlate  = new ArrayList<>();
    private String city;
    private int posicion;
    private List<String> lista = new ArrayList<>();

    private MutableLiveData<List<FavoritosPlatos>> favPlatos;
    private MutableLiveData<List<DocumentSnapshot>> documentList;
    private MutableLiveData<List<DocumentSnapshot>> listUser;
    private MutableLiveData<List<String>> translate;

    public MutableLiveData<List<String>> getTranslate() {
        return translate;
    }

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
        translate = new MutableLiveData<>();

            existPlate();
            searchDish();
            serachEmail();
            translateDescription();
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

                                getPlateFavorite(city, posicion);
                            }

                        }else{
                            getPlateFavorite(null, 0);
                        }
                    } else {
                        System.out.println("No existe favoritos");
                    }

                }

            });
        }
    }

    public void getPlateFavorite(String city, int posicion){

        if(city != null){
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
        }else{
            favPlatos.setValue(platosList);
        }

    }

    //Metodo que comprueba si existe la provincia a la que quiere añadir platos
    public void searchDish(){
        Task<QuerySnapshot> future = mFirestore.collection("Provincias").get();
        future.addOnSuccessListener(t->{
            documentList.setValue(t.getDocuments());
        });

    }

    //Metodo que comprueba si existe el email en la base de datos para poder recuperar la contraseña
    public void serachEmail(){
        Task<QuerySnapshot> future = mFirestore.collection("Users").get();
        future.addOnSuccessListener(t->{
            listUser.setValue(t.getDocuments());
        });
    }

    public void translateDescription(){

    }

}
