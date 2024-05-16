package com.example.urchoice2.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

public class FriendsRequestsSubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    MaterialButton back_to_friends;

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
        View view = inflater.inflate(R.layout.f2___x_sub_fragment_friends_requests, container, false);

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
}