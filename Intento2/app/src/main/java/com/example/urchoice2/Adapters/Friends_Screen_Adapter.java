package com.example.urchoice2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Friends_Screen_Model;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Friends_Screen_Adapter extends RecyclerView.Adapter<Friends_Screen_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Friends_Screen_Model> friendsScreenModels;
    private LayoutInflater inflater;
    private UserAPI userAPI;

    public Friends_Screen_Adapter(Context context, ArrayList<Friends_Screen_Model> friendsScreenModels) {
        this.context = context;
        this.friendsScreenModels = friendsScreenModels;
        Conectar();
    }

    @NonNull
    @Override
    public Friends_Screen_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_friends_screen, parent, false);
        return new Friends_Screen_Adapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull Friends_Screen_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI = retrofit.create(UserAPI.class);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImg;
        TextView friendName;
        TextView friendEmail;
        ImageView deleteButton;
        CardView cvFriends;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return friendsScreenModels.size();
    }
}
