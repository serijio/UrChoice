package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElemCatAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.API.RoomGameAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.Classes.RoomData;
import com.example.urchoice2.Classes.RoomGame;
import com.example.urchoice2.Classes.User;
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

public class MainActivity extends AppCompatActivity {
    CrudSQL crud;

    private int currentRound = 0;

    private List<Element> shuffledElements = new ArrayList<>();
    private List<Element> winnerElements = new ArrayList<>();

    private TextView textViewElement1;
    private TextView textViewElement2;
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
        setContentView(R.layout.activity_main);
        crud = new CrudSQL();
        crud.conexion();
        Conectar();
        textViewElement1 = findViewById(R.id.textViewElement1);
        textViewElement2 = findViewById(R.id.textViewElement2);
        textViewElement1.setOnClickListener(new View.OnClickListener() {
            @Override
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


    /*public void Login(View view){
        String email_user = "SergioGey@gey.com";
        String nick_user = "SergioGey";
        String pass_user = "123";
        String img = "";

        String checkIfExistsQuery = "SELECT COUNT(*) FROM users WHERE email_user = '" + email_user + "'";

        crud.getCount(checkIfExistsQuery, count -> {
            if (count == 0) {
                String insertQuery = "INSERT INTO users VALUES(" +
                        "'0','" + email_user + "','" + nick_user + "','" + pass_user + "','" + img + " ');";
                crud.insert(insertQuery);
            } else {
                Log.e("SQL", "Ya existe un usuario con ese correo electrónico");
            }
        });
    }

   public void Iniciar(View view){
        String email_user = "SergioGey@gey.com";
        String nick_user = "SergioGey";
        String pass_user = "123";
        String img = "";

        String checkIfExistsQuery = "SELECT COUNT(*) FROM users WHERE email_user = '" + email_user + "' AND pass_user = '" + pass_user +  "'";
        crud.getCount(checkIfExistsQuery, count -> {
            if (count == 0) {
                Toast.makeText(this, "No existe un usuario con ese gmail", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);

            }
        });
    }

    public void GetCategorias(View view) {
        String sql = "SELECT * FROM categories";
        crud.GetCategories(sql, new CrudSQL.CategoriesCallback() {
            @Override
            public void onCategoriesReceived(List<Category> categories) {
                // Ahora puedes trabajar con la lista de categorías
                for (Category categoria : categories) {
                    Log.d("Categoria", "ID: " + categoria.id_cat + ", Nombre: " + categoria.name_cat + ", Imagen: " + categoria.img_cat);
                }
            }
        });
    }


    public void GetUsers(View view) {
        String sql = "SELECT * FROM users";
        crud.GetUsers(sql, this::handleUsers);
    }


   public void Ranking(View view) {
        Integer id_cat = 1;
        String sql = "SELECT e.id_elem, e.img_elem, e.name_elem, ec.victories FROM elements e INNER JOIN elemcat ec ON e.id_elem = ec.id_elem " +
                "INNER JOIN categories c ON ec.id_cat = c.id_cat WHERE c.id_cat = '" + id_cat + "' ORDER BY ec.victories DESC;";
        crud.GetElements(sql, this::handleElements);
    }

     public void GetCategorias(View view) {
        String sql = "SELECT * FROM categories";
        crud.GetCategories(sql, this::handleCategories);
    }
    */

    public void GetUsers(View view){
        userApi.getUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> userList = response.body();
                    for (int i = 0; i < userList.size();i++){
                        Log.e("URCHOICE","User:" + userList.get(i).getNick_user());
                    }
                } else {
                    Log.e("URCHOICE","UserERROR");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
            }
        });
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
                                Toast.makeText(MainActivity.this, "Ha ganado" + shuffledElements.get(0).getName_elem(), Toast.LENGTH_SHORT).show();
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

    public void GetCategories(){
        categoriesAPI.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categoryList = response.body();
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
            }
        });

    }

    public void SendRequestFriend(View view){
        Integer id_us1 = 4;
        Integer id_us2 = 5;
        friendsAPI.addFriend(id_us1, id_us2).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("URCHOICE","Solicitud enviada");

                } else {
                    Log.e("URCHOICE","SENDERROR");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("URCHOICE","SENDERROR2");
            }
        });

    }

    public void AcceptFriend(View view){
        Integer id_us1 = 4;
        Integer id_us2 = 5;
        String nuevoEstado = "Aceptada";
        friendsAPI.updateFriendRelation(id_us1, id_us2, nuevoEstado).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("URCHOICE","Solicitud aceptada");
                } else {
                    Log.e("URCHOICE","ACCEPTERROR");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("URCHOICE","ACCEPTERROR2");
            }
        });

    }

    public void DeniedFriend(View view){
        Integer id_us1 = 4;
        Integer id_us2 = 5;
        String nuevoEstado = "Denegado";
        friendsAPI.updateFriendRelation(id_us1, id_us2, nuevoEstado).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("URCHOICE","Solicitud rechazada");
                } else {
                    // Manejar el error de respuesta
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Manejar el fallo de la llamada
            }
        });

    }



    public void createRoom(int categoryId, int userId) {
        Call<Void> call = roomAPI.createRoom(categoryId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomCreation", "Nueva sala creada correctamente");
                } else {
                    // Ocurrió un error al intentar crear la sala
                    Log.e("RoomCreation", "Error al crear la sala: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RoomCreation", "Error de red: " + t.getMessage());
            }
        });
    }


    public void joinRoom(){
        Integer userId = 5;
        Integer roomId = 1;
        Call<Void> call = roomGameAPI.createRoomGame(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomGameCreation", "Nuevo juego de sala creado correctamente");
                } else {
                    Log.e("RoomGameCreation", "Error al crear el juego de sala: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Ocurrió un error de red tap_blue_card otro error durante la llamada
                Log.e("RoomGameCreation", "Error de red: " + t.getMessage());
            }
        });
    }



    public void StartGame(){
        Integer userId = 5;
        Integer roomId = 1;

        Call<Void> call = roomAPI.startRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomStart", "Se ha iniciado la sala correctamente");
                    // Aquí puedes agregar lógica adicional si es necesario
                } else {
                    Log.e("RoomStart", "Error al iniciar la sala: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RoomStart", "Error de red: " + t.getMessage());
            }
        });
    }


    public void endRoom(int roomId, int userId) {
        Call<Void> call = roomAPI.endRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "Operación completada correctamente");
                } else {
                    // Ocurrió un error al intentar finalizar la sala
                    Log.e("RoomEnd", "Error al finalizar la sala: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Ocurrió un error de red tap_blue_card otro error durante la llamada
                Log.e("RoomEnd", "Error de red: " + t.getMessage());
            }
        });
    }
}