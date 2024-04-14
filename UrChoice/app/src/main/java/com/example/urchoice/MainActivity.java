package com.example.urchoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;

public class MainActivity extends AppCompatActivity {

    private static String gIP = "monorail.proxy.rlwy.net";// this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc
    private static String gPORT = "45523";// the port sql server runs on
    private static String gDATABASE = "railway";// the data base name
    private static String gUSR = "root";// the user name
    private static String gPSW = "vDemhtCuWGRNzlYnkENndIbEtZsnnylA";// the password

    private static String gURL = "jdbc:mysql://mysql.railway.internal:3306/railway";

    /*String gURL = "jdbc:mysql://root:vDemhtCuWGRNzlYnkENndIbEtZsnnylA@mysql.railway.internal:3306/railway";
    String gIP = "monorail.proxy.rlwy.net";
    String gPORT = "45523";
    String gDATABASE = "railway";
    String gUSR = "root";
    String gPSW = "vDemhtCuWGRNzlYnkENndIbEtZsnnylA";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();

    }
    public void start() {
        final String class_jdbc = "com.mysql.cj.jdbc.Driver";
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("WENAS", "HAS LLEGAO HASTA AQUÍ MASTER");
        Connection conn = null;

        try {
            Class.forName(class_jdbc);
            conn = DriverManager.getConnection(gURL);
            Log.d("SQL","FUNCIONA");
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show();
            Log.d("SQL","CLASS");
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Connected no", Toast.LENGTH_SHORT).show();
            Log.d("SQL","ERROR");
        }
    }

    /*private static String ip = "monorail.proxy.rlwy.net";// this is the host ip that your data base exists on you can use 10.0.2.2 for local host                                                    found on your pc. use if config for windows to find the ip if the database exists on                                                    your pc
    private static String port = "45523";// the port sql server runs on

    //private static String Classes = "net.sourceforge.jtds.jdbc.Driver";// the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql
    private static String Classes = "com.mysql.cj.jdbc.Driver";// the driver that is required for this connection use                                                                           "org.postgresql.Driver" for connecting to postgresql
    private static String database = "railway";// the data base name
    private static String username = "root";// the user name
    private static String password = "vDemhtCuWGRNzlYnkENndIbEtZsnnylA";// the password

    private static String url = "jdbc:mysql://root:vDemhtCuWGRNzlYnkENndIbEtZsnnylA@mysql.railway.internal:3306/railway";

    private Connection connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start();

    }
    public void start() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Log.d("WENAS", "HAS LLEGAO HASTA AQUÍ MASTER");
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
            Log.d("SQL","FUNCIONA");
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show();
            Log.d("SQL","CLASS");
        } catch (SQLException | java.sql.SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Connected no", Toast.LENGTH_SHORT).show();
            Log.d("SQL","ERROR");
        }
    }*/
}