package com.example.urchoice2.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface FriendsAPI {


    @FormUrlEncoded
    @POST("friends")
    Call<Void> addFriend(@Field("id_us1") int id_us1, @Field("id_us2") int id_us2);

    @FormUrlEncoded
    @PUT("friends/update")
    Call<Void> updateFriendRelation(
            @Field("id_us1") int id_us1,
            @Field("id_us2") int id_us2,
            @Field("nuevoEstado") String nuevoEstado
    );
}
