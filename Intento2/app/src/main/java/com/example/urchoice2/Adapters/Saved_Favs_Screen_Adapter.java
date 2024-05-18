package com.example.urchoice2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.urchoice2.API.FavsAPI;
import com.example.urchoice2.Classes.Favs;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Saved_Favs_Screen_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Saved_Favs_Screen_Adapter extends RecyclerView.Adapter<Saved_Favs_Screen_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Saved_Favs_Screen_Model> savedFavsScreenModels;
    private List<Favs> favsList;
    private LayoutInflater inflater;
    private FavsAPI favsAPI;
    private int userId;

    public Saved_Favs_Screen_Adapter(Context context, ArrayList<Saved_Favs_Screen_Model> savedFavsScreenModels, List<Favs> favorites) {
        this.context = context;
        this.savedFavsScreenModels = savedFavsScreenModels;
        this.favsList = favorites;
    }


    @NonNull
    @Override
    public Saved_Favs_Screen_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_saved_favs_screen, parent, false);
        return new Saved_Favs_Screen_Adapter.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull Saved_Favs_Screen_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String category_name = favsList.get(position).getName_cat();
        String category_img = favsList.get(position).getImg_cat();

        Bitmap bitmap = base64ToBitmap(category_img);
        Favs saved = favsList.get(position);

        holder.favsImg.setImageBitmap(bitmap);
        holder.favsName.setText(category_name);

        holder.favsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.favsButton.setImageResource(R.drawable.fav_red_click);
        holder.favsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!saved.isSaved()) {
                    holder.favsButton.setImageResource(R.drawable.fav_red_border);
                    eliminarFavorito(userId,favsList.get(position).getId_cat());
                    saved.setSaved(true);

                } else {
                    holder.favsButton.setImageResource(R.drawable.fav_red_click);
                    insertarFavorito(userId,favsList.get(position).getId_cat());
                    saved.setSaved(false);
                }
            }
        });
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView favsImg;
        ImageView favsButton;
        ImageView favsSaveButton;
        TextView favsName;
        CardView cvSavedFavs;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.favsImg = itemView.findViewById(R.id.favs_cv_cat_image);
            this.favsButton = itemView.findViewById(R.id.fav_Button);
            this.favsSaveButton = itemView.findViewById(R.id.save_button);
            this.favsName = itemView.findViewById(R.id.favs_cv_cat_name);
            this.cvSavedFavs = itemView.findViewById(R.id.favs_cv);
        }
    }


    @Override
    public int getItemCount() {
        return savedFavsScreenModels.size();
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        favsAPI = retrofit.create(FavsAPI.class);
        SharedPreferences preferences = context.getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);
    }


    private void insertarFavorito(int idUser, int idCat) {
        Call<Void> call = favsAPI.insertarFavorito(idUser, idCat);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SQL","SE INSERTO");
                } else {
                    Log.e("SQL","NO FUNCIONO");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL","ERROR");
            }
        });
    }


    private void eliminarFavorito(int idUser, int idCat) {
        Call<Void> call = favsAPI.eliminarFavorito(idUser, idCat);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SQL","SE ELIMINO");
                } else {
                    Log.e("SQL","NO FUNCIONO");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL","ERROR");
            }
        });
    }
}