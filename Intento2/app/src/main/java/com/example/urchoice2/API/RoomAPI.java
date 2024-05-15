package com.example.urchoice2.API;

import com.example.urchoice2.Classes.Rooms;
import com.example.urchoice2.Classes.UserVote;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoomAPI {

    @FormUrlEncoded
    @POST("room/create")
    Call<Integer> createRoom(@Field("id_cat") int categoryId, @Field("id_user") int userId, @Field("nameRoom") String nameRoom, @Field("passRoom") String password);

    @FormUrlEncoded
    @POST("room/start")
    Call<Void> startRoom(@Field("id_room") int roomId, @Field("id_user") int userId);

    @FormUrlEncoded
    @POST("room/end")
    Call<Void> endRoom(@Field("id_room") int roomId, @Field("id_user") int userId);


    @GET("/room/{id_room}/users")
    Call<List<UserVote>> getUsersInRoom(@Path("id_room") int id_room);
    @GET("/rooms")
    Call<List<Rooms>> getRooms();

    @FormUrlEncoded
    @POST("/room/join")
    Call<Void> joinRoom(
            @Field("id_room") Integer roomId,
            @Field("id_user") Integer userId,
            @Field("password") String password
    );

    @GET("/room/WinnerRound/{id_room}")
    Call<JsonObject> getWinnerRound(@Path("id_room") int idRoom);

}
