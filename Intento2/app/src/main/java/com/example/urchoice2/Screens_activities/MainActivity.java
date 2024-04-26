package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.Classes.RoomData;
import com.example.urchoice2.Classes.RoomGame;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.HttpTask;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crud = new CrudSQL();
        crud.conexion();
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

public void GetUsers(){
    userApi.getUsers().enqueue(new Callback<List<User>>() {
        @Override
        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
            if (response.isSuccessful()) {
                List<User> userList = response.body();
            } else {
            }
        }

        @Override
        public void onFailure(Call<List<User>> call, Throwable t) {
        }
    });

}

public void Ranking(){
    Integer categoryId = 1;
    elementApi.getElementsByCategory(categoryId).enqueue(new Callback<List<Element>>() {
        @Override
        public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
            if (response.isSuccessful()) {
                List<Element> elementList = response.body();
            } else {
            }
        }

        @Override
        public void onFailure(Call<List<Element>> call, Throwable t) {
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


public void SendRequestFriend(){
    Integer id_us1 = 4;
    Integer id_us2 = 5;
    friendsAPI.addFriend(id_us1, id_us2).enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
            } else {
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
        }
    });

}

public void Friend(){
    Integer id_us1 = 4;
    Integer id_us2 = 5;
    String nuevoEstado = "Aceptada";
    friendsAPI.updateFriendRelation(id_us1, id_us2, nuevoEstado).enqueue(new Callback<Void>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if (response.isSuccessful()) {
                // La actualización fue exitosa
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
    public void handleCategories(List<Category> categories) {
        Log.e("SQL", "Error al obtener categorías: " + categories.size());
        // Ahora puedes trabajar con la lista de categorías
        for (Category categoria : categories) {
            // Hacer algo con cada categoría, por ejemplo, imprimir sus detalles
            Log.d("Categoria", "ID: " + categoria.id_cat + ", Nombre: " + categoria.name_cat + ", Imagen: " + categoria.img_cat);
        }
    }

    public void handleElements(List<Element> elements) {
        Log.e("SQL", "Error al obtener elementos: " + elements.size());
        // Ahora puedes trabajar con la lista de elementos
        for (Element element : elements) {
            // Hacer algo con cada elemento, por ejemplo, imprimir sus detalles
            Log.d("Elemento", "ID: " + element.id_elem + ", Nombre: " + element.name_elem + ", Imagen: " + element.img_elem + ", Victorias: " + element.victories);
        }
    }

    public void handleUsers(List<User> users) {
        Log.e("SQL", "Error al obtener elementos: " + users.size());
        // Ahora puedes trabajar con la lista de elementos
        for (User user : users) {
            // Hacer algo con cada elemento, por ejemplo, imprimir sus detalles
            Log.d("Usuario", "ID: " + user.id_user + ", GMAIL: " + user.email_user + ", NICK: " + user.nick_user + ", PASSWORD: " + user.pass_user + ",IMG:" + user.img_user);
        }
    }

    public void handleElementsGame(List<Element> elements) {
        Log.e("SQL", "Error al obtener elementos: " + elements.size());
        // Ahora puedes trabajar con la lista de elementos
        Collections.shuffle(elements);
        for (Element element : elements) {
            // Hacer algo con cada elemento, por ejemplo, imprimir sus detalles
            Log.d("Elemento", "ID: " + element.id_elem + ", Nombre: " + element.name_elem + ", Imagen: " + element.img_elem + ", Victorias: " + element.victories);
        }
        shuffledElements = elements;
        startRound();
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
                    String sql = "UPDATE elemcat SET victories = '" + (shuffledElements.get(0).getVictories() + 1) + "' WHERE id_elem = '" + shuffledElements.get(0).getId_element() + "' " +
                            "AND id_cat = '" + id_cat + "'";

                    crud.Update(sql);
                    Toast.makeText(MainActivity.this, "Ha ganado" + shuffledElements.get(0).getName_elem(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Game(View view) {
        Integer id_cat = 1;
        SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id_cat", id_cat);
        editor.apply();
        String sql = "SELECT e.id_elem, e.img_elem, e.name_elem, ec.victories FROM elements e INNER JOIN elemcat ec ON e.id_elem = ec.id_elem " +
                "INNER JOIN categories c ON ec.id_cat = c.id_cat WHERE c.id_cat = '" + id_cat + "'";
        crud.GetElements(sql, this::handleElementsGame);
    }

    public void SendRequestFriend(View view){
        int id_user = 4;
        int id_friend_user = 5;
        crud.addFriend(id_user,id_friend_user);
    }

    public void AcceptFriend(View view){
        int id_user = 4;
        int id_friend_user = 5;
        String estado = "Aceptado";
        crud.updateFriendRequestStatus(id_user,id_friend_user,estado);
    }

    public void DenniedFriend(View view){
        int id_user = 4;
        int id_friend_user = 5;
        String estado = "Denegado";
        crud.updateFriendRequestStatus(id_user,id_friend_user,estado);
    }


    public void CreateRoom(View view){
        String password = "";
        String category = "1";
        String queryRoom = "INSERT room VALUES(" +
                "'0','" + password + "','NO LISTO','" + category + "');";
        long roomId = crud.insertAndGetId(queryRoom);


        Integer id_user = 4;
        String queryRoom_Game = "INSERT room_game VALUES('0','" + roomId + "','"  + id_user + "','');";
        crud.insert(queryRoom_Game);
    }


    public void JoinGame(View view){

        /*if(!passwordroom.isEmpty()){
            String passwordtry = "";
            if(passwordtry.equals(passwordroom)){
             String id_room = "1";
            String id_user = "4";
            String queryRoom_Game = "INSERT room_game VALUES('0','" + id_room + "','" + id_user + "','');";
            crud.insert(queryRoom_Game);
            }else{
                Toast.makeText(this, "Contraseña Errónea", Toast.LENGTH_SHORT).show();
            }
        }else{
           String id_room = "1";
        String id_user = "4";
        String queryRoom_Game = "INSERT room_game VALUES('0','" + id_room + "','" + id_user + "','');";
        crud.insert(queryRoom_Game);
        }*/


        String id_room = "1";
        String id_user = "4";
        String queryRoom_Game = "INSERT room_game VALUES('0','" + id_room + "','" + id_user + "','');";
        crud.insert(queryRoom_Game);
    }


    private void getUsersRoom() {
        int id_room = 1;
        crud.getUsersForRoom(id_room, this::setHandleroom);
    }

    public void setHandleroom(RoomData roomData) {
        List<User> users = roomData.getUsers();
        List<RoomGame> roomGames = roomData.getRoomGames();
        Boolean jugar = true;

        Log.e("SQL", "Cantidad de users: " + users.size());
        for (User user : users) {
            Log.d("Usuario", "ID: " + user.id_user + ", GMAIL: " + user.email_user + ", NICK: " + user.nick_user + ", PASSWORD: " + user.pass_user + ",IMG:" + user.img_user);
        }

        Log.e("SQL", "Cantidad de registros de room_game: " + roomGames.size());
        for (RoomGame roomGame : roomGames) {
            Log.d("RoomGame", "ID: " + roomGame.getId_game_room());
            if(roomGame.getVote().equals("NO LISTO")) {
                jugar = false;
            }
        }

        String sql = "UPDATE room SET status = 'CERRADA' WHERE id_room = '" + roomGames.get(0).getId_room() + "'";
        crud.Update(sql);

        /*if(jugar == true) {
            Toast.makeText(this, "Todos los jugadores están listos", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        }*/
    }
    private void startFetchingData() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getUsersRoom();
                handler.postDelayed(this, 5000);
            }
        }, 5000);
    }

    private void stopFetchingData() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null); // Detiene la ejecución del Runnable
            handler = null;
        }
    }


    public void StartGame(){
        int id_user = 4;
        int id_room = 1;
        String sql = "UPDATE room_game SET vote = 'LISTO' WHERE id_room = '" + id_room +
                "' AND id_user = '" + id_user + "'";
        crud.Update(sql);
    }
}
