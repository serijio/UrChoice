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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.urchoice2.R;
import com.google.android.material.textview.MaterialTextView;

public class SplashScreen extends AppCompatActivity {

    MaterialTextView MtextView;
    ImageView linea1, linea2, linea3, linea4, linea5, linea6, linea7, linea8, linea9, linea10, linea11, linea12, bluecard, redcard;
    //MaterialButton bottomlogo;
    Animation topTobottomAnimation, bottomTotopAnimation, bottomAnimation, left_middleAnimation, right_middleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //DEGRADADO DE TITULO
        setContentView(R.layout.a1___activity_splash_screen);
        MtextView = findViewById(R.id.splash_app_title);
        setDegradadoTitulo();

        //ANIMACIONES
        topTobottomAnimation = AnimationUtils.loadAnimation(this, R.anim.top_bottom_elements_animation);
        bottomTotopAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_top_elements_animation);
        left_middleAnimation = AnimationUtils.loadAnimation(this, R.anim.left_middle_elemets_animation);
        right_middleAnimation = AnimationUtils.loadAnimation(this, R.anim.right_middle_elemets_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_elemnts_animation);

        linea1 = findViewById(R.id.line1);
        linea2 = findViewById(R.id.line2);
        linea3 = findViewById(R.id.line3);
        linea4 = findViewById(R.id.line4);
        linea5 = findViewById(R.id.line5);
        linea6 = findViewById(R.id.line6);
        linea7 = findViewById(R.id.line7);
        linea8 = findViewById(R.id.line8);
        linea9 = findViewById(R.id.line9);
        linea10 = findViewById(R.id.line10);
        linea11 = findViewById(R.id.line11);
        linea12 = findViewById(R.id.line12);

        bluecard = findViewById(R.id.bluecard);
        redcard = findViewById(R.id.redcard);
        //bottomlogo = findViewById(R.id.tapScreenButton);

        linea1.setAnimation(topTobottomAnimation);
        linea2.setAnimation(topTobottomAnimation);
        linea3.setAnimation(topTobottomAnimation);
        linea4.setAnimation(topTobottomAnimation);
        linea5.setAnimation(topTobottomAnimation);
        linea6.setAnimation(topTobottomAnimation);
        linea7.setAnimation(bottomTotopAnimation);
        linea8.setAnimation(bottomTotopAnimation);
        linea9.setAnimation(bottomTotopAnimation);
        linea10.setAnimation(bottomTotopAnimation);
        linea11.setAnimation(bottomTotopAnimation);
        linea12.setAnimation(bottomTotopAnimation);


        bluecard.setAnimation(left_middleAnimation);
        redcard.setAnimation(right_middleAnimation);
        //bottomlogo.setAnimation(bottomAnimation);

        //SPLASH SCREEN TRANSITION

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, TapToStartScreen.class);
                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(bluecard,"taptostart_blue_card");
                pairs[1] = new Pair<View, String>(redcard,"taptostart_red_card");
                pairs[2] = new Pair<View, String>(MtextView,"taptostart_app_name");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
                    startActivity(intent, options.toBundle());
                    finish();

                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 2400);
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
}