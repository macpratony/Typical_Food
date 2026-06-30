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
import com.example.typicalfood.R;

import java.util.ArrayList;

public abstract class BasePlatosAdapter<T> extends RecyclerView.Adapter<BasePlatosAdapter.ViewHolder> implements View.OnClickListener {

    protected Context context;
    protected int layout;
    protected ArrayList<T> platosList;

    private View.OnClickListener listener;

    public BasePlatosAdapter(@NonNull Context context, int resource, @NonNull ArrayList<T> platosList) {
        this.context = context;
        this.layout = resource;
        this.platosList = platosList;
    }

    protected abstract String getTitulo(T item);

    protected abstract String getFoto(T item);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view);
    }

    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = platosList.get(position);
        String title = getTitulo(item);
        String photo = getFoto(item);

        holder.textViewTitulo.setText(title.toUpperCase());
        Glide.with(context).load(photo).into(holder.imageViewFoto);

        holder.cardView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return platosList.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTitulo;
        public ImageView imageViewFoto;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.titulo);
            imageViewFoto = itemView.findViewById(R.id.imagen_plato);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
