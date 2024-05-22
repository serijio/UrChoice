package com.example.urchoice2.Screens_activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.API.RoomGameAPI;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.Classes.UserVote;
import com.example.urchoice2.Classes.VoteCount;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MultiGame extends AppCompatActivity {

    private Handler handler;
    private boolean allVotesReceived = false;
    private int currentRound = 0;
    private List<Element> shuffledElements = new ArrayList<>();
    private List<Element> winnerElements = new ArrayList<>();

    private List<VoteCount> voteResults = new ArrayList<>();
    private TextView textViewElement1;
    private TextView textViewElement2;
    private ImageView imageViewElement1;
    private ImageView imageViewElement2;
    private ElementsAPI elementApi;
    private RoomAPI roomAPI;
    private RoomGameAPI roomGameAPI;
    private Integer categoryId;
    private Integer userId;
    private Integer roomId;
    private TextView winnerName;
    private ImageView winnerImage;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.xx__fragment_multi_game_round_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        categoryId = sharedPreferences.getInt("id_categoryMulti", 0);
        userId = sharedPreferences.getInt("id_user", 0);
        roomId = sharedPreferences.getInt("id_room", 0);
        waitAlertAltera();
        Conectar();
        textViewElement1 = findViewById(R.id.Multicard_name1);
        textViewElement2 = findViewById(R.id.Multicard_name2);
        imageViewElement1 = findViewById(R.id.MultiimageView1);
        imageViewElement2 = findViewById(R.id.MultiimageView2);

        //Votar al primer elemento
        textViewElement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar la selección del primer element
                Log.e("SQL","Tuyo" + shuffledElements.get(currentRound * 2).getName_elem());
                Vote(String.valueOf(shuffledElements.get(currentRound * 2).getName_elem()));
            }
        });

        //Votar al segundo elemento
        textViewElement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar la selección del segundo element
                Log.e("SQL","Tuyo" + shuffledElements.get(currentRound * 2 + 1).getName_elem());
                Vote(String.valueOf(shuffledElements.get(currentRound * 2 + 1).getName_elem()));
            }
        });

    }

//Layout donde se muestra al ganador
    public void AlertWinner() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.xx__fragment_game_win_alert_layout, null);

        ViewGroup winnerLayout = view.findViewById(R.id.winner_layout);
        View inflatedLayout = layoutInflater.inflate(R.layout.xx__activity_game_win_layout, null);
        winnerLayout.addView(inflatedLayout); // Agregar el layout inflado a winnerLayout

        AlertDialog alertDialog = builder.create();

        alertDialog.setView(view);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

        // Obtener referencia al botón "home" dentro del layout inflado
        MaterialButton homeButton = inflatedLayout.findViewById(R.id.main_home_materialbutton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToMainButton();
            }
        });
        winnerName = inflatedLayout.findViewById(R.id.winner_card_name);
        winnerImage = inflatedLayout.findViewById(R.id.winner_card_image);
        winnerName.setText(shuffledElements.get(0).getName_elem());
        winnerImage.setImageBitmap(base64ToBitmap(shuffledElements.get(0).getImg_elem()));;

    }

//Volver al layout main
    public void changeToMainButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MultiGame.this, MainScreen.class);
                Pair[] pairs = new Pair[0];

                endRoom(roomId,userId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MultiGame.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        elementApi = retrofit.create(ElementsAPI.class);
        roomAPI = retrofit.create(RoomAPI.class);
        roomGameAPI = retrofit.create(RoomGameAPI.class);

        Game();

    }

//Coger los elementos de la categoria a jugar
    public void Game(){
        elementApi.getElementsByCategory(categoryId).enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elementList = response.body();

                    shuffledElements = elementList;
                    Log.e("SQL", "GAMECONECT: " + shuffledElements.size());
                    startRound();
                }
            }
            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {
            }
        });
    }


    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

//Empezar ronda
    private void startRound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissWaitAlert();
                //Empezar ronda entre los elementos iniciales
                if (currentRound < shuffledElements.size() / 2) {
                    //showCountdownAlert();
                    Element firstElement = shuffledElements.get(currentRound * 2);
                    Element secondElement = shuffledElements.get(currentRound * 2 + 1);
                    // Mostrar los nombres de los elementos en los TextView correspondientes
                    Bitmap base64Image = base64ToBitmap(firstElement.img_elem);
                    Bitmap base64Image2 = base64ToBitmap(secondElement.img_elem);
                    imageViewElement1.setImageBitmap(base64Image);
                    imageViewElement2.setImageBitmap(base64Image2);
                    textViewElement1.setText(firstElement.name_elem);
                    textViewElement2.setText(secondElement.name_elem);
                    //Solo quedan los elementos ganadores
                } else if (shuffledElements.size() != 1) {
                    shuffledElements = new ArrayList<>(winnerElements);
                    Collections.sort(shuffledElements, Comparator.comparing(Element::getName_elem));
                    winnerElements.clear();
                    currentRound = 0;
                    if(shuffledElements.size() != 1){
                        showCountdownAlert();
                    }else{
                        startRound();
                    }
                    //Solo queda el ganador
                } else {
                    Call<Void> call = elementApi.updateElement(shuffledElements.get(0).getId_elem(), shuffledElements.get(0).getVictories(),userId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                AlertWinner();
                            } else {
                                // Manejar el error de la respuesta
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Manejar el error de la llamada
                        }
                    });
                }
            }
        });
    }

//Dialog de empezar ronda
    private void showCountdownAlert() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.ff___xx_alert__countdown, null);

        //cargar gif
        ImageView countdownImageView = view.findViewById(R.id.countdownImageView);
        Glide.with(this).asGif().load(R.drawable.countdown).into(countdownImageView);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

        handler.postDelayed(() -> {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
                startRound();
            }
        }, 5000);
    }

    private void dismissAlert() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

//Metodo para subir tu voto
    public void Vote(String voto) {
        //waitAlert();
        waitAlertAltera();
        Call<Void> call = roomGameAPI.updateVote(roomId, userId, voto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    allVotesReceived = false;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startRepeatedCall();
                        }
                    }, 1000);
                } else {
                    Log.e("SQL", "ERRORUP: " + response.message());

                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL", "ERRORUP2: " + t.getMessage());
            }
        });
    }

//Poner en bucle para sacar los votos de los jugadores
    private void startRepeatedCall() {
        Log.e("SQL","ENTRO AL CALL: " + allVotesReceived );
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!allVotesReceived) {
                    GetUsers();
                    handler.postDelayed(this, 1000);
                } else {
                    // Todos los votos han sido recibidos, detener la llamada repetida
                    handler.removeCallbacksAndMessages(null);
                }
            }
        }, 1000);
    }

//Sacar los votos de los usuarios
    public void GetUsers() {
        Call<List<UserVote>> call = roomAPI.getUsersInRoom(roomId);
        call.enqueue(new Callback<List<UserVote>>() {
            @Override
            public void onResponse(Call<List<UserVote>> call, Response<List<UserVote>> response) {
                if (response.isSuccessful()) {
                    List<UserVote> users = response.body();
                    boolean allVotesReceivedTemp = true;
                    for (UserVote user : users) {
                        if (user.getVote_game() == null || user.getVote_game().isEmpty() || user.getVote_game().equals("LISTO") || user.getVote_game().equals(" ") || user.getVote_game().equals(" ")) {
                            allVotesReceivedTemp = false;
                            break;
                        }
                    }
                    allVotesReceived = allVotesReceivedTemp;
                    if (allVotesReceived) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                WinnerRound();
                            }
                        }, 2000);
                    }
                } else {
                    Log.e("SQL","ERRORGET");
                }
            }
            @Override
            public void onFailure(Call<List<UserVote>> call, Throwable t) {
                Log.e("SQL","ERRORGET2");
            }
        });
    }

//Metodo recibir el elemento ganador
    public void WinnerRound(){
        Call<JsonObject> call = roomAPI.getWinnerRound(roomId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    String mostVotedGame = jsonObject.get("mostVotedGame").getAsString();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VoteClear(" ",mostVotedGame);
                        }
                    }, 5000);

                } else {
                    Log.e("SQL", "ERRORW");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SQL", "ERRORW2");
            }
        });
    }

//Metodo para limpiar el voto
    public void VoteClear(String voto, String mostVotedGame) {
        Call<Void> call = roomGameAPI.updateVote(roomId, userId, voto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    if(String.valueOf(shuffledElements.get(currentRound * 2).getName_elem()).equals(mostVotedGame)){
                        winnerElements.add(shuffledElements.get(currentRound * 2));
                    } else {
                        winnerElements.add(shuffledElements.get(currentRound * 2 + 1));
                    }
                    currentRound++;
                    startRound();
                } else {
                    Log.e("SQL", "ERRORUPC: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL", "ERRORUP2C: " + t.getMessage());
            }
        });
    }

//Metodo para acabar la sala
    public void endRoom(int roomId, int userId) {
        Call<Void> call = roomAPI.endRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("id_categoryMulti");
                    editor.remove("id_room");
                    editor.apply();
                    Log.d("RoomEnd", "OperaciÃ³n completada correctamente");
                } else {
                    Log.e("RoomEnd", "Error al finalizar la sala: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RoomEnd", "Error de red: " + t.getMessage());
            }
        });
    }


    //Wait de altera para que carguen los datos
    public void waitAlertAltera() {
        LayoutInflater inflater = LayoutInflater.from(this);  // Utiliza 'this' en lugar de 'requireContext()'
        View view = inflater.inflate(R.layout.ff___all_fragments_loading_alert_dialog_altera, null);

        // Cargar gif
        ImageView alteraImageView = view.findViewById(R.id.altera);
        Glide.with(this).asGif().load(R.drawable.altera_final).into(alteraImageView);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  // Utiliza 'this' en lugar de 'requireContext()'
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }



    public void dismissWaitAlert() {
        alertDialog.dismiss();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        // Dejar vacío para deshabilitar el botón de retroceso
    }
}