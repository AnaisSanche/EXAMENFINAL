package com.example.examenfinal.view.trainer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.examenfinal.R;
import com.example.examenfinal.model.trainerClass;
import com.example.examenfinal.service.serviceI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class trainerCreateActivity extends AppCompatActivity {

    EditText name, town;
    ImageView image;
    static final int IMAGE_CAPTURE = 1;
    private static final int IMAGE = 2;
    String imageS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_create);

        name = findViewById(R.id.name);
        town = findViewById(R.id.town);
        image = findViewById(R.id.imageEn);

        checkPERMISSION();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk/entrenador/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceI service = retrofit.create(serviceI.class);


        findViewById(R.id.registerTrainer).setOnClickListener(v -> {
            String nombre = name.getEditableText().toString();
            String pubelo = town.getEditableText().toString();

            trainerClass aClass = new trainerClass(nombre, pubelo, imageS);
            Call<Void> regis = service.POST(aClass);
            regis.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    String resp = String.valueOf(response.code());
                    if (resp.equals("200")) {
                        Intent intent = new Intent(getApplicationContext(), trainerActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });

        });
        image.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(trainerCreateActivity.this);

            builder.setMessage("Elegir")
                    .setPositiveButton("Galeria", (dialog, id) -> geleria())
                    .setNegativeButton("Camara", (dialog, id) -> camara());


            AlertDialog dialogBu = builder.create();
            dialogBu.show();
        });

    }

    private void geleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    private void camara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imagen = stream.toByteArray();
            imageS = Base64.encodeToString(imagen, Base64.DEFAULT);
            Log.e("codigo de camra ", imageS);

        }
        //Mostrando imagen desde galeria
        if (resultCode == RESULT_OK && requestCode == IMAGE) {
            Uri img = data.getData();
            image.setImageURI(img);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), img);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] image = outputStream.toByteArray();
                String encodedString = Base64.encodeToString(image, Base64.DEFAULT);
                imageS = encodedString;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void checkPERMISSION() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(trainerCreateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }
    }
}