package com.example.typicalfood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.typicalfood.Entity.FavoritosPlatos;
import com.example.typicalfood.Entity.Platos;
import com.example.typicalfood.R;

import java.util.ArrayList;

public class AdapterFavorito extends RecyclerView.Adapter<AdapterFavorito.ViewHolder> implements View.OnClickListener {

    private Context context;
    private int layout;
    private ArrayList<FavoritosPlatos> platosList;
    private CardView cardView;

    private View.OnClickListener listener;


    public AdapterFavorito(@NonNull Context context, int resource, @NonNull ArrayList<FavoritosPlatos> platosList){
        //super(context, resource, platosList);
        this.context = context;
        layout = resource;
        this.platosList = platosList;
    }


    @NonNull
    @Override
    public AdapterFavorito.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new AdapterFavorito.ViewHolder(view);
    }


    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorito.ViewHolder holder, int position) {
        String title = platosList.get(position).getTitulo();
        String photo = platosList.get(position).getFoto();
        String descripcion = platosList.get(position).getDescripcion();

        holder.textViewTitulo.setText(title.toUpperCase());
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
        private TextView textViewTitulo;
        private ImageView imageViewFoto;
        private CardView cardView;
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
