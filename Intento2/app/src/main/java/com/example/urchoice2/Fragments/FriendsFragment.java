package com.example.urchoice2.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

public class FriendsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    MaterialButton friends_requests;

    public FriendsFragment() {}

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        View view = inflater.inflate(R.layout.f2___fragment_friends, container, false);
        friends_requests = view.findViewById(R.id.see_requests_button);

        friends_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                see_requests();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.friends_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] usernames = {"TheRockex", "Spidey1912", "Lukinda551", "LordGrim551", "TuMama", "王八蛋"};
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_friends_screen, parent, false);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView playerName = holder.itemView.findViewById(R.id.friends_name);
                TextView playerMail = holder.itemView.findViewById(R.id.friends_email);

                //MaterialButton delete = holder.itemView.findViewById(R.id.delete_friend);
                playerName.setText(usernames[position]);
                playerMail.setText("/@"+usernames[position]);

            }

            @Override
            public int getItemCount() {
                return usernames.length;
            }
        });


        return view;
    }

    public void see_requests() {
        Fragment friendsRequestsFragment = new FriendsRequestsSubFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.friends_layout, friendsRequestsFragment);
        transaction.addToBackStack(null); // Opcional: añadir a la pila de retroceso
        transaction.commit();
    }
}