package com.example.urchoice2.API;

import com.example.urchoice2.Classes.VoteCount;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RoomGameAPI {

    @FormUrlEncoded
    @POST("roomgame/create")
    Call<Void> createRoomGame(@Field("id_room") int roomId, @Field("id_user") int userId);


    @FormUrlEncoded
    @POST("/room/updateVote")
    Call<Void> updateVote(
            @Field("id_room") int idRoom,
            @Field("id_user") int idUser,
            @Field("vote_game") String voteGame
    );
}
