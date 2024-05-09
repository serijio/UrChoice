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
import com.example.urchoice2.RecyclerViews.Saved_Favs_Screen_Model;
import com.example.urchoice2.RecyclerViews.Saved_Saved_Screen_Model;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Saved_Saved_Screen_Adapter extends RecyclerView.Adapter<Saved_Saved_Screen_Adapter.MyViewHolder> {

    Context context;
    ArrayList<Saved_Saved_Screen_Model> savedSavedScreenModels;
    private LayoutInflater inflater;
    private CategoriesAPI categoriesAPI;

    public Saved_Saved_Screen_Adapter(Context context, ArrayList<Saved_Saved_Screen_Model> savedSavedScreenModels) {
        this.context = context;
        this.savedSavedScreenModels = savedSavedScreenModels;
        Conectar();
    }

    @NonNull
    @Override
    public Saved_Saved_Screen_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_saved_saved_screen, parent, false);
        return new Saved_Saved_Screen_Adapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull Saved_Saved_Screen_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView savedImg;
        ImageView savedButton;
        ImageView savedSaveButton;
        TextView savedName;
        CardView cvSavedSaved;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return savedSavedScreenModels.size();
    }
}