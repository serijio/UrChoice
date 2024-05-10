package com.example.urchoice2.Screens_activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElemCatAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.API.RoomGameAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;
import com.example.urchoice2.SQL.CrudSQL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class prueba extends AppCompatActivity {

    CrudSQL crud;

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

    private RoomGameAPI roomGameAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xx__fragment_multi_game_round_layout);
        crud = new CrudSQL();
        crud.conexion();
        Conectar();
        textViewElement1 = findViewById(R.id.card_name1);
        textViewElement2 = findViewById(R.id.card_name2);
        textViewElement1.setOnClickListener(new View.OnClickListener() {
//            @Override
            public void onClick(View v) {
                // Lógica para manejar la selección del primer element
                winnerElements.add(shuffledElements.get(currentRound * 2));
                currentRound++;
                startRound();
            }
        });

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
    }

    public void Ranking(View view){
        Integer categoryId = 1;
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

    public void Game(View view){
        Integer categoryId = 1;
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


    private void startRound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentRound < shuffledElements.size() / 2) {
                    Element firstElement = shuffledElements.get(currentRound * 2);
                    Element secondElement = shuffledElements.get(currentRound * 2 + 1);
                    // Mostrar los nombres de los elementos en los TextView correspondientes
                    textViewElement1.setText(firstElement.name_elem);
                    textViewElement2.setText(secondElement.name_elem);
                } else if (shuffledElements.size() != 1) {
                    shuffledElements = new ArrayList<>(winnerElements);
                    winnerElements.clear();
                    currentRound = 0;
                    startRound();
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                    int id_cat = sharedPreferences.getInt("id_cat", 0);

                    Call<Void> call = elemCatAPI.updateElemCat(shuffledElements.get(0).getId_element(), id_cat, shuffledElements.get(0).getVictories());
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
                    });
                }
            }
        });
    }





}
