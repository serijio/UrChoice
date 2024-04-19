package com.example.urchoice2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    CrudSQL crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crud = new CrudSQL();
        crud.conexion();
    }

public void GetUser(View view){
        crud.Get("SELECT * FROM users");
}

    public void Login(View view){
        String email_user = "SergioGey@gey.com";
        String nick_user = "SergioGey";
        String pass_user = "123";
        String img = "";

        // Verificar si el usuario ya existe
        String checkIfExistsQuery = "SELECT COUNT(*) FROM users WHERE email_user = ?";
        crud.getCount(checkIfExistsQuery, email_user, count -> {
            if (count == 0) {
                // No hay usuarios con ese correo electrónico, se puede insertar
                String insertQuery = "INSERT INTO users VALUES(" +
                        "'0','" + email_user + "','" + nick_user + "','" + pass_user + "','" + img + " ');";
                crud.insert(insertQuery);
            } else {
                // Ya existe un usuario con ese correo electrónico, manejar en consecuencia
                Log.e("SQL", "Ya existe un usuario con ese correo electrónico");
            }
        });
    }

}
