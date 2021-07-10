package com.example.examenfinal.view.poke;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.examenfinal.R;
import com.example.examenfinal.model.pokemonClass;
import com.example.examenfinal.service.serviceI;
import com.example.examenfinal.view.trainer.trainerActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class createPokeActivity extends AppCompatActivity {

    ImageView image;
    EditText name, type, latitude, longitude;

    Button registrar;

    Uri uri;
    String imageS;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poke);

        image = findViewById(R.id.imageF);
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        registrar = findViewById(R.id.registrar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk/pokemons/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceI service = retrofit.create(serviceI.class);

        registrar.setOnClickListener(v -> {
            String nombre = name.getEditableText().toString().trim();
            String tipo = type.getEditableText().toString().trim();
            String lat = latitude.getEditableText().toString().trim();
            String lon = longitude.getEditableText().toString().trim();

            pokemonClass poke = new pokemonClass(nombre, tipo, imageS, Float.parseFloat(lat), Float.parseFloat(lon));
            Call<Void> entre = service.createPoke(poke);
            entre.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    String respuesta = String.valueOf(response.code());
                    if (respuesta.equals("200")) {

                        Intent intent = new Intent(createPokeActivity.this, trainerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERRO INTENTAR DE NUEVO", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        image.setOnClickListener(v -> addImage());
    }

    private void addImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            uri = data.getData();
            image.setImageURI(uri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] image = outputStream.toByteArray();
                imageS = Base64.encodeToString(image, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}