package com.example.urchoice2.SQL;

import android.util.Log;

import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.Classes.RoomData;
import com.example.urchoice2.Classes.RoomGame;
import com.example.urchoice2.Classes.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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


    public long insertAndGetId(String query) {
        final long[] generatedId = {-1};
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                Connection con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        generatedId[0] = generatedKeys.getLong(1);
                        Log.e("SQL", "ID generado: " + generatedId[0]);
                    }
                    Log.e("SQL", "Inserción exitosa");
                } else {
                    Log.e("SQL", "La inserción no tuvo éxito");
                }
                con.close();
            } catch (SQLException e) {
                Log.e("SQL", "Error en la inserción: " + e.toString());
            }
        });
        return generatedId[0];
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

    public void GetUsers(String query, CrudCallback<List<User>> callback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<User> users = new ArrayList<>();
            try {
                con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id_user");
                    String email = rs.getString("email_user");
                    String nickname = rs.getString("nick_user");
                    String password = rs.getString("pass_user");
                    String img = rs.getString("img_user");
                    User user = new User(id, email, nickname, password, img);
                    users.add(user);
                }
                con.close();
                callback.onComplete(users);
            } catch (SQLException e) {
                Log.e("SQL", "Error al obtener usuarios: " + e.toString());
            }
        });
    }

    public void addFriend(int senderId, int receiverId) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            String sql = "INSERT INTO friends (id_us1, id_us2, estado) VALUES (?, ?, 'pendiente')";
            try {
                Connection con = conexionSQL.conexionDB();
                PreparedStatement statement = con.prepareStatement(sql);
                statement.setInt(1, senderId);
                statement.setInt(2, receiverId);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    Log.e("SQL", "Inserción exitosa");
                } else {
                    Log.e("SQL", "La inserción no tuvo éxito");
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateFriendRequestStatus(int requestId, int friendId, String newStatus) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                Connection con = conexionSQL.conexionDB();
                PreparedStatement stmt;

                if (newStatus.equals("Denegado")) {
                    // Si el nuevo estado es "Denegado", realiza un DELETE
                    String deleteQuery = "DELETE FROM friends WHERE id_us1 = ? AND id_us2 = ?";
                    stmt = con.prepareStatement(deleteQuery);
                    stmt.setInt(1, requestId);
                    stmt.setInt(2, friendId);
                } else {
                    String updateQuery = "UPDATE friends SET estado = ? WHERE id_us1 = ? AND id_us2 = ?";
                    stmt = con.prepareStatement(updateQuery);
                    stmt.setString(1, newStatus);
                    stmt.setInt(2, requestId);
                    stmt.setInt(3, friendId);
                }

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    Log.e("SQL", "Operación exitosa");
                } else {
                    Log.e("SQL", "La operación no tuvo éxito");
                }
                con.close();
            } catch (SQLException e) {
                Log.e("SQL", "Error en la operación: " + e.toString());
            }
        });
    }

    public void Update(String query){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                Connection con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement(query);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    Log.e("SQL", "Operación exitosa");
                } else {
                    Log.e("SQL", "La operación no tuvo éxito");
                }
                con.close();
            } catch (SQLException e) {
                Log.e("SQL", "Error en la operación: " + e.toString());
            }
        });
    }

    public void getUsersForRoom(int idRoom, Consumer<RoomData> callback) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<User> users = new ArrayList<>();
            List<RoomGame> roomGames = new ArrayList<>();

            try {
                Connection con = conexionSQL.conexionDB();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM room_game WHERE id_room = ?");
                stmt.setInt(1, idRoom);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int idGameRoom = rs.getInt("id_game_room");
                    int roomId = rs.getInt("id_room");
                    int gameId = rs.getInt("id_game");
                    int userId = rs.getInt("id_user");
                    String vote = rs.getString("vote");
                    RoomGame roomGame = new RoomGame(idGameRoom, roomId, gameId, userId, vote);
                    roomGames.add(roomGame);
                }

                List<Integer> userIds = new ArrayList<>();
                for (RoomGame roomGame : roomGames) {
                    userIds.add(roomGame.getId_user());
                }

                String userIdsString = userIds.stream().map(Object::toString).collect(Collectors.joining(", "));
                PreparedStatement stmt2 = con.prepareStatement("SELECT * FROM users WHERE id_user IN (" + userIdsString + ")");
                ResultSet rs2 = stmt2.executeQuery();

                while (rs2.next()) {
                    Integer id_user = rs2.getInt("id_user");
                    String email_user = rs2.getString("email_user");
                    String nick_user = rs2.getString("nick_user");
                    String pass_user = rs2.getString("pass_user");
                    String img_user = rs2.getString("img_user");
                    User user = new User(id_user, email_user, nick_user, pass_user, img_user);
                    users.add(user);
                }

                con.close();
            } catch (SQLException e) {
                Log.e("SQL", "Error en la consulta: " + e.toString());
            }

            RoomData roomData = new RoomData(users, roomGames);
            callback.accept(roomData);
        });
    }


}
