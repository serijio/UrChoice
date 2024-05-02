package com.example.urchoice2.API;

import com.example.urchoice2.Classes.Element;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ElementsAPI {


    @GET("/elements/ranking/{categoryId}")
    Call<List<Element>> getRanking(@Path("categoryId") int categoryId);

    @GET("/elements/{categoryId}")
    Call<List<Element>> getElementsByCategory(@Path("categoryId") int categoryId);
}
