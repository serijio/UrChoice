package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginScreen extends AppCompatActivity {
    ImageView blueBack, backPic;
    MaterialButton logToStart, logNow;
    private UserAPI userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4___activity_login_screen);
        blueBack = findViewById(R.id.login_back_blue);
        logToStart = findViewById(R.id.login_to_start_button);
        logNow = findViewById(R.id.login_to_main_button);
        backPic = findViewById(R.id.login_back_profile_pic);
        Conectar();

        logToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogToStartButton();
            }
        });

        logNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserAPI.class);
    }

    public void LogToStartButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginScreen.this, StartScreen.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(blueBack, "start_blue_trans");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginScreen.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }

    public void LogToMainButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(blueBack, "bottom_trans");
                pairs[1] = new Pair<View, String>(backPic, "back_pic_trans");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginScreen.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }

    public void Login() {
        TextView emailTextView = findViewById(R.id.login_email_insert);
        TextView contraTextView = findViewById(R.id.login_pass_insert);
        String emailString = emailTextView.getText().toString();
        String contraString = contraTextView.getText().toString();

        Call<User> call = userApi.loginUser(emailString, contraString);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    Toast.makeText(LoginScreen.this, "Welcome back " + user.getNick_user(), Toast.LENGTH_SHORT).show();

                    ImageView fotoperfil = findViewById(R.id.login_profile_pic);

                    fotoperfil.setImageBitmap(base64ToBitmap(user.getImg_user()));

                    SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id_user", user.getId_user());
                    editor.apply();

                    EndAPP(user.getId_user());
                    Log.e("SQL","Usuario encontrado");
                    /*Intent intent = new Intent(LoginScreen.this, MainScreen.class);
                    startActivity(intent);*/
                    LogToMainButton();
                } else {
                    Toast.makeText(LoginScreen.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("SQL","Error: " + t.getMessage());
            }
        });
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public void EndAPP(int userId) {
        Call<Void> call = userApi.endAPP(userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "OperaciÃ³n completada correctamente");
                } else {
                    // OcurriÃ³ un error al intentar finalizar la sala
                    Log.e("RoomEnd", "Error al finalizar la sala: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // OcurriÃ³ un error de red tap_blue_card otro error durante la llamada
                Log.e("RoomEnd", "Error de red: " + t.getMessage());
            }
        });
    }
}