package com.example.urchoice2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.urchoice2.Classes.Rooms;
import com.example.urchoice2.Classes.UserVote;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.MultiGame;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment_Room_Adapter extends RecyclerView.Adapter<MainFragment_Room_Adapter.RoomViewHolder> {
    private Context context;
    private boolean shouldUpdate = true;
    private List<Rooms> rooms;
    private RoomAPI roomAPI;
    private List<UserVote> userVotes  = new ArrayList<>();
    private int roomId;
    private int userId;

    public MainFragment_Room_Adapter(Context context, List<Rooms> rooms) {
        this.context = context;
        this.rooms = rooms;
    }


    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_home_rooms, parent, false);
        return new RoomViewHolder(view);
    }


    public void onBindViewHolder(@NonNull RoomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Conectar();
        SharedPreferences preferences = context.getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);

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
                roomId = rooms.get(position).getId_room();
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("id_categoryMulti", rooms.get(position).getId_cat());
                editor.apply();

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

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        roomAPI = retrofit.create(RoomAPI.class);
    }

    public void Room(){
        shouldUpdate = true;;
        LayoutInflater inflater = LayoutInflater.from(context);
        View waitingPlayersDialogView = inflater.inflate(R.layout.f3__x__fragment_alert_waiting_players, null);

        RecyclerView recyclerView = waitingPlayersDialogView.findViewById(R.id.recycler_players);

        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_status_room, parent, false);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView playerName = holder.itemView.findViewById(R.id.player_name);
                ImageView readyicon = holder.itemView.findViewById(R.id.ready_status);
                MaterialButton exitstatus = holder.itemView.findViewById(R.id.exit_status);
                playerName.setText(userVotes.get(position).getNick_user());
                boolean admin = false;

                for(int i = 0;i < userVotes.size();i++){
                    if(userVotes.get(i).getId_user() == userId && userVotes.get(i).getAdmin() == 1){
                        admin = true;
                    }
                }

                if(userVotes.get(position).getVote_game().equals("LISTO")){
                    readyicon.setVisibility(View.VISIBLE);
                }
                if(admin == true){
                    exitstatus.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public int getItemCount() {
                return userVotes.size();
            }
        });
        UsersRoom(recyclerView.getAdapter());

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
                shouldUpdate = false;
                endRoom(roomId,userId);
                waitingPlayersAlertDialog.dismiss();
            }
        });

        MaterialButton startButton = waitingPlayersDialogView.findViewById(R.id.alert_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent2 = new Intent(context, MultiGame.class);
                        // Verificar si la versión de Android es igual o superior a LOLLIPOP para manejar la transición de actividades
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            shouldUpdate = false;
                            startButton.setText("ESPERANDO");
                            startButton.setTextSize(10);
                            Listo(roomId, userId, new RoomClosedListener() {
                                @Override
                                public void onRoomClosed() {
                                    shouldUpdate = false;
                                    // Muestra un Toast informando al usuario que la sala se ha cerrado correctamente
                                    context.startActivity(intent2);
                                }
                            });

                        } else {
                            Listo(roomId, userId, new RoomClosedListener() {
                                @Override
                                public void onRoomClosed() {
                                    // Muestra un Toast informando al usuario que la sala se ha cerrado correctamente
                                    context.startActivity(intent2);
                                }
                            });
                        }
                    }
                }, 400);
            }
        });
    }

    public void UsersRoom(RecyclerView.Adapter adapter) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!shouldUpdate) {
                    // Si la variable shouldUpdate es falsa, cancelar la tarea del temporizador
                    timer.cancel();
                    return;
                }
                Call<List<UserVote>> call = roomAPI.getUsersInRoom(roomId);
                call.enqueue(new Callback<List<UserVote>>() {
                    @Override
                    public void onResponse(Call<List<UserVote>> call, Response<List<UserVote>> response) {
                        if (response.isSuccessful()) {
                            userVotes = response.body();
                            List<UserVote> newNames = new ArrayList<>();
                            for (UserVote userVote : userVotes) {
                                newNames.add(userVote);
                                Log.e("SQL", "User: " + userVote.getNick_user());
                            }
                            updateAdapter(newNames,adapter); // Actualiza el adaptador con los nuevos nombres de usuario
                        } else {
                            // Manejar respuesta no exitosa
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserVote>> call, Throwable t) {
                        // Manejar error de red u otro error
                    }
                });
            }
        }, 0, 5000); // Ejecutar la tarea cada 5 segundos
    }

    private void updateAdapter(List<UserVote> userVote, RecyclerView.Adapter adapter) {
        if (!shouldUpdate) {
            return;
        }
        userVotes.clear();
        userVotes.addAll(userVote);
        adapter.notifyDataSetChanged();
    }

    public void endRoom(int roomId, int userId) {
        Call<Void> call = roomAPI.endRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "OperaciÃ³n completada correctamente");
                } else {
                    // Ocurría un error al intentar finalizar la sala
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

    public void Listo(int roomId, int userId, final RoomClosedListener listener){
        Call<Void> call = roomAPI.startRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "Operación completada correctamente");
                    // Llama al método onRoomClosed del listener cuando la sala se cierra correctamente
                    listener.onRoomClosed();
                } else {
                    // Ocurrió un error al intentar finalizar la sala
                    Log.e("RoomEnd", "Error al finalizar la sala: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Ocurrió un error de red o error al procesar la respuesta
                Log.e("RoomEnd", "Error de red: " + t.getMessage());
            }
        });
    }

    public interface RoomClosedListener {
        void onRoomClosed();
    }
}