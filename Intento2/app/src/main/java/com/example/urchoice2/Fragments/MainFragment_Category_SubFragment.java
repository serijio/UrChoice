package com.example.urchoice2.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.Adapters.Main_Screen_Adapter;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.User;
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
    private Context context;
    private int userId;


    private CategoriesAPI categoriesAPI;
    private RecyclerView recyclerView;
    private List<Category> categoryList;
    private AlertDialog alertDialog;
    private Main_Screen_Adapter main_screen_adapter;
    private ArrayList<Main_Screen_Model> main_screen_model = new ArrayList<>();

    public MainFragment_Category_SubFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Conectar();
        View rootView = inflater.inflate(R.layout.f1___sub__fragment_main_category, container, false);

        recyclerView = rootView.findViewById(R.id.rvCategoriesHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //waitAlert();
        waitAlertAltera();
        GetCategories();

        return rootView;
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


    public void GetCategories(){
        categoriesAPI.getCategories(userId).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    setRvMain();

                    // Para que el recycler tenga 2 columnas
                    GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
                    recyclerView.setLayoutManager(layoutManager);

                    // Aquí creas tu adaptador y lo estableces en el RecyclerView
                    main_screen_adapter = new Main_Screen_Adapter(requireContext(), main_screen_model, categoryList);
                    recyclerView.setAdapter(main_screen_adapter);
                    dismissWaitAlert();
                } else {
                    Log.e("API Error", "Error al obtener las categorías: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API Error", "Error al realizar la llamada: " + t.getMessage());
            }
        });

    }


    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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


    /*public void waitAlert(){
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