package com.example.urchoice2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.API.UserAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.MainScreen;
import com.example.urchoice2.Screens_activities.SingleGame;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRankingSubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AlertDialog alertDialog;
    private User user;
    private int userId;

    private UserAPI userApi;

    private String mParam1;
    private String mParam2;

    private ElementsAPI elementsAPI;
    private CategoriesAPI categoriesAPI;
    private Integer categoryId;
    private MaterialButton startGameButton;

    private MaterialButton go_back_button;
    public MainRankingSubFragment() {}

    public static MainRankingSubFragment newInstance(String param1, String param2) {
        MainRankingSubFragment fragment = new MainRankingSubFragment();
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
        View view = inflater.inflate(R.layout.f1___x_sub__fragment_main_ranking, container, false);
        waitAlertAltera();
        GetUser(view);
        GetCategory(view);
        GetRanking(view);

        startGameButton = view.findViewById(R.id.start_game_button); // Reemplaza start_game_button con el ID real de tu botón
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartGame();

            }

        });

        go_back_button = view.findViewById(R.id.back_home_button);
        go_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_home();
            }
        });
        return view;
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userApi = retrofit.create(UserAPI.class);
        elementsAPI = retrofit.create(ElementsAPI.class);
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        categoryId = preferences.getInt("id_categorySingle", 0);
        userId = preferences.getInt("id_user", 0);
    }

    public void GetCategory(View view){
        Call<Category> call = categoriesAPI.getCategory(categoryId);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Category category = response.body();
                    // Aquí puedes manejar la categoría recibida
                    Log.d("Categoría", "ID: " + category.getId_cat() + ", Nombre: " + category.getName_cat());
                    ImageView categoryIMG = view.findViewById(R.id.IMGCategory);
                    categoryIMG.setImageBitmap(base64ToBitmap(category.getImg_cat()));
                } else {
                    // Manejar el caso en que la respuesta no sea exitosa
                    Log.e("Error", "Error al obtener la categoría");
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                // Manejar el caso de error en la comunicación
                Log.e("Error", "Error en la llamada al servidor: " + t.getMessage());
            }
        });
    }

    public void GetRanking(View view){
        Call<List<Element>> call = elementsAPI.getRanking(categoryId);
        call.enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elements = response.body();
                    for (int i = 0; i < elements.size(); i++) {
                        TextView nameTextView = view.findViewById(getResources().getIdentifier("Number" + (i + 1), "id", requireContext().getPackageName()));
                        ImageView imgImageView = view.findViewById(getResources().getIdentifier("Number" + (i + 1) + "IMG", "id", requireContext().getPackageName()));
                        TextView scoreuser =  view.findViewById(getResources().getIdentifier("Number" + (i + 1) + "Score", "id", requireContext().getPackageName()));

                        nameTextView.setText(elements.get(i).getName_elem());
                        imgImageView.setImageBitmap(base64ToBitmap(elements.get(i).getImg_elem()));
                        scoreuser.setText(Integer.toString(elements.get(i).getVictories()));


                        dismissWaitAlert();
                    }
                    // Haz algo con los datos recibidos
                } else {
                    // Maneja el error de respuesta
                }
            }

            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {
                // Maneja el error de la llamada
            }
        });
    }


    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public void StartGame(){

        Intent intent = new Intent(requireContext(), SingleGame.class);
        requireContext().startActivity(intent);
    }
    /*public void waitAlert(){
        // Construir el nuevo AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.ff___all_fragments_loading_alert_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }*/


    public void waitAlertAltera() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.ff___all_fragments_loading_alert_dialog_altera, null);

        //cargar gif
        ImageView alteraImageView = view.findViewById(R.id.altera);
        Glide.with(this).asGif().load(R.drawable.altera_final).into(alteraImageView);

        // Create the AlertDialog
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
    public void back_home() {

        // Reemplaza el fragmento actual con MainFragment
        Fragment mainFragment = new MainFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.mainframe, mainFragment);
        transaction.addToBackStack(null); // Opcional: añadir a la pila de retroceso
        transaction.commit();

        //SI QUITAS ESTO QUEDA RARO Y FEO SERGIO AYUDAAAAA
        /*
        go_back_button.setVisibility(View.GONE);
        startGameButton.setVisibility(View.GONE);
        go_back_button.setEnabled(false);
        startGameButton.setEnabled(false);
        */
    }
    public void GetUser(View view){
        Call<User> call = userApi.getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    updateView(view);
                    Log.e("API Error", "NOMBRE" + user.getNick_user());

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
    private void updateView(View view) {
        if (user != null) {
            TextView username = view.findViewById(R.id.ranking_username);
            TextView email = view.findViewById(R.id.ranking_email);
            TextView games = view.findViewById(R.id.ranking_games_played);
            ImageView userIMG = view.findViewById(R.id.ranking_user_photo);

            username.setText(user.getNick_user());
            email.setText(user.getEmail_user());
            userIMG.setImageBitmap(base64ToBitmap(user.getImg_user()));
            games.setText(String.valueOf(user.getGamesPlayed()));
        }
    }

}