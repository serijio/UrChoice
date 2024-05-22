package com.example.urchoice2.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.urchoice2.API.SavedAPI;
import com.example.urchoice2.Adapters.Saved_Saved_Screen_Adapter;
import com.example.urchoice2.Classes.Saved;
import com.example.urchoice2.R;
import com.example.urchoice2.RecyclerViews.Saved_Saved_Screen_Model;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class tab_SavedFragment extends Fragment {
    private List<Saved> saveds;
    private RecyclerView recyclerView;
    private ArrayList<Saved_Saved_Screen_Model> savedSavedScreenModels = new ArrayList<>();
    private Saved_Saved_Screen_Adapter saved_saved_screen_adapter;
    private SavedAPI savedAPI;
    private AlertDialog alertDialog;

    public tab_SavedFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Conectar();
        View rootView = inflater.inflate(R.layout.f4___x_sub__fragment_saved, container, false);

        recyclerView = rootView.findViewById(R.id.rvSaved);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        savedAPI = retrofit.create(SavedAPI.class);
        SharedPreferences preferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        int userId = preferences.getInt("id_user", 0);
        waitAlertAltera();
        GetSaved(userId);

    }


    //Metodo para recoger los saved del usuario
    private void GetSaved(int idUser) {
        Call<List<Saved>> call = savedAPI.obtenerGuardados(idUser);
        call.enqueue(new Callback<List<Saved>>() {
            @Override
            public void onResponse(Call<List<Saved>> call, Response<List<Saved>> response) {
                if (response.isSuccessful()) {
                    saveds = response.body();
                    setRvMain();

                    saved_saved_screen_adapter = new Saved_Saved_Screen_Adapter(requireContext(), savedSavedScreenModels, saveds);
                    recyclerView.setAdapter(saved_saved_screen_adapter);

                    dismissWaitAlert();
                } else {
                    // La solicitud no fue exitosa (código de respuesta no 200-299)
                    Log.e("Retrofit", "No hay");
                    dismissWaitAlert();
                }
            }
            @Override
            public void onFailure(Call<List<Saved>> call, Throwable t) {
                // Error en la conexión o al procesar la respuesta
                Log.e("Retrofit", "Error de conexión: " + t.getMessage(), t);
                dismissWaitAlert();
            }
        });
    }


    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

//Rellenar las cardview con las categorias en saved
    private void setRvMain() {
        Drawable mainFavIcon = ContextCompat.getDrawable(requireContext(), R.drawable.fav_red_border);
        Drawable mainSaveIcon = ContextCompat.getDrawable(requireContext(), R.drawable.save_blue_border);

        for (int i = 0; i < saveds.size(); i++) {
            savedSavedScreenModels.add(new Saved_Saved_Screen_Model(
                    saveds.get(i).getName_cat(),
                    base64ToBitmap(saveds.get(i).getImg_cat()),
                    mainFavIcon,
                    mainSaveIcon
            ));
        }
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
    //deshabilitar boton de retroceder
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                }
            });
        }
    }

}