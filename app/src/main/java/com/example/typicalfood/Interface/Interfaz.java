package com.example.typicalfood.Interface;

import com.airbnb.lottie.LottieAnimationView;
import com.example.typicalfood.Adapter.AdapterPlatos;
import com.example.typicalfood.Entity.Platos;

public interface Interfaz {

   // public void getData(FirebaseFirestore db );

    public AdapterPlatos getAllUser(String ciudad);
    public void enviarPlatos(Platos platos);
    public void regresar();
    public boolean likeAnimation(LottieAnimationView imageView, int animation, boolean like);

}
