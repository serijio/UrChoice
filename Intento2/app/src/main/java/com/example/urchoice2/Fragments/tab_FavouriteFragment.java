package com.example.urchoice2.Fragments;

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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.API.FavsAPI;
import com.example.urchoice2.Adapters.Saved_Favs_Screen_Adapter;
import com.example.urchoice2.Classes.Favs;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Saved_Favs_Screen_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tab_FavouriteFragment extends Fragment {

    private List<Favs> favorites;
    private RecyclerView recyclerView;
    private ArrayList <Saved_Favs_Screen_Model> savedFavsScreenModels = new ArrayList<>();
    private Saved_Favs_Screen_Adapter saved_favs_screen_adapter;
    private FavsAPI favsAPI;


    public tab_FavouriteFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Conectar();
        View rootView = inflater.inflate(R.layout.f4__x__fragment_favourite, container, false);

        recyclerView = rootView.findViewById(R.id.rvFavs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        favsAPI = retrofit.create(FavsAPI.class);
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        int userId = preferences.getInt("id_user", 0);
        GetFavs(userId);
    }

    private void GetFavs(int id_user) {
        Call<List<Favs>> call = favsAPI.obtenerFavoritos(id_user);
        call.enqueue(new Callback<List<Favs>>() {
            @Override
            public void onResponse(Call<List<Favs>> call, Response<List<Favs>> response) {
                if (response.isSuccessful()) {
                    favorites = response.body();
                    Log.e("SQL","DATOS: " + favorites.get(0).getName_cat());
                    setRvMain();
                    saved_favs_screen_adapter = new Saved_Favs_Screen_Adapter(getContext(),savedFavsScreenModels,favorites);
                    recyclerView.setAdapter(saved_favs_screen_adapter);
                } else {
                    Log.e("Retrofit", "No hay");
                }
            }

            @Override
            public void onFailure(Call<List<Favs>> call, Throwable t) {
                Log.e("SQL","EROR");
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
        for (int i = 0; i < favorites.size(); i++) {
            savedFavsScreenModels.add(new Saved_Favs_Screen_Model(
                    favorites.get(i).getName_cat(),
                    base64ToBitmap(favorites.get(i).getImg_cat()),
                    mainFavIcon,
                    mainSaveIcon
            ));
        }
    }

}