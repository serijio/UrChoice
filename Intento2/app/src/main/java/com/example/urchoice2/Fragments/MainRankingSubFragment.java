package com.example.urchoice2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.prueba;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRankingSubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ElementsAPI elementsAPI;
    private CategoriesAPI categoriesAPI;
    private Integer categoryId;

    public MainRankingSubFragment() {}

    public static MainRankingSubFragment newInstance(String param1, String param2) {
        MainRankingSubFragment fragment = new MainRankingSubFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Conectar();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f1___xsub__fragment_main_ranking, container, false);
        GetCategory(view);
        GetRanking(view);
        return view;
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        elementsAPI = retrofit.create(ElementsAPI.class);
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        categoryId = preferences.getInt("id_categorySingle", 0);
    }

    public void GetCategory(View view){
        Call<Category> call = categoriesAPI.getCategory(categoryId);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Category category = response.body();
                    ImageView categoryIMG = view.findViewById(R.id.IMGCategory);
                    categoryIMG.setImageBitmap(base64ToBitmap(category.getImg_cat()));
                } else {
                    // Manejar el error de la respuesta
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                // Manejar el error de la llamada
            }
        });
    }

    public void GetRanking(View view){
        Call<List<Element>> call = elementsAPI.getRanking(categoryId);
        call.enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elements = response.body();
                    for(int i = 0;i < elements.size();i++){
                        int name = Integer.parseInt("R.id.Number" + i);
                        int IMG = Integer.parseInt("R.id.Number" + i + "IMG");
                        TextView nameTextView  = view.findViewById(name);
                        ImageView IMGImageView = view.findViewById(IMG);
                        nameTextView.setText(elements.get(i).getName_elem());
                        IMGImageView.setImageBitmap(base64ToBitmap(elements.get(i).getImg_elem()));
                    }
                    // Haz algo con los datos recibidos
                } else {
                    // Maneja el error de respuesta
                }
            }

            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {
                // Maneja el error de la llamada
            }
        });
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public void StartGame(View view){
        Intent intent = new Intent(requireContext(), prueba.class);
        requireContext().startActivity(intent);
    }
}