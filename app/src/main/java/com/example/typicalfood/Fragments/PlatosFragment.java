package com.example.typicalfood.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.Interface.Interfaz;
import com.example.typicalfood.Adapter.AdapterPlatos;
import com.example.typicalfood.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class PlatosFragment extends Fragment {

     private RecyclerView recyclerView;
     private AdapterPlatos adapterPlatos;
     private Interfaz mInterfaz;
     private ArrayList<Platos> listaPlatos;
    private TextView title;
    private ImageButton button;
    private String provincia="";
    private Activity actividad;

    public PlatosFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_platos, container, false);

        title = v.findViewById(R.id.txtTitulo);
        button = v.findViewById(R.id.buttonRegresar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterfaz.regresar();
            }
        });
        recyclerView = v.findViewById(R.id.recyclerView);
        listaPlatos = new ArrayList<>();

        mostrarDatos();
        return v;
    }

    public void mostrarDatos() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bundle city = getArguments();
            if(city != null){
                listaPlatos = (ArrayList<Platos>) city.getSerializable("city");
                provincia = (String) city.getSerializable("provincia");
            }

            //Titulo que se añade en el fragment del listado de platos de la ciudad que se pulsa
            title.setText(provincia);
        adapterPlatos = new AdapterPlatos(getContext(),R.layout.item_platos_provincia,listaPlatos);
        recyclerView.setAdapter(adapterPlatos);

        adapterPlatos.setOnclickListener(view -> {
            //enviar mediante la interface el objeto seleccionado al detalle
            //se envia el objeto completo
            //se utiliza la interface como puente para enviar el objeto seleccionado
            mInterfaz.enviarPlatos(listaPlatos.get(recyclerView.getChildAdapterPosition(view)), provincia);
            //luego en el mainactivity se hace la implementacion de la interface para implementar el metodo enviarpersona
        });


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.actividad = (Activity)context;
            mInterfaz = (Interfaz) this.actividad;
        } else {
            throw new RuntimeException();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
}