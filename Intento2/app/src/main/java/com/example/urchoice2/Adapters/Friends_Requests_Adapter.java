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
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.Favs;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Friends_Requests_Model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Friends_Requests_Adapter extends RecyclerView.Adapter<Friends_Requests_Adapter.MyViewHolder> {
    Context context;
    ArrayList<Friends_Requests_Model> friendsRequestsModels;
    private LayoutInflater inflater;
    private FriendsAPI friendsAPI;
    private int userId;
    private List<User> friendsList;

    public Friends_Requests_Adapter(Context context, ArrayList<Friends_Requests_Model> friendsRequestsModels, List<User> users) {
        this.context = context;
        this.friendsRequestsModels = friendsRequestsModels;
        this.friendsList = users;
    }


    @NonNull
    @Override
    public Friends_Requests_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Conectar();
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_friends_requests, parent, false);
        return new Friends_Requests_Adapter.MyViewHolder(view);
    }


    public void onBindViewHolder(@NonNull Friends_Requests_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String friend_name = friendsList.get(position).getNick_user();
        String friend_img = friendsList.get(position).getImg_user();
        String friend_email = friendsList.get(position).getEmail_user();

        if(friend_img != null || friend_img.isEmpty()){
            Bitmap bitmap = base64ToBitmap(friend_img);
            holder.friendReqImg.setImageBitmap(bitmap);
        }
        holder.friendReqName.setText(friend_name);
        holder.friendReqEmail.setText(friend_email);

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFriend(friendsList.get(position).getId_user(), "Aceptada", position);
                friendsList.remove(friendsList.get(position));
            }
        });
        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateFriend(friendsList.get(position).getId_user(), "Denegado", position);
                friendsList.remove(friendsList.get(position));
            }
        });
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView friendReqImg;
        TextView friendReqName;
        TextView friendReqEmail;
        ImageView acceptButton;
        ImageView declineButton;
        CardView cvFriendsRequests;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.friendReqImg = itemView.findViewById(R.id.requests_image);
            this.friendReqName = itemView.findViewById(R.id.requests_name);
            this.friendReqEmail = itemView.findViewById(R.id.requests_email);
            this.acceptButton = itemView.findViewById(R.id.accept_request);
            this.declineButton = itemView.findViewById(R.id.decline_request);
            this.cvFriendsRequests = itemView.findViewById(R.id.cvRequests);
        }
    }

    @Override
    public int getItemCount() {
        return friendsRequestsModels.size();
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

    public void UpdateFriend(int idUs2, String nuevoEstado, int position){
        Log.e("SQL","DATOS: " + idUs2 + "," + userId + "," + nuevoEstado);
        Call<Void> call = friendsAPI.updateFriendStatus(userId, idUs2, nuevoEstado);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("SQL", "Actualización exitosa");
                    friendsRequestsModels.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, friendsRequestsModels.size());
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