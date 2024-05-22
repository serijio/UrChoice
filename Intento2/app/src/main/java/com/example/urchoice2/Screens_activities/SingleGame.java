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

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleGame extends AppCompatActivity {
    private Handler handler;
    private int currentRound = 0;
    private List<Element> shuffledElements = new ArrayList<>();
    private List<Element> winnerElements = new ArrayList<>();
    private TextView textViewElement1;
    private TextView textViewElement2;
    private ImageView imageViewElement1;
    private ImageView imageViewElement2;
    private ElementsAPI elementApi;
    private Integer categoryId;
    private TextView winnerName;
    private ImageView winnerImage;
    private Integer userId;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(Looper.getMainLooper());
        setContentView(R.layout.xx__fragment_individual_game_round_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        categoryId = sharedPreferences.getInt("id_categorySingle", 0);
        waitAlertAltera();
        Conectar();
        textViewElement1 = findViewById(R.id.card_name1);
        textViewElement2 = findViewById(R.id.card_name2);
        imageViewElement1 = findViewById(R.id.imageView1);
        imageViewElement2 = findViewById(R.id.imageView2);

        //Eleccion del primer elemento
        textViewElement1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar la selección del primer element
                winnerElements.add(shuffledElements.get(currentRound * 2));
                currentRound++;
                startRound();
            }
        });

        //Eleccion del segundo elemento
        textViewElement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar la selección del segundo element
                winnerElements.add(shuffledElements.get(currentRound * 2 + 1));
                currentRound++;
                startRound();
            }
        });

    }

//Alert del ganador
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
        winnerImage.setImageBitmap(base64ToBitmap(shuffledElements.get(0).getImg_elem()));

    }

//Volver al main
    public void changeToMainButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SingleGame.this, MainScreen.class);
                Pair[] pairs = new Pair[0];
                SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("id_categorySingle");
                editor.apply();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SingleGame.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        }, 400);
    }

    //lo nuevo de Luca

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        elementApi = retrofit.create(ElementsAPI.class);
        SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        categoryId = sharedPreferences.getInt("id_categorySingle", 0);
        userId = sharedPreferences.getInt("id_user", 0);
        Game();
    }

//Coger los elementos de la categoria
    public void Game(){
        elementApi.getElementsByCategory(categoryId).enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elementList = response.body();
                    Collections.shuffle(elementList);
                    shuffledElements = elementList;
                    dismissWaitAlert();
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

//Metodo para empezar la ronda
    private void startRound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Si son los elmentos principales
                if (currentRound < shuffledElements.size() / 2) {
                    Element firstElement = shuffledElements.get(currentRound * 2);
                    Element secondElement = shuffledElements.get(currentRound * 2 + 1);
                    Bitmap base64Image = base64ToBitmap(firstElement.img_elem);
                    Bitmap base64Image2 = base64ToBitmap(secondElement.img_elem);
                    imageViewElement1.setImageBitmap(base64Image);
                    imageViewElement2.setImageBitmap(base64Image2);
                    textViewElement1.setText(firstElement.name_elem);
                    textViewElement2.setText(secondElement.name_elem);

                    //Si solo quedan los ganadores
                } else if (shuffledElements.size() != 1) {
                    shuffledElements = new ArrayList<>(winnerElements);
                    winnerElements.clear();
                    currentRound = 0;
                    if(shuffledElements.size() != 1){
                        showCountdownAlert();
                    }else{
                        startRound();
                    }

                    //Si solo queda el ganador
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


    //Alert para el comienzo de cada ronda
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