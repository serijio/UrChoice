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
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main_Screen_Adapter extends RecyclerView.Adapter<Main_Screen_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Main_Screen_Model> mainScreenModels;
    private LayoutInflater inflater;
    private CategoriesAPI categoriesAPI;

    public Main_Screen_Adapter(Context context, ArrayList<Main_Screen_Model> mainScreenModels) {
        this.context = context;
        this.mainScreenModels = mainScreenModels;
        Conectar();
    }

    @NonNull
    @Override
    public Main_Screen_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.a5___activity_register_screen, parent, false); //modificar cuando haya row
        return new Main_Screen_Adapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull Main_Screen_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        ImageView favsButton;
        ImageView saveButton;
        TextView catName;
        CardView cvMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mainScreenModels.size();
    }
}
