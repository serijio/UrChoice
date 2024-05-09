package com.example.urchoice2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.Classes.RoomData;
import com.example.urchoice2.Classes.Rooms;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.prueba;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment_Room_Adapter extends RecyclerView.Adapter<MainFragment_Room_Adapter.RoomViewHolder> {

    private Context context;



    private List<Rooms> rooms;
    private RoomAPI roomAPI;



    public MainFragment_Room_Adapter(Context context, List<Rooms> rooms) {
        this.context = context;
        this.rooms = rooms;
    }



    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.f1__x__main_fragment_room_card_view, parent, false);
        return new RoomViewHolder(view);
    }


    public void onBindViewHolder(@NonNull RoomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Conectar();
        SharedPreferences preferences = context.getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        int userId = preferences.getInt("id_user", 0);

        String roomName = rooms.get(position).getName_room();
        int numberOfPlayers = this.rooms.get(position).getUserCount();


        holder.roomNameTextView.setText(roomName);
        holder.numberOfPlayersTextView.setText(String.valueOf(numberOfPlayers));

        if (position % 2 == 0) {
            // Si la posición es par, establecer el fondo azul
            holder.roomNameTextView.setBackgroundColor(context.getResources().getColor(R.color.blue));
        } else {
            // Si la posición es impar, establecer el fondo rojo
            holder.roomNameTextView.setBackgroundColor(context.getResources().getColor(R.color.red));
        }





        // Agregar OnClickListener al TextView del nombre de la habitación
        holder.roomNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // Aquí abres el AlertDialog
                int roomId = rooms.get(position).getId_room();

                if(!rooms.get(position).getPass_room().isEmpty()){
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.f1__x__main_fragment_room_pin_alert, null);

                    // Construir el AlertDialog utilizando el diseño personalizado
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);

                    // Mostrar el AlertDialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    alertDialog.show();



                    // Obtener referencia al botón dentro del AlertDialog
                    MaterialButton enterRoomPinButton = dialogView.findViewById(R.id.enter_room_pin);
                    enterRoomPinButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextInputEditText Pin = dialogView.findViewById(R.id.pin_room_textEdittextlayout);
                            String pinValue = Pin.getText().toString();
                            // Cerrar el AlertDialog actual
                            alertDialog.dismiss();
                            joinRoom(roomId,userId,pinValue);
                            // Abrir el nuevo AlertDialog f3__x__fragment_alert_waiting_players




                        }
                    });

                }else{
                    joinRoom(roomId,userId,"");
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return rooms.size();
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

    public void joinRoom(Integer roomId, Integer userId, String password){
        Call<Void> call = roomAPI.joinRoom(roomId, userId, password);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("SQL","FUNCIONA");
                    // Abrir el nuevo AlertDialog f3__x__fragment_alert_waiting_players
                    Room();


                } else {
                    Log.e("SQL","NO FUNCIONA");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error de red o error al procesar la respuesta
                Log.e("SQL","ERROR");
            }
        });
    }

    public void endRoom(int roomId, int userId) {
        Call<Void> call = roomAPI.endRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "OperaciÃ³n completada correctamente");
                } else {
                    // OcurriÃ³ un error al intentar finalizar la sala
                    Log.e("RoomEnd", "Error al finalizar la sala: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // OcurriÃ³ un error de red tap_blue_card otro error durante la llamada
                Log.e("RoomEnd", "Error de red: " + t.getMessage());
            }
        });
    }
    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        roomAPI = retrofit.create(RoomAPI.class);
    }

    public void Room(){

        LayoutInflater inflater = LayoutInflater.from(context);
        View waitingPlayersDialogView = inflater.inflate(R.layout.f3__x__fragment_alert_waiting_players, null);

        RecyclerView recyclerView = waitingPlayersDialogView.findViewById(R.id.recycler_players);
        String[] usernames = {"TheRockex", "Spidey1912", "Lukinda551", "LordGrim551", "TuMama", "王八蛋"};


        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.f3__x__fragment_player_status_cardview, parent, false);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView playerName = holder.itemView.findViewById(R.id.player_name);
                ImageView readyicon = holder.itemView.findViewById(R.id.ready_status);
                MaterialButton exitstatus = holder.itemView.findViewById(R.id.exit_status);

                playerName.setText(usernames[position]);
                readyicon.setVisibility(View.VISIBLE);
                exitstatus.setVisibility(View.VISIBLE);
            }

            @Override
            public int getItemCount() {
                return usernames.length;
            }
        });


        // Construir el nuevo AlertDialog
        AlertDialog.Builder waitingPlayersBuilder = new AlertDialog.Builder(context);
        waitingPlayersBuilder.setView(waitingPlayersDialogView);

        // Mostrar el nuevo AlertDialog
        AlertDialog waitingPlayersAlertDialog = waitingPlayersBuilder.create();
        waitingPlayersAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        waitingPlayersAlertDialog.show();
        MaterialButton exitButton = waitingPlayersDialogView.findViewById(R.id.alert_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                waitingPlayersAlertDialog.dismiss();
            }
        });
        MaterialButton startButton = waitingPlayersDialogView.findViewById(R.id.alert_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, prueba.class);

                        // Verificar si la versión de Android es igual o superior a LOLLIPOP para manejar la transición de actividades
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startButton.setText("ESPERANDO AL ANFITRIÓN");
                            startButton.setTextSize(10);
                            //context.startActivity(intent);
                        } else {
                            // En versiones anteriores a LOLLIPOP, simplemente inicia la actividad
                            context.startActivity(intent);
                            // Si es necesario, finaliza la actividad actual
                            // ((Activity) context).finish();
                        }
                    }
                }, 400);
            }

        });
    }
}