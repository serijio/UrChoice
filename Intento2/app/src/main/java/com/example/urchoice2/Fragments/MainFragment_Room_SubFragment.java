package com.example.urchoice2.Fragments;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.urchoice2.API.RoomAPI;
import com.example.urchoice2.Adapters.MainFragment_Room_Adapter;
import com.example.urchoice2.Classes.Rooms;
import com.example.urchoice2.R;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment_Room_SubFragment extends Fragment {
    private RecyclerView recyclerView;
    private RoomAPI roomAPI;
    private MainFragment_Room_Adapter roomAdapter;
    private AlertDialog alertDialog;
    private Handler handler;
    private Runnable runnable;


    public MainFragment_Room_SubFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Conectar();
        View rootView = inflater.inflate(R.layout.f1___sub__fragment_main_room, container, false);

        recyclerView = rootView.findViewById(R.id.rooms_recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        roomAPI = retrofit.create(RoomAPI.class);
        handler = new Handler();

        //Se llama a GetRooms cada 3 segundos
        runnable = new Runnable() {
            @Override
            public void run() {
                GetRooms();
                handler.postDelayed(this, 3000);
            }
        };
        waitAlertAltera();
    }

    //Coger todas las Rooms que hay en la base de datos
    public void GetRooms(){
        Call<List<Rooms>> call = roomAPI.getRooms();
        call.enqueue(new Callback<List<Rooms>>() {
            @Override
            public void onResponse(Call<List<Rooms>> call, Response<List<Rooms>> response) {
                if (response.isSuccessful()) {

                    List<Rooms> rooms = response.body();

                    roomAdapter = new MainFragment_Room_Adapter(getContext(),rooms);
                    recyclerView.setAdapter(roomAdapter);
                    dismissWaitAlert();
                } else {
                    dismissWaitAlert();
                }
            }

            @Override
            public void onFailure(Call<List<Rooms>> call, Throwable t) {
                // Manejar errores de la llamada
                dismissWaitAlert();
            }
        });
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

    //Wait de altera para que carguen los datos
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
}