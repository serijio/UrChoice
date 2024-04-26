package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;

import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class StartScreen extends AppCompatActivity {
    MaterialTextView MtextView;
    MaterialButton tapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2_activity_start_screen);
        MtextView = findViewById(R.id.lobby_app_title);
        tapButton = findViewById(R.id.tapScreenButton);
        setDegradadoTitulo();
        tapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeButton();
            }
        });
    }
    private void setDegradadoTitulo(){
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
    public void ChangeButton(){


        Intent intent = new Intent(StartScreen.this, MainActivity.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartScreen.this);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
            finish();
        }
    }


}
