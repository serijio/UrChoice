package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class TapToStartScreen extends AppCompatActivity {
    MaterialTextView MtextView;
    TextView slogan;
    ImageView taptostart_bluecard, taptostart_redcard;
    MaterialButton tapButton;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a2___activity_tap_to_start_screen);
        MtextView = findViewById(R.id.tap_app_title);
        slogan = findViewById(R.id.tap_slogan);
        linear = findViewById(R.id.tap_credits);
        tapButton = findViewById(R.id.tap_button);
        taptostart_redcard = findViewById(R.id.tap_redcard);
        taptostart_bluecard = findViewById(R.id.tap_bluecard);
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(TapToStartScreen.this, StartScreen.class);
                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<View, String>(taptostart_bluecard,"start_blue_trans");
                pairs[1] = new Pair<View, String>(taptostart_redcard,"start_red_card");
                pairs[2] = new Pair<View, String>(MtextView,"start_appname_trans");
                pairs[3] = new Pair<View, String>(slogan,"start_log_trans");
                pairs[4] = new Pair<View, String>(tapButton,"start_sign_trans");
                pairs[5] = new Pair<View, String>(linear,"start_credits_trans");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(TapToStartScreen.this,pairs);

                    SharedPreferences preferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                    int userId = preferences.getInt("id_user", 0);
                    startActivity(intent, options.toBundle());
                    if(userId == 0){
                        startActivity(intent, options.toBundle());
                    }else{
                        Intent intent2 = new Intent(TapToStartScreen.this, MainScreen.class);
                        startActivity(intent2, options.toBundle());

                    }

                    finish();

                } else {
                    startActivity(intent);
                    finish();
                }
            }
        },400);
    }
}
