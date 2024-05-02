package com.example.urchoice2.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ElemCatAPI {


    @FormUrlEncoded
    @POST("elemcat/update")
    Call<Void> updateElemCat(
            @Field("id_elem") int idElem,
            @Field("id_cat") int idCat,
            @Field("victories") int victories
    );
}
