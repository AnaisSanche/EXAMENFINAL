package com.example.examenfinal.view.poke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.examenfinal.MapsActivity;
import com.example.examenfinal.R;
import com.example.examenfinal.model.pokemonClass;
import com.example.examenfinal.service.serviceI;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class detailPokeActivity extends AppCompatActivity {

    ImageView image;
    EditText name, type;
    String latitudeP;
    String longitudeP;
    String namePoke;

    Button viewMap, capturePoke;
    String id_poke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_poke);

        image = findViewById(R.id.imageF);
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);

        id_poke = getIntent().getStringExtra("id");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceI service = retrofit.create(serviceI.class);
        Call<pokemonClass> getPokeDetail = service.getPokemon(id_poke);
        detailPoke(getPokeDetail);

        findViewById(R.id.capturePoke).setOnClickListener(v -> capturePo());
        findViewById(R.id.viewPokeMap).setOnClickListener(v -> {
            Intent intent = new Intent(detailPokeActivity.this, MapsActivity.class);
            intent.putExtra("latitude", latitudeP);
            intent.putExtra("longitude", longitudeP);
            intent.putExtra("name", namePoke);
            startActivity(intent);
        });
    }

    private void capturePo() {
        Retrofit POSpo = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceI capturePoke = POSpo.create(serviceI.class);
        pokemonClass pokemon = new pokemonClass();
        pokemon.setPokemon_id(id_poke);
        Call<pokemonClass> captureP = capturePoke.capturePoke(pokemon);
        captureP.enqueue(new Callback<pokemonClass>() {
            @Override
            public void onResponse(Call<pokemonClass> call, Response<pokemonClass> response) {
                String Res = String.valueOf(response.code());
                if (Res.equals("200")) {
                    startActivity(new Intent(detailPokeActivity.this, listCapturePokeActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR INTENTAR DE NUEVO", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<pokemonClass> call, Throwable t) {

            }
        });
    }

    private void detailPoke(Call<pokemonClass> getPokeDetail) {
        getPokeDetail.enqueue(new Callback<pokemonClass>() {
            @Override
            public void onResponse(Call<pokemonClass> call, Response<pokemonClass> response) {

                pokemonClass pokemonDe = response.body();
                assert pokemonDe != null;

                String imageURL = "https://upn.lumenes.tk" + pokemonDe.getUrl_imagen();
                Picasso.get()
                        .load(imageURL)
                        .into(image);
                name.setText(pokemonDe.getNombre());
                namePoke = pokemonDe.getNombre();

                type.setText(pokemonDe.getTipo());
                pokemonDe.setId(pokemonDe.getId());

                latitudeP = String.valueOf(pokemonDe.getLatitude());
                longitudeP = String.valueOf(pokemonDe.getLongitude());
            }

            @Override
            public void onFailure(Call<pokemonClass> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}