package com.example.urchoice2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.urchoice2.R;

public class tab_FavouriteFragment extends Fragment {

    public tab_FavouriteFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.f4__x__fragment_favourite, container, false);




        return rootView;
    }

}