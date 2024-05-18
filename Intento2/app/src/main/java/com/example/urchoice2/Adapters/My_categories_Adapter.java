package com.example.urchoice2.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class My_categories_Adapter extends RecyclerView.Adapter<My_categories_Adapter.my_categories_CardViewHolder> {

    Context context;
    ArrayList<Main_Screen_Model> mainScreenModels;
    private LayoutInflater inflater;
    private List<Category> categoryList;

    private int userId;
    private SavedAPI savedAPI;
    private FavsAPI favsAPI;




    public My_categories_Adapter(Context context, ArrayList<Main_Screen_Model> mainScreenModels, List<Category> category) {
        this.context = context;
        this.mainScreenModels = mainScreenModels;
        this.categoryList = category;
    }

    @NonNull
    @Override
    public my_categories_CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_my_categories, parent, false);
        return new My_categories_Adapter.my_categories_CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull my_categories_CardViewHolder holder, int position) {
        String category_name = categoryList.get(position).getName_cat();
        String category_img = categoryList.get(position).getImg_cat();
        Bitmap bitmap = base64ToBitmap(category_img);

        holder.catName.setText(category_name);
        holder.catImg.setImageBitmap(bitmap);
    }



    @Override
    public int getItemCount() {
        return mainScreenModels.size();
    }
    public class my_categories_CardViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;
        ImageView favsButton;
        ImageView saveButton;
        TextView catName;
        CardView cvMain;
        public my_categories_CardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cvMain = itemView.findViewById(R.id.main_cv);
            this.favsButton = itemView.findViewById(R.id.main_fav);
            this.saveButton = itemView.findViewById(R.id.main_save);
            this.catName = itemView.findViewById(R.id.main_cv_cat_name);
            this.catImg = itemView.findViewById(R.id.main_cv_cat_image);
        }


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
    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
