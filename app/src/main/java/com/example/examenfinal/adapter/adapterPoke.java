package com.example.examenfinal.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinal.R;
import com.example.examenfinal.model.pokemonClass;
import com.example.examenfinal.view.poke.detailPokeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterPoke extends RecyclerView.Adapter<adapterPoke.adapterPokeView> {


    List<pokemonClass> list;
    Context mContext;

    public adapterPoke(List<pokemonClass> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public adapterPokeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new adapterPokeView(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull adapterPoke.adapterPokeView holder, int position) {

        pokemonClass poke = list.get(position);

        holder.name.setText(poke.getNombre());
        holder.type.setText(poke.getTipo());

        String image = "https://upn.lumenes.tk" + poke.getUrl_imagen();

        Picasso.get()
                .load(image)
                .into(holder.image);

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, detailPokeActivity.class);
            intent.putExtra("id", String.valueOf(poke.getId()));
            mContext.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class adapterPokeView extends RecyclerView.ViewHolder {

        TextView name, type;
        CardView view;
        ImageView image;

        public adapterPokeView(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            view = itemView.findViewById(R.id.poke_ca);
            image = itemView.findViewById(R.id.image);
        }
    }
}
