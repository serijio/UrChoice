package com.example.urchoice2.Fragments;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.MainActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class CreateRoomFragment extends Fragment {
    private TextInputLayout textInputLayout;
    private Spinner spinner;
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
        spinner = view.findViewById(R.id.Spinner01);
        CustomAdapter adapter = new CustomAdapter(requireContext(), options, images, backgrounds);
        spinner.setAdapter(adapter);

        createRoomButton = view.findViewById(R.id.create_room_button);
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogOpen();
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

    public class CustomAdapter extends ArrayAdapter<String> {
        private final Context mContext;
        private final String[] mOptions;
        private final int[] mImages;
        private final int[] mBackgrounds;

        public CustomAdapter(Context context, String[] options, int[] images, int[] backgrounds) {
            super(context, R.layout.f5___fragment_x_spinner_category_list, options);
            this.mContext = context;
            this.mOptions = options;
            this.mImages = images;
            this.mBackgrounds = backgrounds;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.f5___fragment_x_spinner_category_list, parent, false);

            TextView textViewLabel = view.findViewById(R.id.textViewLabel);
            ImageView imageViewIcon = view.findViewById(R.id.imageViewIcon);

            textViewLabel.setText(mOptions[position]);
            imageViewIcon.setImageResource(mImages[position]);

            return view;
        }

        @NonNull
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.f5___fragment_x_spinner_category_list, parent, false);

            ImageView imageViewIcon = view.findViewById(R.id.imageViewIcon);
            TextView textViewLabel = view.findViewById(R.id.textViewLabel);

            // Set image and background
            imageViewIcon.setImageResource(mImages[position]);
            view.setBackgroundResource(mBackgrounds[position]);

            // Establecer transparencia para el icono


            // Establecer transparencia para el texto
            textViewLabel.setText(mOptions[position]);

            return view;
            //hola
        }
    }
}
