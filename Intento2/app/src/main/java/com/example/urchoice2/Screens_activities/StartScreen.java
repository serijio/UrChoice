package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class StartScreen extends AppCompatActivity {
    MaterialTextView MtextView;
    ImageView blueCard, redCard;

    MaterialButton login_tapButton, sign_tapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a3___activity_start_screen);
        MtextView = findViewById(R.id.start_app_name);
        blueCard = findViewById(R.id.start_blue_card);
        redCard = findViewById(R.id.start_red_card);
        login_tapButton = findViewById(R.id.start_login_button);
        sign_tapButton = findViewById(R.id.start_sign_button);
        setDegradadoTitulo();


        login_tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToLoginButton();
            }
        });

        sign_tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToSignButton();
            }
        });

    }
    private void setDegradadoTitulo() {
        TextPaint pintar = MtextView.getPaint();
        float width = pintar.measureText(("UrChoice"));
        //Establecemos el tamaño del degradado que se va a plicar en el MaterialTextView
        Shader textShader = new LinearGradient(0, 0, width, MtextView.getTextSize(),
                new int[]{
                        //El color inicial que esta aplicado en el layout de la splashScreen
                        //Siempre tiene que estar ubicado en el primer lugar
                        Color.parseColor("#ED3B3B"),
                        Color.parseColor("#38E1FC"),
                }, null, Shader.TileMode.CLAMP);
        MtextView.getPaint().setShader(textShader);
    }
    public void ToLoginButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, LoginScreen.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(blueCard, "login_blue_trans");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartScreen.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }

    public void ToSignButton() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, RegisterScreen.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(redCard, "sign_red_trans");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartScreen.this, pairs);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 400);
    }
}