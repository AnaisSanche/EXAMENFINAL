package com.example.examenfinal.view.trainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenfinal.R;
import com.example.examenfinal.model.trainerClass;
import com.example.examenfinal.service.serviceI;
import com.example.examenfinal.view.poke.createPokeActivity;
import com.example.examenfinal.view.poke.listCapturePokeActivity;
import com.example.examenfinal.view.poke.listPokeActivity;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class trainerActivity extends AppCompatActivity {

    Button registerTrainer, registerPoke, viewPoke, viewPokCap;
    TextView name, town;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer);

        name = findViewById(R.id.name);
        town = findViewById(R.id.town);
        image = findViewById(R.id.image);


        registerTrainer = findViewById(R.id.registerTrainer);
        registerTrainer.setVisibility(View.GONE);
        registerTrainer.setOnClickListener(v -> registerT());

        registerPoke = findViewById(R.id.registerPoke);
        registerPoke.setVisibility(View.GONE);

        viewPoke = findViewById(R.id.viewPoke);
        viewPoke.setVisibility(View.GONE);

        viewPokCap = findViewById(R.id.viewPokCap);
        viewPokCap.setVisibility(View.GONE);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk/entrenador/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceI service = retrofit.create(serviceI.class);
        Call<trainerClass> trainerGet = service.getTrainer();
        trainerG(trainerGet);

        registerPoke.setOnClickListener(v -> {
            startActivity(new Intent(trainerActivity.this, createPokeActivity.class));
        });
        viewPoke.setOnClickListener(v -> {
            startActivity(new Intent(trainerActivity.this, listPokeActivity.class));
        });
        viewPokCap.setOnClickListener(v -> {
            startActivity(new Intent(trainerActivity.this, listCapturePokeActivity.class));
        });
    }

    private void registerT() {
        startActivity(new Intent(trainerActivity.this, trainerCreateActivity.class));
    }

    private void trainerG(Call<trainerClass> trainerGet) {
        trainerGet.enqueue(new Callback<trainerClass>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<trainerClass> call, Response<trainerClass> response) {

                trainerClass trainer = response.body();
                assert trainer != null;

                if (trainer.getImagen() == null) {
                    registerTrainer.setVisibility(View.VISIBLE);
                } else {

                    registerPoke.setVisibility(View.VISIBLE);
                    viewPoke.setVisibility(View.VISIBLE);
                    viewPokCap.setVisibility(View.VISIBLE);

                    Picasso.get()
                            .load(trainer.getImagen())
                            .into(image);
                    name.setText("Nombre del Entrenador: " + trainer.getNombres());
                    town.setText("Pueblo del Entrenador: " + trainer.getPueblo());
                }
            }

            @Override
            public void onFailure(Call<trainerClass> call, Throwable t) {
                Toast.makeText(trainerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}