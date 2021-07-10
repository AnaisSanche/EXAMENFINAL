package com.example.examenfinal.service;

import com.example.examenfinal.model.pokemonClass;
import com.example.examenfinal.model.trainerClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface serviceI {

    //OBTENER ENTRENADOR
    @GET("N000337452")
    Call<trainerClass> getTrainer();

    //GUARDAR ENTRENADOR
    @POST("N000337452")
    Call<Void> POST(@Body trainerClass trainer);

    //GUARDAR POKEMON
    @POST("N000337452/crear")
    Call<Void> createPoke(@Body pokemonClass pokClass);

    //TRAER LISTA DE  POKEMONES
    @GET("N000337452")
    Call<List<pokemonClass>> getListPoke();

    //CPTURAR POKEMON
    @POST("entrenador/N000337452/pokemon")
    Call<pokemonClass> capturePoke(@Body pokemonClass capture);

    //LISTA DE POKEMONES CAPTURADOS
    @GET("N000337452/pokemones")
    Call<List<pokemonClass>> getLisPokeCapture();

    //DETALLE DE POKEMON
    @GET("pokemones/{ID}")
    Call<pokemonClass> getPokemon(@Path("ID") String id_poke);
}
