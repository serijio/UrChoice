package com.example.urchoice2.SQL;

import android.util.Log;

import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.SQL.ConexionClandestina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CrudSQL {

    ConexionClandestina conexionSQL = new ConexionClandestina();;
    Connection con;

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

    public interface CrudCallback<T> {
        void onComplete(T result);
    }

    public void GetCategories(String query, CrudCallback<List<Category>> callback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<Category> categorias = new ArrayList<>();
            try {
                con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id_cat");
                    String name = rs.getString("name_cat");
                    String img = rs.getString("img_cat");
                    Category categoria = new Category(id, name, img);
                    categorias.add(categoria);
                }
                con.close();
                callback.onComplete(categorias);
            } catch (SQLException e) {
                Log.e("SQL", "Error al obtener categorías: " + e.toString());
            }
        });
    }




    public void getCount(String query, Consumer<Integer> callback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                int count = 0;
                if (rs.next()) {
                    count++;
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



    public void GetElements(String query, CrudCallback<List<Element>> callback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<Element> elementos = new ArrayList<>();
            try {
                con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id_elem");
                    String img = rs.getString("img_elem");
                    String name = rs.getString("name_elem");
                    int victories = rs.getInt("victories");
                    Element element = new Element(id, img, name, victories);
                    elementos.add(element);
                }
                con.close();
                callback.onComplete(elementos);
            } catch (SQLException e) {
                Log.e("SQL", "Error al obtener elementos: " + e.toString());
            }
        });
    }


}
