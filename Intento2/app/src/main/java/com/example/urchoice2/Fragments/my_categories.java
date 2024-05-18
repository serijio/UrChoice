package com.example.urchoice2.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.FavsAPI;
import com.example.urchoice2.API.SavedAPI;
import com.example.urchoice2.Adapters.Main_Screen_Adapter;
import com.example.urchoice2.Adapters.My_categories_Adapter;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Friends_Screen_Model;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class my_categories extends Fragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int userId;
    private CategoriesAPI categoriesAPI;
    private RecyclerView recyclerView;
    private List<Category> categoryList;
    private My_categories_Adapter my_categories_adapter;
    private ArrayList<Main_Screen_Model> main_screen_model = new ArrayList<>();


    public my_categories(){}

    public static my_categories newInstance(String param1, String param2) {
        my_categories fragment = new my_categories();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Conectar();
        View rootView = inflater.inflate(R.layout.f5___x_fragment_profile_my_categories, container, false);
        recyclerView = rootView.findViewById(R.id.recycler_my_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GetMyCategories();
        return rootView;
    }


    public void GetMyCategories(){
        Call<List<Category>> call = categoriesAPI.getCategoriesByUserId(userId); // Cambia 1 por el user_id que desees
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    setRvMain();
                    Log.e("SQL","DATOS: " + categoryList.get(0).getName_cat());
                    my_categories_adapter = new My_categories_Adapter(requireContext(), main_screen_model, categoryList);
                    recyclerView.setAdapter(my_categories_adapter);
                } else {
                    Log.e("SQL", "Request failed");
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("SQL", "Network error", t);
            }
        });
    }

    private void setRvMain() {
        Drawable mainFavIcon = ContextCompat.getDrawable( requireContext(), R.drawable.fav_red_border);
        Drawable mainSaveIcon = ContextCompat.getDrawable(requireContext(), R.drawable.save_blue_border);
        for (int i = 0; i < categoryList.size(); i++) {
            main_screen_model.add(new Main_Screen_Model(
                    categoryList.get(i).getName_cat(),
                    base64ToBitmap(categoryList.get(i).getImg_cat()),
                    mainFavIcon,
                    mainSaveIcon
            ));
        }
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}