package com.example.urchoice2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.SQL.CrudSQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CrudSQL crud;

    private int currentRound = 0;

    private List<Element> shuffledElements = new ArrayList<>();
    private List<Element> winnerElements = new ArrayList<>();

    private TextView textViewElement1;
    private TextView textViewElement2;

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

    public void Login(View view){
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
                /*Intent intent = new Intent(this, SecondActivity.class);
                startActivity(intent);*/

            }
        });
    }

    /*public void GetCategorias(View view) {
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
    }*/

    public void GetCategorias(View view) {
        String sql = "SELECT * FROM categories";
        crud.GetCategories(sql, this::handleCategories);
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
                    Toast.makeText(MainActivity.this, "Ha ganado" + shuffledElements.get(0).getName_elem(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void Ranking(View view) {
        Integer id_cat = 1;
        String sql = "SELECT e.id_elem, e.img_elem, e.name_elem, ec.victories FROM elements e INNER JOIN elemcat ec ON e.id_elem = ec.id_elem " +
                "INNER JOIN categories c ON ec.id_cat = c.id_cat WHERE c.id_cat = '" + id_cat + "' ORDER BY ec.victories DESC;";
        crud.GetElements(sql, this::handleElements);
    }

    public void Game(View view) {
        Integer id_cat = 1;
        String sql = "SELECT e.id_elem, e.img_elem, e.name_elem, ec.victories FROM elements e INNER JOIN elemcat ec ON e.id_elem = ec.id_elem " +
                "INNER JOIN categories c ON ec.id_cat = c.id_cat WHERE c.id_cat = '" + id_cat + "'";
        crud.GetElements(sql, this::handleElementsGame);
    }



}
