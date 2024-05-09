package com.example.urchoice2.Fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.Adapters.MainFragment_Room_Adapter;
import com.example.urchoice2.Classes.Rooms;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
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

    private TextView room_pin_textview;
    private MaterialButton enter_room;

    public MainFragment_Room_SubFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.f1__x__main_fragment_room, container, false);

        recyclerView = rootView.findViewById(R.id.rooms_recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> roomNames = new ArrayList<>();
        roomNames.add("Room 1");
        // Añade más nombres de habitaciones si es necesario

        List<Integer> numberOfPlayers = new ArrayList<>();
        numberOfPlayers.add(0);
        // Añade más números de jugadores si es necesario

        roomAdapter = new MainFragment_Room_Adapter(getContext(), roomNames, numberOfPlayers);
        recyclerView.setAdapter(roomAdapter);










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
                    List<String> roomNames = new ArrayList<>();
                    List<Integer> numberOfPlayers = new ArrayList<>();
                    List<Rooms> rooms = response.body();
                    for (int i = 0; i < rooms.size();i++){
                        roomNames.add(rooms.get(i).getNameRoom());
                        numberOfPlayers.add(rooms.get(i).getUserCount());
                    }
                    roomAdapter = new MainFragment_Room_Adapter(getContext(), roomNames, numberOfPlayers);
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