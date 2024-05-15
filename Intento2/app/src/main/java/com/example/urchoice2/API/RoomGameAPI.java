package com.example.urchoice2.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RoomGameAPI {
    @FormUrlEncoded
    @POST("roomgame/create")
    Call<Void> createRoomGame(@Field("id_room") int roomId, @Field("id_user") int userId);
}
