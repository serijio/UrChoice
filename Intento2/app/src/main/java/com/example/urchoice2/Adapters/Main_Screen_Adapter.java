package com.example.urchoice2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.API.FavsAPI;
import com.example.urchoice2.API.SavedAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Fragments.MainRankingSubFragment;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main_Screen_Adapter extends RecyclerView.Adapter<Main_Screen_Adapter.MyViewHolder>{
    Context context;
    ArrayList<Main_Screen_Model> mainScreenModels;
    private LayoutInflater inflater;
    private List<Category> categoryList;
    private List<Category> categorySavedList = new ArrayList<>();
    private List<Category> categoryFavList = new ArrayList<>();
    private List<Category> filteredCategoryList = new ArrayList<>();
    private int userId;
    private SavedAPI savedAPI;
    private FavsAPI favsAPI;


    public Main_Screen_Adapter(Context context, ArrayList<Main_Screen_Model> mainScreenModels, List<Category> category) {
        this.context = context;
        this.mainScreenModels = mainScreenModels;
        this.categoryList = category;
        this.filteredCategoryList = new ArrayList<>(categoryList);
    }


    @NonNull
    @Override
    public Main_Screen_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_main_screen, parent, false);
        return new Main_Screen_Adapter.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull Main_Screen_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Category category = filteredCategoryList.get(position);
        String category_name = category.getName_cat();
        String category_img = category.getImg_cat();
        Bitmap bitmap = base64ToBitmap(category_img);

        holder.catName.setText(category_name);
        holder.catImg.setImageBitmap(bitmap);

        holder.catImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id_categorySingle", categoryList.get(position).getId_cat());
                editor.apply();

                //Intent intent = new Intent(context, MainRankingSubFragment.class);
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

                // Iniciar la transacción de fragmento
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual con MainRankingSubFragment
                MainRankingSubFragment fragment = new MainRankingSubFragment();
                transaction.replace(R.id.mainframe, fragment); // Reemplaza R.id.mainFrame(El frame intermedio) con el ID de tu contenedor de fragmentos
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        holder.favsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categoryFavList.contains(categoryList.get(position))){
                    eliminarFavorito(userId, categoryList.get(position).getId_cat());
                    categoryFavList.remove(categoryList.get(position));
                    holder.favsButton.setImageResource(R.drawable.fav_red_border);
                    holder.saveButton.setImageResource(R.drawable.save_fav_click);

                }else{
                    addFav(userId, categoryList.get(position).getId_cat());
                    categoryFavList.add(categoryList.get(position));
                    holder.favsButton.setImageResource(R.drawable.fav_red_click);
                    holder.saveButton.setImageResource(R.drawable.save_blue_border);

                }
            }
        });

        holder.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categorySavedList.contains(categoryList.get(position))){
                    eliminarSaved(userId, categoryList.get(position).getId_cat());
                    categorySavedList.remove(categoryList.get(position));
                    holder.saveButton.setImageResource(R.drawable.save_blue_border);
                    holder.favsButton.setImageResource(R.drawable.fav_red_click);


                }else{
                    insertarSaved(userId, categoryList.get(position).getId_cat());
                    categorySavedList.add(categoryList.get(position));
                    holder.saveButton.setImageResource(R.drawable.save_fav_click);
                    holder.favsButton.setImageResource(R.drawable.fav_red_border);



                }
            }
        });
    }


    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public void filter(String text) {
        filteredCategoryList.clear();
        if (text.isEmpty()) {
            filteredCategoryList.addAll(categoryList);
        } else {
            for (Category category : categoryList) {
                if (category.getName_cat().toLowerCase().contains(text.toLowerCase())) {
                    filteredCategoryList.add(category);
                }
            }
        }
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        ImageView favsButton;
        ImageView saveButton;
        TextView catName;
        CardView cvMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cvMain = itemView.findViewById(R.id.main_cv);
            this.favsButton = itemView.findViewById(R.id.main_fav);
            this.saveButton = itemView.findViewById(R.id.main_save);
            this.catName = itemView.findViewById(R.id.main_cv_cat_name);
            this.catImg = itemView.findViewById(R.id.main_cv_cat_image);
        }
    }


    @Override
    public int getItemCount() {
        return filteredCategoryList.size();
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