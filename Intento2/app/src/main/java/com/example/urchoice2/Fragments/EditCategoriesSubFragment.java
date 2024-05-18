package com.example.urchoice2.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditCategoriesSubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ID_CAT = "id_cat";
    private String mParam1;
    private String mParam2;
    private int idCat;

    private CategoriesAPI categoriesAPI;
    private ElementsAPI elementApi;

    private int userId;

    public EditCategoriesSubFragment() {}

    public static EditCategoriesSubFragment newInstance(int idCat) {
        EditCategoriesSubFragment fragment = new EditCategoriesSubFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_CAT, idCat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCat = getArguments().getInt(ARG_ID_CAT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f5___x_sub__fragment_profile__edit_categories, container, false);
        Log.e("SQL","DATOS: " + idCat);
        return view;
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        elementApi = retrofit.create(ElementsAPI.class);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
        GetCategories();
    }

    public void GetCategories(){
        elementApi.getElementsByCategory(idCat).enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elementList = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {
            }
        });
    }

    private void InsertCategory(String categoryName, String categoryImage, List<Element> elements){
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("id_cat", idCat);
            jsonRequest.put("name_cat", categoryName);
            jsonRequest.put("img_cat", categoryImage);
            jsonRequest.put("id_user", userId);

            JSONArray elementsArray = new JSONArray();
            for (Element element : elements) {
                JSONObject elementObject = new JSONObject();
                elementObject.put("name_elem", element.getName_elem());
                elementObject.put("img_elem", element.getImg_elem());
                elementObject.put("victories", element.getVictories());
                elementObject.put("id_cat", element.getId_cat());
                elementsArray.put(elementObject);
            }
            jsonRequest.put("elements", elementsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        Call<Void> call = categoriesAPI.updateCategory(requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // La solicitud fue exitosa
                    Log.d("CategoryActivity", "Categoría creada exitosamente");
                } else {
                    // La solicitud no fue exitosa
                    Log.e("CategoryActivity", "Error al crear la categoría: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error de red o error en la respuesta
                Log.e("CategoryActivity", "Error en la llamada: " + t.getMessage());
            }
        });
    }
}