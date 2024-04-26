package com.example.urchoice2.API;

import com.example.urchoice2.Classes.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoriesAPI {

    @GET("categories")
    Call<List<Category>> getCategories();
}
