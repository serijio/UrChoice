package com.example.urchoice2.Screens_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.R;
import com.example.urchoice2.SQL.CrudSQL;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginScreen extends AppCompatActivity {

    CrudSQL crud;

    private UserAPI userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a4___activity_login_screen);
        Conectar();
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserAPI.class);
    }

    public void LoginNow(View view) {
        crud = new CrudSQL();
        crud.conexion();

        TextInputLayout loginEmailLayout = findViewById(R.id.login_email);
        TextInputLayout loginPassLayout = findViewById(R.id.login_pass);

        TextInputEditText loginEmailEditText = (TextInputEditText) loginEmailLayout.getEditText();
        TextInputEditText loginPassEditText = (TextInputEditText) loginPassLayout.getEditText();

        String loginEmail = loginEmailEditText.getText().toString();
        String loginPass = loginPassEditText.getText().toString();

        String checkIfExistsQuery = "SELECT COUNT(*) FROM users WHERE email_user = '" + loginEmail + "' AND pass_user = '" + loginPass +  "'";

        crud.getCount(checkIfExistsQuery, count -> {
            if (count == 0) {
                Toast.makeText(this, "Login information isn't correct", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Welcome " + loginEmail, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainScreen.class);
                startActivity(intent);

            }
        });

    }

    public void ToRegister(View view) {
        Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
        startActivity(intent);
    }
    /*
    public void Login(Dialog dialog) {
        TextView emailTextView = dialog.findViewById(R.id.register_name);
        TextView contraTextView = dialog.findViewById(R.id.register_pass);

        String emailString = emailTextView.getText().toString();
        String contraString = contraTextView.getText().toString();

        Call<User> call = userApi.loginUser(emailString, contraString);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("id_user", user.getId_user());
                    editor.apply();
                    Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginScreen.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

}