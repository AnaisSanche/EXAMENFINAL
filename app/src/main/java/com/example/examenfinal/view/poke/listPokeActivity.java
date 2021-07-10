package com.example.examenfinal.view.poke;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.examenfinal.R;
import com.example.examenfinal.adapter.adapterPoke;
import com.example.examenfinal.model.pokemonClass;
import com.example.examenfinal.service.serviceI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class listPokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_poke);

        RecyclerView recyclerView = findViewById(R.id.recy_id);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upn.lumenes.tk/pokemons/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceI service = retrofit.create(serviceI.class);
        Call<List<pokemonClass>> listGet = service.getListPoke();
        getList(recyclerView, listGet);
    }

    private void getList(RecyclerView recyclerView, Call<List<pokemonClass>> listGet) {
        listGet.enqueue(new Callback<List<pokemonClass>>() {
            @Override
            public void onResponse(Call<List<pokemonClass>> call, Response<List<pokemonClass>> response) {
                String code = String.valueOf(response.code());
                if (code.equals("200")) {

                    List<pokemonClass> myList = response.body();
                    adapterPoke adapterList = new adapterPoke(myList, listPokeActivity.this);
                    recyclerView.setAdapter(adapterList);
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<pokemonClass>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}