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
import com.example.urchoice2.API.SavedAPI;
import com.example.urchoice2.Classes.Saved;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Saved_Saved_Screen_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Saved_Saved_Screen_Adapter extends RecyclerView.Adapter<Saved_Saved_Screen_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Saved_Saved_Screen_Model> savedSavedScreenModels;
    private List<Saved> savedList;
    private List<Saved> favsList = new ArrayList<>();
    private LayoutInflater inflater;
    private int userId;
    private SavedAPI savedAPI;
    private FavsAPI favsAPI;
    public Saved_Saved_Screen_Adapter(Context context, ArrayList<Saved_Saved_Screen_Model> savedSavedScreenModels, List<Saved> saveds) {
        this.context = context;
        this.savedSavedScreenModels = savedSavedScreenModels;
        this.savedList = saveds;
    }


    @NonNull
    @Override
    public Saved_Saved_Screen_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_saved_favs_screen, parent, false);
        return new Saved_Saved_Screen_Adapter.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull Saved_Saved_Screen_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String category_name = savedList.get(position).getName_cat();
        String category_img = savedList.get(position).getImg_cat();

        Bitmap bitmap = base64ToBitmap(category_img);
        Saved saved = savedList.get(position);

        if (saved.isSaved()) {
            //holder.savedSaveButton.setImageResource(R.drawable.ic_saved); // Por ejemplo, cambia la imagen del botón
        } else {
            //holder.savedSaveButton.setImageResource(R.drawable.ic_unsaved); // Por ejemplo, cambia la imagen del botón
        }
        holder.savedImg.setImageBitmap(bitmap);
        holder.savedName.setText(category_name);
        holder.savedButton.setImageResource(R.drawable.save_fav_click);


        holder.savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!saved.isSaved()) {
                    holder.savedButton.setImageResource(R.drawable.save_blue_border);
                    eliminarSaved(userId, saved.getId_cat());
                    saved.setSaved(true);

                } else {
                    holder.savedButton.setImageResource(R.drawable.save_fav_click);
                    insertarSaved(userId, saved.getId_cat());
                    saved.setSaved(false);
                }
            }
        });

        holder.savedSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favsList.contains(saved)){
                    eliminarFavorito(userId,saved.getId_cat());
                    favsList.remove(saved);
                }else{
                    addFav(userId,saved.getId_cat());
                    favsList.add(saved);
                    saved.setSaved(true);
                }
            }
        });
    }


    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView savedImg;
        ImageView savedButton;
        ImageView savedSaveButton;
        TextView savedName;
        CardView cvSavedSaved;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.savedImg = itemView.findViewById(R.id.favs_cv_cat_image);
            this.savedButton = itemView.findViewById(R.id.save_button);
            this.savedSaveButton = itemView.findViewById(R.id.fav_Button);
            this.savedName = itemView.findViewById(R.id.favs_cv_cat_name);
            this.cvSavedSaved = itemView.findViewById(R.id.favs_cv);
        }
    }


    @Override
    public int getItemCount() {
        return savedSavedScreenModels.size();
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        savedAPI = retrofit.create(SavedAPI.class);
        favsAPI = retrofit.create(FavsAPI.class);
        SharedPreferences preferences = context.getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);
    }


    private void insertarSaved(int idUser, int idCat) {
        Call<Void> call = savedAPI.insertarSaved(idUser, idCat);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SQL","FUNCIONO");
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


    private void addFav(int idUser, int idCat){
        Call<Void> call = savedAPI.anadirFav(idUser, idCat);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SQL","FUNCIONO");
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


    private void eliminarSaved(int idUser, int idCat) {
        Call<Void> call = savedAPI.eliminarSaved(idUser, idCat);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SQL","FUNCIONO");
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