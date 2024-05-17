package com.example.urchoice2.API;

import com.example.urchoice2.Classes.User;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FriendsAPI {
    @FormUrlEncoded
    @PUT("friends/update")
    Call<Void> updateFriendRelation(
            @Field("id_us1") int id_us1,
            @Field("id_us2") int id_us2,
            @Field("nuevoEstado") String nuevoEstado
    );

    @GET("/friends/count/{id_user}")
    Call<JsonObject> getFriendCount(@Path("id_user") int userId);

    @GET("/friends/{id_user}")
    Call<List<User>> getFriends(@Path("id_user") int userId);

    @GET("/friends/request/{id_user}")
    Call<List<User>> getRequests(@Path("id_user") int userId);

    @FormUrlEncoded
    @PUT("/friends/update")
    Call<Void> updateFriendStatus(
            @Field("id_us1") int idUs1,
            @Field("id_us2") int idUs2,
            @Field("nuevoEstado") String nuevoEstado
    );

    @FormUrlEncoded
    @POST("friends")
    Call<Void> addFriend(@Field("id_us1") int id_us1, @Field("nick_name") String nick_name);
}