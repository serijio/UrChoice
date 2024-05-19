package com.example.urchoice2.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.FavsAPI;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.API.RoomGameAPI;
import com.example.urchoice2.API.SavedAPI;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.Favs;
import com.example.urchoice2.Classes.Saved;
import com.example.urchoice2.Classes.UserVote;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.MultiGame;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateRoomSubFragment extends Fragment {
    private MaterialButton categoryButton;
    private Handler handler;
    private MaterialButton createRoomButton;
    private CategoriesAPI categoriesAPI;

    private FavsAPI favsAPI;
    private SavedAPI savedAPI;
    private RoomAPI roomAPI;
    private Integer userId;
    private RoomGameAPI roomGameAPI;
    private List<Category> categoryList = new ArrayList<>();
    private List<Category> filteredCategoryList = new ArrayList<>();

    private List<Favs> favsList = new ArrayList<>();
    private List<Saved> savedList = new ArrayList<>();
    private Integer selectedPosition;
    private int roomId;
    private boolean shouldUpdate = true;
    private boolean allVotesReceived = false;
    private List<UserVote> userVotes = new ArrayList<>();
    private MaterialButton close_alert_category;


    public CreateRoomSubFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f3___x_sub__fragment_create_room_screen, container, false);
        handler = new Handler();
        Conectar();
        createRoomButton = view.findViewById(R.id.create_room_button);
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();

                TextInputEditText nickroom = view.findViewById(R.id.nick_room);
                TextInputEditText passroom = view.findViewById(R.id.passRoom);
                String nameRoom = nickroom.getText().toString();
                String password = passroom.getText().toString();
                if(nameRoom.isEmpty() || selectedPosition == null){
                    Toast.makeText(context, "Los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else{
                    createRoom(selectedPosition,userId,nameRoom,password);
                }
            }
        });

        categoryButton = view.findViewById(R.id.choose_categorybutton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCategories();
            }
        });
        return view;
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        roomAPI = retrofit.create(RoomAPI.class);
        roomGameAPI = retrofit.create(RoomGameAPI.class);
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);
    }


    private void category_alertDialogOpen() {
        // Inflar el diseÃ±o del AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3___xx_alert__createcatroom_fragment_choose_category, null);
        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        // Obtener la referencia del RecyclerView dentro del diseÃ±o del AlertDialog
        RecyclerView category_recyclerView = view.findViewById(R.id.recycler_category);

        // Datos para el RecyclerView
        String[] category_name = new String[categoryList.size()];

        Log.e("SQL","Tamaño: " + categoryList.size() + favsList.size() + savedList.size());
        for (int i = 0; i < categoryList.size(); i++) {
            category_name[i] = categoryList.get(i).getName_cat();
        }

        ArrayList<String> imageBase64List = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            String base64Image = categoryList.get(i).getImg_cat();
            imageBase64List.add(base64Image);
        }

        String[] imagesBase64 = imageBase64List.toArray(new String[0]);

        // Configurar el RecyclerView
        AlertDialog alertDialog = builder.create();
        CategoryAdapter adapter = new CategoryAdapter(categoryList, imagesBase64, category_name, alertDialog);
        category_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        category_recyclerView.setAdapter(adapter);

        TextInputEditText searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        close_alert_category = view.findViewById(R.id.close_room_choose_category);
        close_alert_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // Crear y mostrar el AlertDialo
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }


    public void GetCategories(){
        Call<List<Category>> call = categoriesAPI.getAllCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    category_alertDialogOpen();

                } else {
                    Log.e("SQL", "Error en la respuesta: " + response.message());
                    // Manejar errores de la API
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("SQL", "Error en la llamada: " + t.getMessage());
                // Manejar errores de conexión
            }
        });

    }


    public void createRoom(int categoryId, int userId,String nameRoom,String password) {
        Call<Integer> call = roomAPI.createRoom(categoryId, userId, nameRoom, password);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    roomId = response.body(); // Obtener el roomId de la respuesta
                    if (roomId != 0) {
                        Log.d("RoomCreation", "Nueva sala creada correctamente, ID: " + roomId);
                        // Haz algo con el roomId, como mostrarlo en un Toast
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("id_categoryMulti", categoryId);
                        editor.putInt("id_room", roomId);
                        editor.apply();
                        alertDialogOpen(roomId);
                    } else {
                        // La respuesta del servidor fue exitosa pero el roomId es cero
                        Log.e("RoomCreation", "El ID de la nueva sala es cero");
                    }
                } else {
                    // Ocurría un error al intentar crear la sala
                    Log.e("RoomCreation", "Error al crear la sala: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("RoomCreation", "Error de red: " + t.getMessage());
            }
        });
    }


    private void alertDialogOpen(int roomId) {
        shouldUpdate = true;
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3___xx_alert__createcatroom_fragment_waiting_players, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_players);

        recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_status_room, parent, false);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                TextView playerName = holder.itemView.findViewById(R.id.player_name);
                ImageView readyicon = holder.itemView.findViewById(R.id.ready_status);
                MaterialButton exitstatus = holder.itemView.findViewById(R.id.exit_status);

                playerName.setText(userVotes.get(position).getNick_user());
                exitstatus.setVisibility(View.VISIBLE);
                if(userVotes.get(position).getVote_game().equals("LISTO")){
                    readyicon.setVisibility(View.VISIBLE);
                }
                if(userVotes.get(position).getId_user() == userId){
                    exitstatus.setVisibility(View.INVISIBLE);
                }

                exitstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("id_categoryMulti");
                        editor.apply();
                        endRoom(roomId,userVotes.get(position).getId_user());
                    }
                });
            }
            @Override
            public int getItemCount() {
                return userVotes.size();
            }
        });

        UsersRoom(recyclerView.getAdapter());
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

        // Obtener una referencia al botÃ³n de salir dentro del cuadro de diÃ¡logo
        MaterialButton exitButton = view.findViewById(R.id.alert_exit_button);
        MaterialButton startButton = view.findViewById(R.id.alert_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setText("ESPERANDO");
                startButton.setTextSize(10);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Ready(roomId, userId);
                } else {
                    Ready(roomId, userId);
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldUpdate = false;
                endRoom(roomId,userId);
                alertDialog.dismiss();
            }
        });
    }


    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public void UsersRoom(RecyclerView.Adapter adapter) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!shouldUpdate) {
                    // Si la variable shouldUpdate es falsa, cancelar la tarea del temporizador
                    timer.cancel();
                    return;
                }
                Call<List<UserVote>> call = roomAPI.getUsersInRoom(roomId);
                call.enqueue(new Callback<List<UserVote>>() {
                    @Override
                    public void onResponse(Call<List<UserVote>> call, Response<List<UserVote>> response) {
                        if (response.isSuccessful()) {
                            Log.e("SQL", "HOLA");

                            // Crear una lista de nombres de usuario a partir de los datos obtenidos
                            userVotes = response.body();
                            List<UserVote> newNames = new ArrayList<>();
                            for (UserVote userVote : userVotes) {
                                newNames.add(userVote);
                                Log.e("SQL", "User: " + userVote.getNick_user());
                            }
                            updateAdapter(newNames, adapter);
                        } else {
                            // Error al obtener la respuesta
                            Log.e("SQL", "Error por lo que sea");
                        }
                    }
                    @Override
                    public void onFailure(Call<List<UserVote>> call, Throwable t) {
                        // Error de red o error al parsear la respuesta
                        Log.e("SQL", "Error" + t);
                    }
                });
            }
        }, 0, 5000); // Ejecutar la tarea cada 5 segundos
    }


    private void updateAdapter(List<UserVote> userVote, RecyclerView.Adapter adapter) {
        if (!shouldUpdate) {
            return;
        }
        userVotes.clear();
        userVotes.addAll(userVote);
        adapter.notifyDataSetChanged();
    }


    public void endRoom(int roomId, int userId) {
        Call<Void> call = roomAPI.endRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "OperaciÃ³n completada correctamente");
                } else {
                    // Ocurría un error al intentar finalizar la sala
                    Log.e("RoomEnd", "Error al finalizar la sala: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // OcurriÃ³ un error de red tap_blue_card otro error durante la llamada
                Log.e("RoomEnd", "Error de red: " + t.getMessage());
            }
        });
    }


    private void startRepeatedCall() {
        Log.e("SQL","ENTRO AL CALL: " + allVotesReceived );
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!allVotesReceived) {
                    GetUsers();
                    handler.postDelayed(this, 3000);
                } else {
                    // Todos los votos han sido recibidos, detener la llamada repetida
                    handler.removeCallbacksAndMessages(null);
                }
            }
        }, 3000);
    }


    public void Ready(int roomId, int userId) {
        Call<Void> call = roomGameAPI.updateVote(roomId, userId,"LISTO");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    startRepeatedCall();
                } else {
                    Log.e("SQL", "ERRORUPC: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL", "ERRORUP2C: " + t.getMessage());
            }
        });
    }


    public void GetUsers() {
        Call<List<UserVote>> call = roomAPI.getUsersInRoom(roomId);
        call.enqueue(new Callback<List<UserVote>>() {
            @Override
            public void onResponse(Call<List<UserVote>> call, Response<List<UserVote>> response) {
                if (response.isSuccessful()) {
                    Log.e("SQL","FUNCIONOGET");
                    List<UserVote> users = response.body();
                    boolean allVotesReceivedTemp = true;
                    for (UserVote user : users) {
                        if (user.getVote_game() == null || user.getVote_game().isEmpty()) {
                            allVotesReceivedTemp = false;
                            break;
                        }
                    }
                    allVotesReceived = allVotesReceivedTemp;
                    Log.e("SQL","BOOL" + allVotesReceived);
                    if (allVotesReceived) {
                        VoteClear(" ");
                        handler.removeCallbacksAndMessages(null);
                    }
                } else {
                    Log.e("SQL","ERRORGET");
                }
            }
            @Override
            public void onFailure(Call<List<UserVote>> call, Throwable t) {
                Log.e("SQL","ERRORGET2");
            }
        });
    }


    public void VoteClear(String voto) {
        Call<Void> call = roomGameAPI.updateVote(roomId, userId, voto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    shouldUpdate = false;
                    Intent intent = new Intent(requireContext(),MultiGame.class);
                    requireContext().startActivity(intent);
                } else {
                    Log.e("SQL", "ERRORUPC: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SQL", "ERRORUP2C: " + t.getMessage());
            }
        });
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


    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        private List<Category> categoryList;
        private List<Category> filteredCategoryList;
        private String[] imagesBase64;
        private String[] category_name;
        private AlertDialog alertDialog;

        public CategoryAdapter(List<Category> categoryList, String[] imagesBase64, String[] category_name, AlertDialog alertDialog) {
            this.categoryList = categoryList;
            this.filteredCategoryList = new ArrayList<>(categoryList);
            this.imagesBase64 = imagesBase64;
            this.category_name = category_name;
            this.alertDialog = alertDialog;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_create_category_image, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Category category = filteredCategoryList.get(position);
            holder.textView.setText(category.getName_cat());
            String base64Image = category.getImg_cat();
            byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imageView.setImageBitmap(bitmap);

            holder.itemView.setOnClickListener(v -> {
                // Update the category button with the selected category image and text
                categoryButton.setText(category.getName_cat());
                selectedPosition = category.getId_cat();
                // Convert the Bitmap to a Drawable and set it as the button's background
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    categoryButton.setBackground(bitmapDrawable);
                } else {
                    categoryButton.setBackgroundDrawable(bitmapDrawable);
                }
                alertDialog.dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return filteredCategoryList.size();
        }

        public void filter(String text) {
            filteredCategoryList.clear();
            if (text.isEmpty()) {
                filteredCategoryList.addAll(categoryList);
            } else {
                for (Category category : categoryList) {
                    if (category.getName_cat().toLowerCase().contains(text.toLowerCase())) {
                        filteredCategoryList.add(category);
                    }
                }
            }
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.card_title);
                imageView = itemView.findViewById(R.id.card_image);
            }
        }
    }
}