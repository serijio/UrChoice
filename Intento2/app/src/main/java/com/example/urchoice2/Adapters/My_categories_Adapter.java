package com.example.urchoice2.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Fragments.EditCategoriesSubFragment;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Main_Screen_Model;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class My_categories_Adapter extends RecyclerView.Adapter<My_categories_Adapter.my_categories_CardViewHolder> {

    Context context;
    ArrayList<Main_Screen_Model> mainScreenModels;
    private LayoutInflater inflater;
    private List<Category> categoryList;
    private MaterialButton catedit;
    private MaterialButton catDelete;
    private CategoriesAPI categoriesAPI;
    private int userId;


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
    public void onBindViewHolder(@NonNull my_categories_CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String category_name = categoryList.get(position).getName_cat();
        String category_img = categoryList.get(position).getImg_cat();
        Bitmap bitmap = base64ToBitmap(category_img);
        holder.catName.setText(category_name);
        holder.catImg.setImageBitmap(bitmap);

        holder.catedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

                // Iniciar la transacción de fragmento
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Crear una nueva instancia del fragmento y pasar el argumento
                int idCat = categoryList.get(position).getId_cat();
                EditCategoriesSubFragment fragment = EditCategoriesSubFragment.newInstance(idCat);
                transaction.replace(R.id.my_categories_fragment, fragment); // Reemplaza R.id.my_categories_fragment con el ID de tu contenedor de fragmentos
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        holder.catDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.f5___xx_alert__delete_category, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);

                // Mostrar el AlertDialog
                AlertDialog alertDialog = builder.create();


                MaterialButton yes_delete_button = view.findViewById(R.id.yes_delete);
                yes_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCategory(position);
                        alertDialog.dismiss();
                    }
                });
                MaterialButton no_delete_button = view.findViewById(R.id.no_delete);
                no_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialog.show();




            }
        });

    }



    @Override
    public int getItemCount() {
        return mainScreenModels.size();
    }
    public class my_categories_CardViewHolder extends RecyclerView.ViewHolder {
        ImageView catImg;

        TextView catName;
        MaterialButton catedit;
        MaterialButton catDelete;
        CardView cvMain;
        public my_categories_CardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cvMain = itemView.findViewById(R.id.main_cv);
            this.catName = itemView.findViewById(R.id.main_cv_cat_name);
            this.catImg = itemView.findViewById(R.id.main_cv_cat_image);
            this.catDelete = itemView.findViewById(R.id.delete_category);
            this.catedit = itemView.findViewById(R.id.edit_category);
        }


    }
    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        SharedPreferences sharedPreferences = context.getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    public void deleteCategory(int position) {
        Call<Void> call = categoriesAPI.deleteCategory(categoryList.get(position).getId_cat());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CategoryActivity", "Categoría eliminada exitosamente");
                    mainScreenModels.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mainScreenModels.size());
                } else {
                    Log.e("CategoryActivity", "Error al eliminar la categoría: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CategoryActivity", "Error en la llamada: " + t.getMessage());
            }
        });
    }

}
