package com.example.urchoice2;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ConexionClandestina extends AsyncTask<Void,Integer,Boolean> {

    String queryResult;

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            //String connectionString = "jdbc:mysql://monorail.proxy.rlwy.net:45523/railway;user=root;password=vDemhtCuWGRNzlYnkENndIbEtZsnnylA";
            String connectionString = "jdbc:mysql://monorail.proxy.rlwy.net:45523/railway";
            //String connectionString = "jdbc:jtds:mysql://monorail.proxy.rlwy.net:45523/railway";
            String user = "root";
            String password = "vDemhtCuWGRNzlYnkENndIbEtZsnnylA";
            /*Properties props = new Properties();
            props.setProperty("user", "root");
            props.setProperty("password", "vDemhtCuWGRNzlYnkENndIbEtZsnnylA");*/
            Connection con = DriverManager.getConnection(connectionString, user, password);
            if (con == null)
            {
                return false;
            }

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            StringBuilder resultStringBuilder = new StringBuilder();

            while (resultSet.next()) {
                // Assuming you have a column named 'column_name' in your table
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

            /*if (resultSet.first()) {
                do {
                    String id_user = resultSet.getString("id_user");
                    Log.d("USER","id: " + id_user);
                    String email_user = resultSet.getString("email_user");
                    String nick_user = resultSet.getString("nick_user");
                    String pass_user = resultSet.getString("pass_user");
                    String img_user = resultSet.getString("img_user");
                    resultStringBuilder.append(id_user).append("\n");
                    resultStringBuilder.append(email_user).append("\n");
                    resultStringBuilder.append(nick_user).append("\n");
                    resultStringBuilder.append(pass_user).append("\n");
                    resultStringBuilder.append(img_user).append("\n");
                } while (resultSet.next()); // Move to the next row if available
            }*/

            // Assign the result to the queryResult variable
            queryResult = resultStringBuilder.toString();
            //Log.d("WENAS query Result:", "jiji " + queryResult);

            Statement statement2 = con.createStatement();
            int rowsAffected = statement2.executeUpdate("INSERT INTO users VALUES(" +
                    "'2','alexgei','supergei','1234','');");

            if (rowsAffected > 0) {
                // Insertion was successful
                Log.d("ANIADIR", "Mu bien, has agregao a Alex gei");
            } else {
                // Insertion failed
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
