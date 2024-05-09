package com.example.urchoice2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tab_SavedFragment extends Fragment {

    public tab_SavedFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.f4__x__fragment_saved, container, false);




        return rootView;
    }




}
