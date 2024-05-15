package com.example.urchoice2.API;

import com.example.urchoice2.Classes.Saved;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SavedAPI {
    @GET("saved/{id_user}")
    Call<List<Saved>> obtenerGuardados(@Path("id_user") int idUser);

    @FormUrlEncoded
    @POST("saved/insert")
    Call<Void> insertarSaved(@Field("id_user") int idUser, @Field("id_cat") int idCat);

    @DELETE("saved/fav/delete/{id_user}/{id_cat}")
    Call<Void> anadirFav(@Path("id_user") int idUser, @Path("id_cat") int idCat);

    @DELETE("saved/delete/{id_user}/{id_cat}")
    Call<Void> eliminarSaved(@Path("id_user") int idUser, @Path("id_cat") int idCat);
}