package com.example.urchoice2.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.Classes.RoomData;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MainFragment_Room_Adapter extends RecyclerView.Adapter<MainFragment_Room_Adapter.RoomViewHolder> {

    private Context context;
    private List<String> roomNames;
    private List<Integer> numberOfPlayers;

    public MainFragment_Room_Adapter(Context context, List<String> roomNames, List<Integer> numberOfPlayers) {
        this.context = context;
        this.roomNames = roomNames;
        this.numberOfPlayers = numberOfPlayers;
    }



    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.f1__x__main_fragment_room_card_view, parent, false);
        return new RoomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        String roomName = roomNames.get(position);
        int numberOfPlayers = this.numberOfPlayers.get(position);

        holder.roomNameTextView.setText(roomName);
        holder.numberOfPlayersTextView.setText(String.valueOf(numberOfPlayers));

        // Agregar OnClickListener al TextView del nombre de la habitación
        holder.roomNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí abres el AlertDialog
                LayoutInflater inflater = LayoutInflater.from(context);
                View dialogView = inflater.inflate(R.layout.f1__x__main_fragment_room_pin_alert, null);

                // Construir el AlertDialog utilizando el diseño personalizado
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);

                // Mostrar el AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomNames.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomNameTextView;
        TextView numberOfPlayersTextView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNameTextView = itemView.findViewById(R.id.room_name);
            numberOfPlayersTextView = itemView.findViewById(R.id.number_players);
        }





    }




}
