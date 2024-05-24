package com.example.urchoice2.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.urchoice2.API.FriendsAPI;
import com.example.urchoice2.Adapters.Friends_Screen_Adapter;
import com.example.urchoice2.Classes.User;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Friends_Screen_Model;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context context;
    private AlertDialog alertDialog;
    private RecyclerView recyclerView;
    String nick_name;
    MaterialButton friends_requests;
    ImageButton friends_add;
    private FriendsAPI friendsAPI;
    private List<User> users;
    private int userId;
    private Friends_Screen_Adapter friends_requests_adapter;
    private ArrayList<Friends_Screen_Model> friendsScreenModels = new ArrayList<>();

    private Handler handler;
    private Runnable runnable;

    public FriendsFragment() {}

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Conectar();
        waitAlertAltera();
        View view = inflater.inflate(R.layout.f2___fragment_friends, container, false);
        friends_requests = view.findViewById(R.id.see_requests_button);
        friends_add = view.findViewById(R.id.send_request);

        friends_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                see_requests();
            }
        });

        friends_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText textInputEditText = view.findViewById(R.id.search_friends);
                nick_name = textInputEditText.getText().toString();
                Addfriend();
            }
        });

        recyclerView = view.findViewById(R.id.friends_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    public void see_requests() {
        Fragment friendsRequestsFragment = new FriendsRequestsSubFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.friends_layout, friendsRequestsFragment);
        transaction.addToBackStack(null); // Opcional: añadir a la pila de retroceso
        transaction.commit();
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        friendsAPI = retrofit.create(FriendsAPI.class);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                GetFriends();
                handler.postDelayed(this, 2000);
            }
        };
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

    public void Addfriend(){
        Log.e("SQL","DATOS: " + nick_name + userId);
        Call<Void> call = friendsAPI.addFriend(userId, nick_name);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // La solicitud se realizó correctamente (código de estado 200)
                    Toast.makeText(requireContext(), "Request sent", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    // El usuario no existe (código de estado 404)
                    Toast.makeText(requireContext(), "No user exist with that friend", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 400) {
                    // Ya existe una relación de amistad entre los dos usuarios (código de estado 400)
                    Toast.makeText(requireContext(), "You already have that friend", Toast.LENGTH_SHORT).show();
                } else {
                    // Otro error
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API_CALL", "Error en la llamada: " + t.getMessage());
                Toast.makeText(requireContext(), "Error en la llamada: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GetFriends(){
        Call<List<User>> call = friendsAPI.getFriends(userId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    users = new ArrayList<>(response.body());
                    setRvMain();
                    friends_requests_adapter = new Friends_Screen_Adapter(requireContext(),friendsScreenModels,users);
                    recyclerView.setAdapter(friends_requests_adapter);
                    dismissWaitAlert();
                } else {
                    Log.e("SQL", "Error en la respuesta: " + response.message());
                    // Manejar errores de la API
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("SQL", "Error en la llamada: " + t.getMessage());
                // Manejar errores de conexión
            }
        });
    }


    private void setRvMain() {
        friendsScreenModels = new ArrayList<>();
        Drawable mainDelIcon = ContextCompat.getDrawable(requireContext(), R.drawable.save_blue_border);
        for (int i = 0; i < users.size(); i++) {
            friendsScreenModels.add(new Friends_Screen_Model(
                    users.get(i).getNick_user(),
                    users.get(i).getEmail_user(),
                    base64ToBitmap(users.get(i).getImg_user()),
                    mainDelIcon
            ));
        }
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(runnable); // Inicia la ejecución
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Detiene la ejecución
    }
    public void waitAlertAltera() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View view = inflater.inflate(R.layout.ff___xx_alert__all_fragments_loading_altera, null);

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
}