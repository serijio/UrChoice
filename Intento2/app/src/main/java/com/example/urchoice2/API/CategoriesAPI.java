package com.example.urchoice2.API;

import com.example.urchoice2.Classes.Category;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoriesAPI {
    @POST("/categories/create")
    Call<Void> createCategory(@Body RequestBody requestBody);

    @POST("/categories/update")
    Call<Void> updateCategory(@Body RequestBody requestBody);

    @GET("/categories/{id_user}")
    Call<List<Category>> getCategories(@Path("id_user") int userId);

    @DELETE("categories/{id_cat}")
    Call<Void> deleteCategory(@Path("id_cat") int idCat);

    @GET("/category/{id}")
    Call<Category> getCategory(@Path("id") int categoryId);

    @GET("/categories/all")
    Call<List<Category>> getAllCategories();

    @GET("categories/mine/{user_id}")
    Call<List<Category>> getCategoriesByUserId(@Path("user_id") int userId);
}