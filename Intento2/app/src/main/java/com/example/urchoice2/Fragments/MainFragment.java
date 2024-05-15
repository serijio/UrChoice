package com.example.urchoice2.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Adapters.MyPagerAdapter;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private User user;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int userId;

    private UserAPI userApi;
    public MainFragment() {
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.f1___fragment_main, container, false);
        // Obtener referencias de TabLayout y ViewPager
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        GetUser(view);


        // Crear un adaptador para el ViewPager y agregar las páginas
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MainFragment_Category_SubFragment(), "CATEGORY");
        adapter.addFragment(new MainFragment_Room_SubFragment(), "ROOM");

        // Asignar el adaptador al ViewPager
        viewPager.setAdapter(adapter);

        // Conectar el ViewPager con el TabLayout
        tabLayout.setupWithViewPager(viewPager);

        // Agregar un OnTabSelectedListener para cambiar entre las pestañas
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Cambiar al fragmento correspondiente al hacer clic en la pestaña
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No se requiere ninguna acción al deseleccionar una pestaña
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No se requiere ninguna acción al volver a seleccionar una pestaña
            }
        });

        return view;
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserAPI.class);
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);
    }

    public void GetUser(View view){
        Call<User> call = userApi.getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    updateView(view);
                    Log.e("API Error", "NOMBRE" + user.getNick_user());

                } else {
                    Log.e("API Error", "Error al obtener el usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API Error", "Error al realizar la llamada: " + t.getMessage());
            }
        });
    }

    private void updateView(View view) {
        if (user != null) {
            TextView username = view.findViewById(R.id.home_username);
            TextView email = view.findViewById(R.id.home_email);
            TextView games = view.findViewById(R.id.home_games_played);
            ImageView userIMG = view.findViewById(R.id.UserPhoto);

            username.setText(user.getNick_user());
            email.setText(user.getEmail_user());
            if(user.getImg_user() != null || user.getImg_user().isEmpty()){
                userIMG.setImageBitmap(base64ToBitmap(user.getImg_user()));
            }
            games.setText(String.valueOf(user.getGamesPlayed()));
        }
    }
}