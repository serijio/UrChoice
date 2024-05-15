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

    @GET("user")
    Call<List<User>> getUsers();
    @GET("/users/{id_user}")
    Call<User> getUser(@Path("id_user") int userId);
}
