package com.example.typicalfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.Fragments.DetallePlatoFragment;
import com.example.typicalfood.Fragments.ProvinciasFragment;
import com.example.typicalfood.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterPlatos extends RecyclerView.Adapter<AdapterPlatos.ViewHolder> implements View.OnClickListener {

    private Context context;
    private int layout;
    private ArrayList<Platos> platosList;

    private View.OnClickListener listener;

    public AdapterPlatos( @NonNull Context context, int resource, @NonNull ArrayList<Platos> platosList){
        //super(context, resource, platosList);
        this.context = context;
        layout = resource;
        this.platosList = platosList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view);
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       String title = platosList.get(position).getTitulo();
       String photo = platosList.get(position).getFoto();
       String descripcion = platosList.get(position).getDescripcion();

        holder.textViewTitulo.setText(title);
        Glide.with(context).load(photo).into(holder.imageViewFoto);//Transforma el enlace url en imagen

        holder.cardView.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return platosList.size();
    }

    @Override
    public void onClick(View v) {

        if(listener != null){
            listener.onClick(v);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitulo;
        ImageView imageViewFoto;
        CardView cardView;

        /*
         *Tomamos referencia de las id creada en item_platos_provincia.xml
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.titulo);
            imageViewFoto = itemView.findViewById(R.id.imagen_plato);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
