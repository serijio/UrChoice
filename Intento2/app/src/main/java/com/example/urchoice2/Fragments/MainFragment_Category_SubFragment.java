package com.example.urchoice2.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.Adapters.MainFragment_Room_Adapter;
import com.example.urchoice2.Adapters.Main_Screen_Adapter;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment_Category_SubFragment extends Fragment {

    private CategoriesAPI categoriesAPI;

    private RecyclerView recyclerView;

    private List<Category> categoryList;

    private Main_Screen_Adapter main_screen_adapter;
    private ArrayList<Main_Screen_Model> main_screen_model = new ArrayList<>();

    public MainFragment_Category_SubFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Conectar();

        View rootView = inflater.inflate(R.layout.f1___sub__fragment_main_category, container, false);

        recyclerView = rootView.findViewById(R.id.rvCategoriesHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GetCategories();

        return   rootView;
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
    }

    public void GetCategories(){
        categoriesAPI.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    setRvMain();
                    main_screen_adapter = new Main_Screen_Adapter(getContext(),main_screen_model,categoryList);
                    recyclerView.setAdapter(main_screen_adapter);

                } else {
                    Log.e("SQL","ERROR AL SACAR CATEGORIA");
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });
    }
    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    private void setRvMain() {
        Drawable mainFavIcon = ContextCompat.getDrawable(requireContext(), R.drawable.fav_red_border);
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

}