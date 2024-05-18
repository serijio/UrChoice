package com.example.urchoice2.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.SplashScreen;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

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
    private FriendsAPI friendAPI;
    ImageView profile_image;
    private View view;
    ImageView profileBackground;
    TextView profile_userName;
    Bitmap selectedBitmap;
    Bitmap selectedBitmap2;
    private MaterialButton logout;
    private int userId;
    private TextView profileMail;
    private EditText profileNameEditText;
    private MaterialButton editNewNameButton;
    private MaterialButton setNewNameButton;
    private AlertDialog alertDialog;
    private static final int PICK_IMAGE_REQUEST1 = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private MaterialButton my_categories_button;

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
    public void my_categories_fragment() {
        Fragment myCategoriesSubFragment = new ProfileMyCategoriesSubFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.my_profile, myCategoriesSubFragment);
        transaction.addToBackStack(null); // Opcional: añadir a la pila de retroceso
        transaction.commit();
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
        view =  inflater.inflate(R.layout.wwwwww_prueba_de_nuevo_xd, container, false);

        edit_profile_image_button  = view.findViewById(R.id.edit_profile_image);
        profile_image = view.findViewById(R.id.profile_image);
        profileMail = view.findViewById(R.id.profilemail);
        profileBackground = view.findViewById(R.id.profile_background);
        my_categories_button= view.findViewById(R.id.profile_categories_button);
        my_categories_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_categories_fragment();
            }
        });

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
        profileNameEditText = view.findViewById(R.id.profileName_edittext);
        editNewNameButton = view.findViewById(R.id.editnew_namebutton);
        setNewNameButton = view.findViewById(R.id.setnew_namebutton);

        profileNameEditText.setEnabled(false);

        // Establece un OnClickListener para el botón de edición
        editNewNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Habilita el EditText para que se pueda editar
                editNewNameButton.setVisibility(View.INVISIBLE);
                editNewNameButton.setElevation(0);
                setNewNameButton.setVisibility(View.VISIBLE);
                setNewNameButton.setElevation(1);
                profileNameEditText.setEnabled(true);
                profileNameEditText.setText("");
            }
        });

        setNewNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = profileNameEditText.getText().toString();
                UpdateName(newName);
                profileMail.setText("/@"+newName);
                profileNameEditText.setText(newName);

                setNewNameButton.setVisibility(View.INVISIBLE);
                setNewNameButton.setElevation(0);
                editNewNameButton.setVisibility(View.VISIBLE);
                editNewNameButton.setElevation(1);
                profileNameEditText.setEnabled(false);
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
                UpdateIMG(selectedBitmap);

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
                    // No hacer nada cuando se presiona el botÃ³n de retroceso
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
        friendAPI = retrofit.create(FriendsAPI.class);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
        waitAlert();
        GetFriends();

    }


    public void GetFriends(){
        Log.e("SQL","DATOS: " + userId);
        Call<JsonObject> call = friendAPI.getFriendCount(userId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonResponse = response.body();
                    int friendCount = jsonResponse.get("count").getAsInt();
                    TextView friendTextView = view.findViewById(R.id.friends);
                    friendTextView.setText(String.valueOf(friendCount));
                    GetUser();
                    dismissWaitAlert();
                }else{
                    Log.e("SQL","ERROR");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("SQL","ERROR2" + t);
            }
        });
    }


    public void GetUser(){
        Call<User> call = userAPI.getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    TextInputEditText nombreTextView = view.findViewById(R.id.profileName_edittext);
                    TextView emailTextView = view.findViewById(R.id.profilemail);
                    nombreTextView.setText(user.getNick_user());
                    emailTextView.setText("@" + user.getNick_user());

                    ImageView userIMG = view.findViewById(R.id.profile_image);
                    ImageView userBackgorundIMG = view.findViewById(R.id.profile_background);

                    userIMG.setImageBitmap(base64ToBitmap(user.getImg_user()));
                    userBackgorundIMG.setImageBitmap(base64ToBitmap(user.getImg_user()));


                    TextView profileGames = view.findViewById(R.id.games);
                    profileGames.setText(String.valueOf(user.getGamesPlayed()));
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


    public void UpdateName(String newName){
        Call<Void> call = userAPI.updateUserName(userId, newName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Bienvenido " + newName, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("SQL","ERROR");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL","ERROR2" + t);
            }
        });
    }


    public void UpdateIMG(Bitmap selectedBitmap){
        String IMGString = bitmapToBase64(selectedBitmap);
        Call<Void> call = userAPI.updateUserIMG(userId, IMGString);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Imagen Actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("SQL","ERROR");
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL","ERROR2" + t);
            }
        });
    }


    public void LogOut(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("id_user");
        editor.apply();
    }
    public void waitAlert(){
        // Construir el nuevo AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.ff___all_fragments_loading_alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

    }
    public void dismissWaitAlert() {
        alertDialog.dismiss();
    }

}