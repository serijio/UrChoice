package com.example.urchoice2.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class CreateRoomFragment extends Fragment {
    private TextInputLayout textInputLayout;

    private MaterialButton categoryButton;
    private MaterialButton createRoomButton;

    public CreateRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f5___fragment_create_room_screen, container, false);

        // Declare and initialize the array of images and backgrounds
        final int[] images = {
                R.drawable.background_transparent,
                R.drawable.a,
                R.drawable.b,
                // Add more images as needed
        };

        final int[] backgrounds = {
                R.drawable.background_transparent,
                R.drawable.a,
                R.drawable.b,
                // Add more backgrounds as needed
        };

        // Configure the Spinner and the custom adapter
        String[] options = {"CHOOSE CATEGORY", "UMA USUME", "SIRGAY"};



        createRoomButton = view.findViewById(R.id.create_room_button);
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogOpen();
            }
        });

        categoryButton = view.findViewById(R.id.choose_categorybutton);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_alertDialogOpen();
            }
        });

        return view;


    }

    private void alertDialogOpen() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f5___fragment_x_alert_waiting_players, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_players);

        String[] usernames = {"TheRockex", "Spidey1912", "Lukinda551", "LordGrim551", "TuMama", "王八蛋"};

        recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.f5___fragment_x_player_status_cardview, parent, false);
                return new RecyclerView.ViewHolder(itemView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView playerName = holder.itemView.findViewById(R.id.player_name);
                ImageView readyicon = holder.itemView.findViewById(R.id.ready_status);
                MaterialButton exitstatus = holder.itemView.findViewById(R.id.exit_status);

                playerName.setText(usernames[position]);
                readyicon.setVisibility(View.VISIBLE);
                exitstatus.setVisibility(View.VISIBLE);
            }

            @Override
            public int getItemCount() {
                return usernames.length;
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

        // Obtener una referencia al botón de salir dentro del cuadro de diálogo
        MaterialButton exitButton = view.findViewById(R.id.alert_exit_button);

        // Agregar un OnClickListener al botón de salir
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se haga clic en el botón de salir
                alertDialog.dismiss(); // Cierra el cuadro de diálogo
            }
        });

        MaterialButton startButton = view.findViewById(R.id.alert_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se haga clic en el botón de inicio
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(requireContext(), MainActivity.class);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent);

                        } else {
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    }
                }, 400);
            }
        });

        // Agregar un OnClickListener al botón de salir
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar cuando se haga clic en el botón de salir
                alertDialog.dismiss(); // Cierra el cuadro de diálogo
            }
        });
    }

    private void category_alertDialogOpen() {
        // Inflar el diseño del AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f5___fragment_x_choose_category_alert, null);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        // Obtener la referencia del RecyclerView dentro del diseño del AlertDialog
        RecyclerView category_recyclerView = view.findViewById(R.id.recycler_category);

        // Datos para el RecyclerView
        String[] category_name = {"CHOOSE CATEGORY", "UMA USUME", "SIRGAY"};
        final int[] images = {
                R.drawable.background_transparent,
                R.drawable.a,
                R.drawable.b,
                // Añadir más imágenes si es necesario
        };

        // Configurar el RecyclerView
        categoryButton.findViewById(R.id.choose_categorybutton);
        AlertDialog alertDialog = builder.create();
        category_recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        category_recyclerView.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.f5___fragment_x_category_cardview_list, parent, false);
                return new RecyclerView.ViewHolder(itemView) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                // Obtener las vistas dentro de la CardView
                ImageView imageView = holder.itemView.findViewById(R.id.card_image);
                TextView textView = holder.itemView.findViewById(R.id.card_title);

                // Establecer los datos en las vistas
                imageView.setImageResource(images[position]);
                textView.setText(category_name[position]);

                // Añadir onClickListener a cada elemento de RecyclerView
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Cerrar el AlertDialog
                        alertDialog.dismiss();
                        // Cambiar el fondo y el texto del MaterialButton
                        categoryButton.setBackgroundResource(images[position]);
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



}