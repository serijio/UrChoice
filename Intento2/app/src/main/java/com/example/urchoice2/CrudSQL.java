package com.example.urchoice2;

import android.util.Log;
import android.view.View;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CrudSQL {

    ConexionClandestina conexionSQL = new ConexionClandestina();;
    Connection con;

    String name;

    public void conexion() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = conexionSQL.conexionDB();
                if (con == null) {
                    Log.e("SQL", "ERROR EN CON NULL");
                } else {
                    Log.e("SQL", "FUNCIONO");
                }
            } catch (Exception e) {
                Log.e("SQL", "ERROR EN CONEXION MAIN");
            }
        });
    }

    public void Get(String query) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                StringBuilder bstr = new StringBuilder("Users\n");
                while (rs.next()) {
                    bstr.append(rs.getString("nick_user")).append("\n");
                }
                name = bstr.toString();
            } catch (Exception e) {
                Log.e("SQL", "ERROR EN CONEXION MAIN" + e.toString());
            }
            Log.e("SQL", "HOLA" + name);
        });
    }


    public void getCount(String query, String email, Consumer<Integer> callback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                if (rs.next()) {
                    count = rs.getInt(1);
                }
                callback.accept(count);
            } catch (SQLException e) {
                Log.e("SQL", "Error al obtener el recuento de usuarios: " + e.toString());
            }
        });
    }


    public void insert(String query) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                Connection con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    Log.e("SQL", "Inserción exitosa");
                } else {
                    Log.e("SQL", "La inserción no tuvo éxito");
                }
                con.close();
            } catch (SQLException e) {
                Log.e("SQL", "Error en la inserción: " + e.toString());
            }
        });
    }

}
