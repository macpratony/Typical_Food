package com.example.typicalfood.Provincia;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.typicalfood.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class ProvinciasFragment extends Fragment {

    private ArrayList<String> lista_provincias;
    private ListView lista_list;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;
//
//    private static final String TEXT = "text";
//
//    public static MenuFragment newInstance(String text) {
//        MenuFragment frag = new MenuFragment();
//
//        Bundle args = new Bundle();
//        args.putString(TEXT, text);
//        frag.setArguments(args);
//
//        return frag;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_provincias, container, false);

        //PASAR DEL ARCHIVO QUE CONTIENE EL LISTADO DE LAS PROVINCIAS A UN ARRAY
        String[] array_listado_provincias = getResources().getStringArray(R.array.provincias);
        lista_provincias = new ArrayList<>(Arrays.asList(array_listado_provincias));

        db = FirebaseFirestore.getInstance();

        lista_list = (ListView)v.findViewById(R.id.listadoProvincia);

        //Llamamos al layout personalizado que hemos creado con anterioridad "list_item_provincias"
        adapter = new ArrayAdapter<String>(v.getContext(),R.layout.list_item_provincias, lista_provincias);
        lista_list.setAdapter(adapter);
        lista_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String array = lista_provincias.get(position); //Guarda el nombre de la provincia que se esta pulsando
                Toast.makeText(getContext(),array, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}