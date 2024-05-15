package com.example.urchoice2.Fragments;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.Adapters.MainFragment_Room_Adapter;
import com.example.urchoice2.Classes.Rooms;
import com.example.urchoice2.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment_Room_SubFragment extends Fragment {
    private RecyclerView recyclerView;
    private RoomAPI roomAPI;
    private MainFragment_Room_Adapter roomAdapter;




    public MainFragment_Room_SubFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Conectar();
        View rootView = inflater.inflate(R.layout.f1___sub__fragment_main_room, container, false);

        recyclerView = rootView.findViewById(R.id.rooms_recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GetRooms();


        return rootView;
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        roomAPI = retrofit.create(RoomAPI.class);
    }
    public void GetRooms(){
        Call<List<Rooms>> call = roomAPI.getRooms();
        call.enqueue(new Callback<List<Rooms>>() {
            @Override
            public void onResponse(Call<List<Rooms>> call, Response<List<Rooms>> response) {
                if (response.isSuccessful()) {

                    List<Rooms> rooms = response.body();

                    roomAdapter = new MainFragment_Room_Adapter(getContext(),rooms);
                    recyclerView.setAdapter(roomAdapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Rooms>> call, Throwable t) {
                // Manejar errores de la llamada
            }
        });
    }


}