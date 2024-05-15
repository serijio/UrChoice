package com.example.urchoice2.API;

import com.example.urchoice2.Classes.Favs;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavsAPI {
    @GET("favoritos/{id_user}")
    Call<List<Favs>> obtenerFavoritos(@Path("id_user") int id_user);

    @FormUrlEncoded
    @POST("fav/insert")
    Call<Void> insertarFavorito(@Field("id_user") int idUser, @Field("id_cat") int idCat);

    @DELETE("fav/delete/{id_user}/{id_cat}")
    Call<Void> eliminarFavorito(@Path("id_user") int idUser, @Path("id_cat") int idCat);
}