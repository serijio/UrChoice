package com.example.urchoice2.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.API.RoomGameAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.SplashScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedDrawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {
    MaterialButton edit_name_button;
    MaterialButton edit_profile_image_button;
    private UserAPI userAPI;
    ImageView profile_image;
    private View view;
    ImageView profileBackground;
    TextView profile_userName;
    Bitmap selectedBitmap;
    Bitmap selectedBitmap2;
    private MaterialButton logout;
    private int userId;
    private static final int PICK_IMAGE_REQUEST1 = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private User user;

    public ProfileFragment() {}

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Conectar();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.f5___fragment_profile, container, false);
        edit_profile_image_button  = view.findViewById(R.id.edit_profile_image);
        profile_image = view.findViewById(R.id.profile_image);
        profileBackground = view.findViewById(R.id.profile_background);
        edit_profile_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();


            }
        });
        logout = view.findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });



        return view;
    }

    public void CerrarSesion(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("id_user");
        editor.apply();
        Intent intent = new Intent(requireContext(), SplashScreen.class);
        startActivity(intent);
        requireActivity().finish();
    }

    public void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);

    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*evitar que se pete debido a ciertas imagenes sobre todo las de camara*/
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtiene la URI de la imagen seleccionada
            Uri selectedImageUri = data.getData();

            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                profile_image.setImageBitmap(selectedBitmap);
                profileBackground.setImageBitmap(selectedBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //deshabilitar boton de retroceder
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    // No hacer nada cuando se presiona el botón de retroceso
                }
            });
        }
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userAPI = retrofit.create(UserAPI.class);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
        GetUser();
    }


    public void GetUser(){
        Call<User> call = userAPI.getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    TextInputEditText nombreTextView = view.findViewById(R.id.profileName_edittext);
                    nombreTextView.setText(user.getNick_user());
                    ImageView userIMG = view.findViewById(R.id.profile_image);
                    if(user.getImg_user() != null || user.getImg_user().isEmpty()){
                        userIMG.setImageBitmap(base64ToBitmap(user.getImg_user()));
                    }
                } else {
                    Log.e("API Error", "Error al obtener el usuario: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API Error", "Error al realizar la llamada: " + t.getMessage());
            }
        });
    }


    public void UpdateName(){
        TextInputEditText nombreTextView = view.findViewById(R.id.profileName_edittext);
        String nameString = nombreTextView.getText().toString();
        Call<Void> call = userAPI.updateUserName(userId, nameString);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Bienvenido " + nameString, Toast.LENGTH_SHORT).show();
                } else {
                    // La llamada no fue exitosa, maneja el error aquí
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error de red o error en la llamada, maneja el error aquí
            }
        });
    }

    public void UpdateIMG(){
        ImageView imgView = view.findViewById(R.id.profile_image);
        Drawable drawable = imgView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        String IMGString = bitmapToBase64(bitmap);
        Call<Void> call = userAPI.updateUserName(userId, IMGString);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                } else {
                    // La llamada no fue exitosa, maneja el error aquí
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error de red o error en la llamada, maneja el error aquí
            }
        });
    }

    public void LogOut(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("id_user");
        editor.apply();
    }

}