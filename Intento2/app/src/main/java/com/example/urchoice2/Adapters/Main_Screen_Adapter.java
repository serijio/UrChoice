package com.example.urchoice2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Rooms;
import com.example.urchoice2.Fragments.MainRankingSubFragment;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;
import com.example.urchoice2.Screens_activities.prueba;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main_Screen_Adapter extends RecyclerView.Adapter<Main_Screen_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Main_Screen_Model> mainScreenModels;
    private LayoutInflater inflater;
    private CategoriesAPI categoriesAPI;

    private List<Category> categoryList;

    public Main_Screen_Adapter(Context context, ArrayList<Main_Screen_Model> mainScreenModels, List<Category> category) {
        this.context = context;
        this.mainScreenModels = mainScreenModels;
        this.categoryList = category;
    }

    @NonNull
    @Override
    public Main_Screen_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_main_screen, parent, false);
        return new Main_Screen_Adapter.MyViewHolder(view);
    }

    public void onBindViewHolder(@NonNull Main_Screen_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String category_name = categoryList.get(position).getName_cat();
        String category_img = categoryList.get(position).getImg_cat();

        Bitmap bitmap = base64ToBitmap(category_img);

        holder.catName.setText(category_name);
        holder.catImg.setImageBitmap(bitmap);
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id_categorySingle", categoryList.get(position).getId_cat());
                editor.apply();
                Intent intent = new Intent(context, MainRankingSubFragment.class);
                context.startActivity(intent);

            }
        });

        //holder.catImg.setOnClickListener();


    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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
        return mainScreenModels.size();
    }
}