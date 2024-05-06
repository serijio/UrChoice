package com.example.urchoice2.API;

import com.example.urchoice2.Classes.Category;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoriesAPI {

    @GET("categories")
    Call<List<Category>> getCategories();
    @POST("/categories/create")
    Call<Void> createCategory(@Body RequestBody requestBody);
}
