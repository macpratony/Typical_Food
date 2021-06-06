package com.example.typicalfood.ViewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.typicalfood.Adapter.AdapterFavorito;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.PlatosFavoritos.FavoritosFragment;
import com.example.typicalfood.Pojo.UserPojo;
import com.example.typicalfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class ViewModelFavorites extends ViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private List<FavoritosPlatos> platosList ;
    private ArrayList<FavoritosPlatos> listPlate;
    private String city;
    private int posicion;

    private MutableLiveData<List<FavoritosPlatos>> favPlatos;

    public ViewModelFavorites(MutableLiveData<List<FavoritosPlatos>> favPlatos) {
        this.favPlatos = favPlatos;
    }

    public void setPlatosFavoritos(List<FavoritosPlatos> favorite){
        favPlatos.setValue(favorite);
    }

    public LiveData<List<FavoritosPlatos>> getFavPlatos() {
        if(favPlatos == null){
            favPlatos = new MutableLiveData<List<FavoritosPlatos>>();
            //loadFavPlatos();
        }
        return favPlatos;
    }

    private void  loadFavPlatos() {
        platosList = new ArrayList<>();
        existPlate();
        platosList = getPlateFavorite();

        favPlatos.setValue(platosList);

    }
    public void existPlate(){

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserPojo user = documentSnapshot.toObject(UserPojo.class);
                    List<DocumentReference> ref = user.getFavorites();

                    if(ref.size() > 0){
                        for (int i= 0; i < ref.size(); i++){
                            String r = ref.get(i).getPath();
                            String[] prueba = r.split("/");
                            for(int x=0; x<prueba.length; x++){
                                city = prueba[1];
                                posicion = Integer.parseInt(prueba[3]);
                                //Provincias/Albacete/platos/0
                                //El array prueba contiene el texto de arriba separado por / siendo provincia la posicion 0 , etc
                                //Enviamos por parametro la provincia que corresponde a la posicion 1 y la posicion del plato que corresponde a la posicion 3 del array
                                //Como es un string se trasforma a Integer

                            }


                        }
                    }
                }else{
                    System.out.println("No existe favoritos");
                }

            }

        });


    }

    public List<FavoritosPlatos> getPlateFavorite(){

        mFirestore.collection("Provincias").document(city).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ArrayList lista = (ArrayList) documentSnapshot.getData().get("platos");

                    Map<String, String> map = (Map<String,String>) lista.get(posicion);
                    String title =  map.get("titulo");
                    String description = map.get("descripcion");
                    String photo = map.get("foto");
                    platosList.add(new FavoritosPlatos(title,description,photo));

                }else{
                    System.out.println("No existe nada en el documento");
                }
            }
        });

        return platosList;
    }

}
