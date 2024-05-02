package com.example.urchoice2.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RoomAPI {

    @FormUrlEncoded
    @POST("room/create")
    Call<Void> createRoom(@Field("id_cat") int categoryId, @Field("id_user") int userId);

    @FormUrlEncoded
    @POST("room/start")
    Call<Void> startRoom(@Field("id_room") int roomId, @Field("id_user") int userId);

    @FormUrlEncoded
    @POST("room/end")
    Call<Void> endRoom(@Field("id_room") int roomId, @Field("id_user") int userId);

}
