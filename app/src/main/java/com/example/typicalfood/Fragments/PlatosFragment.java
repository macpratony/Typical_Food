package com.example.typicalfood.Fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.typicalfood.Base.BaseInterfazFragment;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.Adapter.AdapterPlatos;
import com.example.typicalfood.R;
import com.example.typicalfood.Utils.UIUtils;

import java.util.ArrayList;


public class PlatosFragment extends BaseInterfazFragment {

     public RecyclerView recyclerView;
     private AdapterPlatos adapterPlatos;
     private ArrayList<Platos> listaPlatos;
    private TextView title;
    private ImageButton button;
    private String provincia;

    public ProgressBar mProgressBar;

    public PlatosFragment() {}

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_platos, container, false);

        title = v.findViewById(R.id.txtTitulo);
        button = v.findViewById(R.id.buttonRegresar);
        recyclerView = v.findViewById(R.id.recyclerView);
        mProgressBar = v.findViewById(R.id.progressBar2);
        UIUtils.styleProgressBar(mProgressBar);
        listaPlatos = new ArrayList<>();

        try {
            mostrarDatos();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btnRegresar();

        return v;
    }

    public void btnRegresar(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterfaz.regresar();
            }
        });
    }

    public void mostrarDatos() throws InterruptedException {
        mProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            Bundle city = getArguments();
            if(city != null){
                listaPlatos = (ArrayList<Platos>) city.getSerializable("city");
                provincia = (String) city.getSerializable("provincia");

            title.setText(provincia);
            adapterPlatos = new AdapterPlatos(getContext(), R.layout.item_platos_provincia, listaPlatos);
            recyclerView.setAdapter(adapterPlatos);

            //Titulo que se añade en el fragment del listado de platos de la ciudad que se pulsa

            adapterPlatos.setOnclickListener(view -> {
                mInterfaz.enviarPlatos(listaPlatos.get(recyclerView.getChildAdapterPosition(view)), provincia);

            });
        }
        mProgressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
