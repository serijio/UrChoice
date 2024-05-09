package com.example.urchoice2.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.Adapters.MainFragment_Room_Adapter;
import com.example.urchoice2.R;

import java.util.ArrayList;
import java.util.List;

public class MainFragment_Room_SubFragment extends Fragment {
    private RecyclerView recyclerView;
    private MainFragment_Room_Adapter roomAdapter;

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
}