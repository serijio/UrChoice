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

import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Friends_Screen_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Friends_Screen_Adapter extends RecyclerView.Adapter<Friends_Screen_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Friends_Screen_Model> friendsScreenModels;
    private LayoutInflater inflater;
    private FriendsAPI friendsAPI;
    private int userId;
    private List<User> friendsList;

    public Friends_Screen_Adapter(Context context, ArrayList<Friends_Screen_Model> friendsScreenModels, List<User> users) {
        this.context = context;
        this.friendsScreenModels = friendsScreenModels;
        this.friendsList = users;
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
        TextView playerName = holder.itemView.findViewById(R.id.friends_name);
        TextView playerMail = holder.itemView.findViewById(R.id.friends_email);
        String friend_img = friendsList.get(position).getImg_user();
        if(friend_img != null || friend_img.isEmpty()){
            Bitmap bitmap = base64ToBitmap(friend_img);
            holder.friendImg.setImageBitmap(bitmap);
        }
        //MaterialButton delete = holder.itemView.findViewById(R.id.delete_friend);
        playerName.setText(friendsList.get(position).getNick_user());
        playerMail.setText("/@"+ friendsList.get(position).getEmail_user());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFriend(friendsList.get(position).getId_user());
            }
        });
    }





    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView friendImg;
        TextView friendName;
        TextView friendEmail;
        ImageView deleteButton;
        CardView cvFriends;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.friendImg = itemView.findViewById(R.id.friends_image);
            this.friendName = itemView.findViewById(R.id.friends_name);
            this.friendEmail = itemView.findViewById(R.id.friends_email);
            this.deleteButton = itemView.findViewById(R.id.delete_friend);
            this.cvFriends = itemView.findViewById(R.id.cvFriends);
        }
    }

    @Override
    public int getItemCount() {
        return friendsScreenModels.size();
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        friendsAPI = retrofit.create(FriendsAPI.class);
        SharedPreferences preferences = context.getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);
    }

    public void UpdateFriend(int idUs2){
        Call<Void> call = friendsAPI.updateFriendStatus(userId, idUs2, "Denegado");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SQL", "Actualización exitosa");
                    // Manejar respuesta exitosa
                } else {
                    Log.e("SQL", "Error en la respuesta: " + response.message());
                    // Manejar errores de la API
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL", "Error en la llamada: " + t.getMessage());
                // Manejar errores de conexión
            }
        });
    }

}
