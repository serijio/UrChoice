package com.example.urchoice2.Screens_activities;

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
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElemCatAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.API.RoomGameAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.Fragments.MainFragment;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class prueba extends AppCompatActivity {

    //CrudSQL crud;

    private int currentRound = 0;

    private List<Element> shuffledElements = new ArrayList<>();
    private List<Element> winnerElements = new ArrayList<>();

    private TextView textViewElement1;
    private TextView textViewElement2;

    private ImageView imageViewElement1;
    private ImageView imageViewElement2;

    private Handler handler;

    private UserAPI userApi;
    private ElementsAPI elementApi;

    private CategoriesAPI categoriesAPI;

    private FriendsAPI friendsAPI;
    private RoomAPI roomAPI;
    private ElemCatAPI elemCatAPI;

    private Integer categoryId;
    private RoomGameAPI roomGameAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xx__fragment_multi_game_round_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        categoryId = sharedPreferences.getInt("id_categoryMulti", 0);
        Conectar();
        textViewElement1 = findViewById(R.id.card_name1);
        textViewElement2 = findViewById(R.id.card_name2);
        imageViewElement1 = findViewById(R.id.imageView1);
        imageViewElement2 = findViewById(R.id.imageView2);
        textViewElement1.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View v) {
                // Lógica para manejar la selección del primer element
                winnerElements.add(shuffledElements.get(currentRound * 2));
                currentRound++;
                startRound();
                //lo nuevo de Luca
                //AlertWaitingPlayers();
            }
        });

        textViewElement2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para manejar la selección del segundo element
                winnerElements.add(shuffledElements.get(currentRound * 2 + 1));
                currentRound++;
                startRound();
                //lo nuevo de Luca
                //AlertWinner();
            }
        });

    }

    /*
     //lo nuevo de Luca

    public void AlertWaitingPlayers(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.xx__fragment_multi_waiting_game_alert,null);
        AlertDialog alertDialog = builder.create();

        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

    }
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
    }

    public void changeToMainButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(prueba.this, MainScreen.class);
                Pair[] pairs = new Pair[0];



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(prueba.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }

        //lo nuevo de Luca
*/

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserAPI.class);
        elementApi = retrofit.create(ElementsAPI.class);
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        friendsAPI = retrofit.create(FriendsAPI.class);
        roomAPI = retrofit.create(RoomAPI.class);
        elemCatAPI = retrofit.create(ElemCatAPI.class);
        roomGameAPI = retrofit.create(RoomGameAPI.class);
        Game();
    }

    public void Ranking(View view){
        elementApi.getRanking(categoryId).enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elementList = response.body();
                    for (int i = 0; i < elementList.size();i++){
                        Log.e("URCHOICE","Elemento:" + elementList.get(i).getName_elem());
                    }
                } else {
                    Log.e("URCHOICE","ElementoERROR");
                }
            }

            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {
                Log.e("URCHOICE","ElementoERROR2");
            }
        });
    }

    public void Game(){
        elementApi.getElementsByCategory(categoryId).enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elementList = response.body();
                    Collections.shuffle(elementList);
                    shuffledElements = elementList;
                    startRound();
                } else {
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

    private void startRound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentRound < shuffledElements.size() / 2) {
                    Element firstElement = shuffledElements.get(currentRound * 2);
                    Element secondElement = shuffledElements.get(currentRound * 2 + 1);
                    // Mostrar los nombres de los elementos en los TextView correspondientes


                    Bitmap base64Image = base64ToBitmap(firstElement.img_elem);
                    Bitmap base64Image2 = base64ToBitmap(secondElement.img_elem);
                    imageViewElement1.setImageBitmap(base64Image);
                    imageViewElement2.setImageBitmap(base64Image2);
                    textViewElement1.setText(firstElement.name_elem);
                    textViewElement2.setText(secondElement.name_elem);
                } else if (shuffledElements.size() != 1) {
                    shuffledElements = new ArrayList<>(winnerElements);
                    winnerElements.clear();
                    currentRound = 0;
                    startRound();
                } else {
                    Toast.makeText(prueba.this , "Ha ganado" + shuffledElements.get(0).getName_elem(), Toast.LENGTH_SHORT).show();

                   /* Call<Void> call = elemCatAPI.updateElemCat(shuffledElements.get(0).getId_element(), categoryId, shuffledElements.get(0).getVictories());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // La actualización se realizó correctamente
                                Log.d("ElemCatUpdate", "Actualización exitosa");
                                Toast.makeText(prueba.this , "Ha ganado" + shuffledElements.get(0).getName_elem(), Toast.LENGTH_SHORT).show();
                            } else {
                                // Error en la respuesta del servidor
                                Log.e("ElemCatUpdate", "Error en la respuesta del servidor: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Error en la llamada (p. ej., problemas de red)
                            Log.e("ElemCatUpdate", "Error en la llamada: " + t.getMessage());
                        }
                    });*/
                }
            }
        });
    }





}