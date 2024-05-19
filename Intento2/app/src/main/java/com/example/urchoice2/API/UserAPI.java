package com.example.urchoice2.API;

import com.example.urchoice2.Classes.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserAPI {
    @FormUrlEncoded
    @POST("user/register")
    Call<User> registerUser(
            @Field("email") String email,
            @Field("nick") String nick,
            @Field("img") String img,
            @Field("contra") String contra
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<User> loginUser(
            @Field("email") String email,
            @Field("contra") String contra
    );

    @GET("/users/all/{id_user}")
    Call<List<User>> getUsers(@Path("id_user") int userId);


    @GET("/users/{id_user}")
    Call<User> getUser(@Path("id_user") int userId);

    @FormUrlEncoded
    @POST("/user/UpdateName")
    Call<Void> updateUserName(
            @Field("user_id") int user_id,
            @Field("nick_user") String new_nick_user
    );

    @FormUrlEncoded
    @POST("/user/UpdateIMG")
    Call<Void> updateUserIMG(
            @Field("user_id") int user_id,
            @Field("img_user") String img_user
    );

    @FormUrlEncoded
    @POST("/app/end")
    Call<Void> endAPP(@Field("id_user") int userId);


}