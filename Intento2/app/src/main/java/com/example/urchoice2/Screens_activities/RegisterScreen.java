package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
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

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterScreen extends AppCompatActivity {

    ImageView redBack;
    MaterialButton signToStart, createAcc;
    TextView titleSign;

    private UserAPI userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a5___activity_register_screen);
        redBack = findViewById(R.id.sign_back_red);
        signToStart = findViewById(R.id.sign_to_start_button);
        createAcc = findViewById(R.id.create_acc_button);
        titleSign = findViewById(R.id.sign_title);
        Conectar();

        //Volver al start
        signToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignToStartButton();
            }
        });

        //Crear usuario
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registrer();
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

    //Metodo para ir al layout Start
    public void SignToStartButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(RegisterScreen.this, StartScreen.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(redBack, "start_red_trans");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterScreen.this, pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }

//Metodo para ir al layout main
    public void SignToMainButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(RegisterScreen.this, MainScreen.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(redBack, "bottom_trans");
                pairs[1] = new Pair<View, String>(titleSign, "back_pic_trans");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegisterScreen.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }


    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        /*evitar que se pete debido a ciertas imagenes sobre todo las de camara*/
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }



    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    //Metodo para registrar al user en la base de datos
    public void Registrer() {

        TextView emailTextView = findViewById(R.id.sign_email_insert);
        TextView nickTextView = findViewById(R.id.sign_user_insert);
        TextView contraTextView = findViewById(R.id.sign_pass_insert);
        TextView contra2TextView = findViewById(R.id.sign_pass2_insert);

        String emailString = emailTextView.getText().toString();
        String nickString = nickTextView.getText().toString();
        nickString = nickString.replace(" ", "");
        String contraString = contraTextView.getText().toString();
        String contra2String = contra2TextView.getText().toString();
        // Obtener el recurso Drawable
        Drawable drawable = getResources().getDrawable(R.drawable.logo);

        Bitmap bitmap = drawableToBitmap(drawable);
        String IMGString = bitmapToBase64(bitmap);


        if(nickString.length() > 15){
            Toast.makeText(RegisterScreen.this, "Name cannot contain more than 15 characters", Toast.LENGTH_SHORT).show();
        } else if(!emailString.isEmpty() || !emailString.isEmpty() || !nickString.isEmpty() || !contraString.isEmpty()){
            if(contraString.equals(contra2String)){

                Call<User> call = userApi.registerUser(emailString, nickString, IMGString, contraString);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("id_user", user.getId_user());
                            editor.apply();
                            Toast.makeText(RegisterScreen.this, "Successfully registered user", Toast.LENGTH_SHORT).show();
                            SignToMainButton();
                        } else {
                            Toast.makeText(RegisterScreen.this, "A user already exists with that email or name", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("SQL","ERROR" + t);
                    }
                });
            }else{
                Toast.makeText(this, "Passwords don´t match", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}