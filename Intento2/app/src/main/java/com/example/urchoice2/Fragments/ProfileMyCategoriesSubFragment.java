package com.example.urchoice2.Fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.Adapters.My_categories_Adapter;
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

public class ProfileMyCategoriesSubFragment extends Fragment {
    /*private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/
    private int userId;
    private AlertDialog alertDialog;
    private CategoriesAPI categoriesAPI;
    private RecyclerView recyclerView;
    private List<Category> categoryList;
    private My_categories_Adapter my_categories_adapter;
    private ArrayList<Main_Screen_Model> main_screen_model = new ArrayList<>();


    public ProfileMyCategoriesSubFragment(){}

    /*public static ProfileMyCategoriesSubFragment newInstance(String param1, String param2) {
        ProfileMyCategoriesSubFragment fragment = new ProfileMyCategoriesSubFragment();
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
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f5___x_sub__fragment_profile_my_categories, container, false);
        Conectar();
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        //waitAlert();
        waitAlertAltera();
        recyclerView = view.findViewById(R.id.recycler_my_categories);
        recyclerView.setLayoutManager(layoutManager);
        GetMyCategories();
        return view;
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


    public void GetMyCategories(){
        Call<List<Category>> call = categoriesAPI.getCategoriesByUserId(userId); // Cambia 1 por el user_id que desees
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList = response.body();
                    setRvMain();
                    //Log.e("SQL","DATOS: " + categoryList.get(0).getName_cat());
                    my_categories_adapter = new My_categories_Adapter(requireContext(), main_screen_model, categoryList);
                    recyclerView.setAdapter(my_categories_adapter);
                    dismissWaitAlert();
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
    /*
    public void waitAlert(){
        // Construir el nuevo AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.ff___all_fragments_loading_alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }*/

    public void waitAlertAltera() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.ff___all_fragments_loading_alert_dialog_altera, null);

        //cargar gif
        ImageView alteraImageView = view.findViewById(R.id.altera);
        Glide.with(this).asGif().load(R.drawable.altera_final).into(alteraImageView);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }


    public void dismissWaitAlert() {
        alertDialog.dismiss();
    }
}