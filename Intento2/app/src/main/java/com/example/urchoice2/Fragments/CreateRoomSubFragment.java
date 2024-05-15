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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.Adapters.MainFragment_Room_Adapter;
import com.example.urchoice2.Classes.Category;
import com.example.urchoice2.Classes.UserVote;
import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.MultiGame;
import com.example.urchoice2.Screens_activities.SingleGame;
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
    private MaterialButton createRoomButton;
    private CategoriesAPI categoriesAPI;
    private RoomAPI roomAPI;
    private Integer userId;
    private List<Category> categoryList;
    private Integer selectedPosition;
    private int roomId;
    private boolean shouldUpdate = true;

    private List<UserVote> userVotes = new ArrayList<>();
    private MaterialButton enter_room;





    public CreateRoomSubFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f3__sub__fragment_create_room_screen, container, false);
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
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = preferences.getInt("id_user", 0);
    }

    public void GetCategories(){
        categoriesAPI.getCategories(userId).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    category_alertDialogOpen();

                } else {
                    Log.e("SQL","ERROR AL SACAR CATEGORIA");
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
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
                    // OcurriÃ³ un error al intentar crear la sala
                    Log.e("RoomCreation", "Error al crear la sala: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("RoomCreation", "Error de red: " + t.getMessage());
            }
        });
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

    private void alertDialogOpen(int roomId) {
        shouldUpdate = true;
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3__x__fragment_alert_waiting_players, null);
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
                // AcciÃ³n a realizar cuando se haga clic en el botÃ³n de inicio
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(requireContext(), MultiGame.class);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            shouldUpdate = false;
                            //startActivity(intent);
                            startButton.setText("ESPERANDO");
                            startButton.setTextSize(10);
                            Listo(roomId, userId, new MainFragment_Room_Adapter.RoomClosedListener() {
                                @Override
                                public void onRoomClosed() {
                                    shouldUpdate = false;
                                    // Muestra un Toast informando al usuario que la sala se ha cerrado correctamente
                                    startActivity(intent);
                                }
                            });

                        } else {
                            Listo(roomId, userId, new MainFragment_Room_Adapter.RoomClosedListener() {
                                @Override
                                public void onRoomClosed() {
                                    // Muestra un Toast informando al usuario que la sala se ha cerrado correctamente
                                    startActivity(intent);
                                    requireActivity().finish();
                                }
                            });
                        }
                    }
                }, 400);
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

    private void category_alertDialogOpen() {
        // Inflar el diseÃ±o del AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3__x__fragment_choose_category_alert, null);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        // Obtener la referencia del RecyclerView dentro del diseÃ±o del AlertDialog
        RecyclerView category_recyclerView = view.findViewById(R.id.recycler_category);

        // Datos para el RecyclerView
        String[] category_name = new String[categoryList.size()];
        Log.e("SQL","TamaÃ±o: " + categoryList.size());
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
        categoryButton.findViewById(R.id.choose_categorybutton);
        AlertDialog alertDialog = builder.create();
        category_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        category_recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_create_category_image, parent, false);
                return new RecyclerView.ViewHolder(itemView) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                // Obtener las vistas dentro de la CardView
                ImageView imageView = holder.itemView.findViewById(R.id.card_image);
                TextView textView = holder.itemView.findViewById(R.id.card_title);

                Category category = categoryList.get(position);

                // Decodificar la imagen Base64 y establecerla en el ImageView

                Bitmap bitmap = base64ToBitmap(imagesBase64[position]);
                imageView.setImageBitmap(bitmap);

                textView.setText(category_name[position]);

                // Añadir onClickListener a cada elemento de RecyclerView
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Cerrar el AlertDialog
                        alertDialog.dismiss();
                        selectedPosition = category.getId_cat();

                        // Convertir el Bitmap en un Drawable
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                        // Establecer el Drawable como fondo del botón
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            categoryButton.setBackground(bitmapDrawable);
                        } else {
                            categoryButton.setBackgroundDrawable(bitmapDrawable);
                        }

                        // Establecer el texto del botón
                        categoryButton.setText(category_name[position]);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return category_name.length;
            }
        });

        // Crear y mostrar el AlertDialog

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
    }

    public void endRoom(int roomId, int userId) {
        Call<Void> call = roomAPI.endRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "OperaciÃ³n completada correctamente");
                } else {
                    // OcurriÃ³ un error al intentar finalizar la sala
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

    public void Listo(int roomId, int userId, final MainFragment_Room_Adapter.RoomClosedListener listener){
        Call<Void> call = roomAPI.startRoom(roomId, userId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("RoomEnd", "OperaciÃ³n completada correctamente");
                    listener.onRoomClosed();
                } else {
                    // OcurriÃ³ un error al intentar finalizar la sala
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

}