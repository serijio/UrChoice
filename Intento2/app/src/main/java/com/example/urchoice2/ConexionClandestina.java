package com.example.urchoice2;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ConexionClandestina extends AsyncTask<Void,Integer,Boolean> {

    String queryResult; //esto es pa meterle el valor en String de cualquier consulta tipo SELECT

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            /*En railway, es la que aparece en "Settings" en el apartado de NetWorking, poniéndole delante "jdbc:mysql://"
            y detrás el nombre que aparece en "Variables" como "MYSQLDATABASE" con una barra delante, en este caso, "/railway" */
            String connectionString = "jdbc:mysql://monorail.proxy.rlwy.net:45523/railway";

            /*Lo que aparece como "MYSQLUSER" en la parte de "Variables", es el súper usuario, lo mantenemos siempre así */
            String user = "root";

            /*Lo que aparece como "MYSQL_ROOT_PASSWORD" en la parte de "Variables" */
            String password = "vDemhtCuWGRNzlYnkENndIbEtZsnnylA";

            Connection con = DriverManager.getConnection(connectionString, user, password);
            if (con == null)
            {
                return false;
            }

            /*Consulta tipo SELECT, tiene:
                - Statement: pa crear la consulta
                - ResultSet: pa decirle de que va la consulta, siempre es "executeQuery"
                - StringBuilder: pa convertir lo que devuelva el SELECT a un String
                - */
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            StringBuilder resultStringBuilder = new StringBuilder();

            while (resultSet.next()) {
                String id_user = resultSet.getString("id_user");
                String email_user = resultSet.getString("email_user");
                String nick_user = resultSet.getString("nick_user");
                String pass_user = resultSet.getString("pass_user");
                String img_user = resultSet.getString("img_user");
                resultStringBuilder.append(id_user).append("\n");
                resultStringBuilder.append(email_user).append("\n");
                resultStringBuilder.append(nick_user).append("\n");
                resultStringBuilder.append(pass_user).append("\n");
                resultStringBuilder.append(img_user).append("\n");
            }

            // Asignarle al String el valor de la consulta
            queryResult = resultStringBuilder.toString();

            /*Consulta tipo INSERT o DELETE, tiene:
                - Statement: pa crear la consulta
                - int: siempre va con el "executeUpdate"

                Ejemplos de consultas:
                  Statement statement2 = con.createStatement();
                  int rowsAffected2 = statement2.executeUpdate("INSERT INTO users VALUES(" +
                                                              "'2','alexgei','supergei','1234','');");


                  Statement statement3 = con.createStatement();
                  int rowsAffected3 = statement3.executeUpdate("INSERT INTO users VALUES(" +
                                                               "'3','lucajin','pelucas','1234','');");
                */

            Statement statement4 = con.createStatement();
            int rowsAffected4 = statement4.executeUpdate("INSERT INTO users VALUES(" +
                    "'4','kike','kiko','rocky','');");

            if (rowsAffected4 > 0) {
                // Pa que luego pongamos el típico mensaje de "Cuenta creada"
                Log.d("ANIADIR", "Mu bien, has agregao a Alex gei");
            } else {
                // Por ejemplo pa decirle "Tienes que meter todos tus datos mimimimi"
                Log.d("ANIADIR", "Alex es tan gei que no se deja agregar :(");
            }

        } catch (NoClassDefFoundError e){
            Log.e("Definicion de clase",e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR Conexion:",e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean resultado) {
        Log.d("RESULTADO", resultado.toString());
        if(resultado) {
            Log.d("WENAS:", "DE LOKISIMOS MANO");
            Log.d("WENAS query Result:", "jiji " + queryResult);
        }else {
            Log.d("WENAS:", "trosteza :(");
        }
    }
}
