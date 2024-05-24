package com.example.urchoice2.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.Adapters.Friends_Requests_Adapter;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Friends_Requests_Model;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendsRequestsSubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private String mParam1;
    private String mParam2;
    MaterialButton back_to_friends;
    private List<User> users;
    private int userId;
    private FriendsAPI friendsAPI;

    private Friends_Requests_Adapter friends_requests_adapter;
    private ArrayList<Friends_Requests_Model> friendsRequestsModels = new ArrayList<>();

    private Handler handler;
    private Runnable runnable;
    private AlertDialog alertDialog;

    public FriendsRequestsSubFragment() {}

    public static FriendsRequestsSubFragment newInstance(String param1, String param2) {
        FriendsRequestsSubFragment fragment = new FriendsRequestsSubFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Conectar();
        waitAlertAltera();
        View view = inflater.inflate(R.layout.f2___x_sub_fragment_friends_requests, container, false);
        back_to_friends = view.findViewById(R.id.back_to_friends_button);
        recyclerView = view.findViewById(R.id.requests_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        back_to_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_friends();
            }
        });

        return view;
    }

    public void back_friends() {
        Fragment friendsFragment = new FriendsFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.requests_layout, friendsFragment);
        transaction.addToBackStack(null); // Opcional: añadir a la pila de retroceso
        transaction.commit();
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        friendsAPI = retrofit.create(FriendsAPI.class);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                GetFriends();
                handler.postDelayed(this, 2000);
            }
        };
    }

    public void GetFriends(){
        Call<List<User>> call = friendsAPI.getRequests(userId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    users = new ArrayList<>(response.body());
                    setRvMain();
                    friends_requests_adapter = new Friends_Requests_Adapter(requireContext(),friendsRequestsModels,users);
                    recyclerView.setAdapter(friends_requests_adapter);
                    dismissWaitAlert();
                } else {
                    Log.e("SQL", "Error en la respuesta: " + response.message());
                    // Manejar errores de la API
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("SQL", "Error en la llamada: " + t.getMessage());
                // Manejar errores de conexión
            }
        });
    }

    private void setRvMain() {
        friendsRequestsModels = new ArrayList<>();
        Drawable mainAddIcon = ContextCompat.getDrawable(requireContext(), R.drawable.fav_red_border);
        Drawable mainDelIcon = ContextCompat.getDrawable(requireContext(), R.drawable.save_blue_border);
        for (int i = 0; i < users.size(); i++) {
            friendsRequestsModels.add(new Friends_Requests_Model(
                    users.get(i).getNick_user(),
                    users.get(i).getEmail_user(),
                    base64ToBitmap(users.get(i).getImg_user()),
                    mainAddIcon,
                    mainDelIcon
            ));
        }
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable); // Inicia la ejecución
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Detiene la ejecución
    }
    public void waitAlertAltera() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.ff___xx_alert__all_fragments_loading_altera, null);

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