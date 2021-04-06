package com.example.typicalfood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class ProvinciasActivity extends AppCompatActivity {

    private ArrayList<String> lista_provincias;
    private ListView lista_list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provincias);

        //PASAR DEL ARCHIVO QUE CONTIENE EL LISTADO DE LAS PROVINCIAS A UN ARRAY
        String[] array_listado_provincias = getResources().getStringArray(R.array.provincias);
        lista_provincias = new ArrayList<>(Arrays.asList(array_listado_provincias));

        lista_list = (ListView)findViewById(R.id.listadoProvincia);

        //Llamamos al layout personalizado que hemos creado con anterioridad "list_item_provincias"
        adapter = new ArrayAdapter<>(this,R.layout.list_item_provincias, lista_provincias);
        lista_list.setAdapter(adapter);
    }
}