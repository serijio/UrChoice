package com.example.urchoice2.Fragments;

import static android.app.Activity.RESULT_OK;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.Adapters.CreateCategory_CardAdapter;
import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCategorySubFragment extends Fragment {
    MaterialButton add_new_card_button;
    MaterialButton add_category_image_button;
    ImageView add_category_image;
    RoundedImageView edit_image_button;
    Bitmap selectedBitmap;
    Bitmap selectedBitmap2;
    TextView alert_card_textview;
    MaterialButton setCard_data_button;
    List<Element> cardsList;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    //private Uri selectedImageUri;
    MaterialButton create_category_button;
    MaterialButton go_to_newCP_sceen;
    private CategoriesAPI categoriesAPI;
    //private AlertDialog alertDialog2;
    private BitmapDrawable bitmapDrawable;
    private MaterialButton close_add_card_alert;

    private static final int PICK_IMAGE_REQUEST1 = 0;
    private static final int PICK_IMAGE_REQUEST2 = 1;
    private TextView empty_text;

    private int userId;

    public CreateCategorySubFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f3___x_sub__fragment_create_category_screen, container, false);
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Conectar();


        cardsList = new ArrayList<>();
        add_new_card_button = view.findViewById(R.id.add_new_card);

        add_new_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_card_alertDialog();
            }
        });
        add_category_image_button = view.findViewById(R.id.add_new_category_image_button);
        add_category_image = view.findViewById(R.id.add_new_category_image);
        add_category_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategoryImageFromGallery();
            }
        });
        cardsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_card_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new CreateCategory_CardAdapter(cardsList));

        create_category_button = view.findViewById(R.id.create_new_category_button);
        create_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText textInputEditText = view.findViewById(R.id.create_cat_name_insert);
                String categoryName = textInputEditText.getText().toString();

                if(categoryName.isEmpty() || cardsList.isEmpty() || bitmapDrawable.toString().isEmpty()){
                    Toast.makeText(getActivity(), "Los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else if(cardsList.size() < 4){
                    Toast.makeText(getActivity(), "Ponga mas de 4 Cartas", Toast.LENGTH_SHORT).show();
                }else if((cardsList.size() & (cardsList.size() - 1)) != 0){
                    Toast.makeText(getActivity(), "El número de cartas tiene que ser potencia de 2", Toast.LENGTH_SHORT).show();
                }else{
                    String IMGString = bitmapToBase64(selectedBitmap2);
                    InsertCategory(categoryName, IMGString, cardsList,userId);
                }
            }
        });

        empty_text = view.findViewById(R.id.empty_card_views_text);

        if(cardsList.isEmpty()){
            empty_text.setVisibility(View.VISIBLE);
            empty_text.setElevation(0);
        }else{
            empty_text.setVisibility(View.INVISIBLE);
            empty_text.setElevation(0);
        }
        return view;
    }
    public void create_new_category_alert_dialog(){
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3___xx_alert__createcatroom_fragment_new_category,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        final AlertDialog alertDialog2 = builder.create();
        alertDialog2.setCanceledOnTouchOutside(false);
        alertDialog2.setCancelable(false);
        alertDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog2.show();

        go_to_newCP_sceen = view.findViewById(R.id.gotohome);
        go_to_newCP_sceen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mandarCPFragment();
                alertDialog2.dismiss();
            }
        });
    }
    private void mandarCPFragment() {
        // Obtenemos el FragmentManager del Activity y comenzamos una transacción
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplazamos el FragmentoA con FragmentoB y lo agregamos a la pila de retroceso
        CreateCatRoomFragment fragmentoB = new CreateCatRoomFragment();
        fragmentTransaction.replace(R.id.uwu, fragmentoB);
        fragmentTransaction.addToBackStack(null);

        // Realizamos el cambio de fragmento
        fragmentTransaction.commit();
    }
    private void create_card_alertDialog(){
        // Inflar el diseño del AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3___xx_alert__createcatroom_fragment_add_category_card_data,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        alertDialog = builder.create();
        edit_image_button = view.findViewById(R.id.card_image_data);
        setCard_data_button = view.findViewById(R.id.set_data_alert_addcard_button);
        alert_card_textview = view.findViewById(R.id.card_textEdittextlayout);
        close_add_card_alert = view.findViewById(R.id.close_card_button);
        close_add_card_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        edit_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });

        // Configurar el botón para establecer la imagen y el nombre de la carta
        setCard_data_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String cardName = alert_card_textview.getText().toString();
                Bitmap cardImage = ((RoundedDrawable) edit_image_button.getDrawable()).getSourceBitmap();

                // Establecer la imagen en el RoundedImageView
                ((RoundedImageView) edit_image_button).setImageBitmap(cardImage);

                String base64Image = bitmapToBase64(cardImage);

                //edit_image_button.setImageBitmap(cardImage);

                alert_card_textview.setText(cardName);

                if(cardName.isEmpty() || cardName == null || base64Image.isEmpty() || base64Image == null){
                    Toast.makeText(getContext(), "Los datos son obligatorios", Toast.LENGTH_SHORT).show();
                }else{
                    // Establecer la imagen y el nombre de la carta donde lo desees
                    Element newCard = new Element(0, base64Image, cardName, 0,0);

                    cardsList.add(newCard);

                    // Notificar al adaptador del RecyclerView que se ha agregado una nueva carta
                    recyclerView.getAdapter().notifyItemInserted(cardsList.size() - 1);

                    empty_text.setVisibility(View.INVISIBLE);
                    empty_text.setElevation(1);

                    // Cerrar el AlertDialog
                    alertDialog.dismiss();
                }
            }
        });

        // Crear el AlertDialog

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }


    public void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
    }


    public void selectCategoryImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST2);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtiene la URI de la imagen seleccionada
            Uri selectedImageUri = data.getData();

            try {
                // Convierte la URI en un Bitmap
                selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                // Establece el Bitmap en el ImageButton
                edit_image_button.setImageBitmap(selectedBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == PICK_IMAGE_REQUEST2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Obtiene la URI de la imagen seleccionada
            Uri selectedImageUri = data.getData();
            try {
                // Convierte la URI en un Bitmap
                selectedBitmap2 = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                // Convierte el Bitmap en un Drawable
                //Drawable drawable = new BitmapDrawable(getResources(), selectedBitmap2);
                // Establece el Drawable como fondo del MaterialButton
                //add_category_image_button.setBackground(drawable);
                // Convertir Bitmap a Drawable
                bitmapDrawable = new BitmapDrawable(getResources(), selectedBitmap2);
                // Establecer el Drawable como fondo del botón
                //bitmapDrawable.setGravity(Gravity.CENTER);

                //add_category_image_button.setBackground(bitmapDrawable);
                //add_category_image.setBackground(bitmapDrawable);
                add_category_image.setImageBitmap(selectedBitmap2);

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
    /*
        public class CreateCategory_CardAdapter extends RecyclerView.Adapter<CreateCategory_CardAdapter.CardViewHolder> {

            private List<Element> cardsList;

            public CreateCategory_CardAdapter(List<Element> cardsList) {
                this.cardsList = cardsList;
            }

            @NonNull
            @Override
            public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.f3__x__fragment_category_card_data_cardview, parent, false);
                return new CardViewHolder(itemView);
            }

            @Override
            public int getItemCount() {
                return cardsList.size();
            }

            @Override
            public void onBindViewHolder(@NonNull CardViewHolder holder, @SuppressLint("RecyclerView") int position) {
                Element element = cardsList.get(position);

                Bitmap bitmap = base64ToBitmap(element.getImg_element());

                holder.bind(bitmap, element.getName_elem());

                holder.deleteCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Eliminar la carta del cardsList
                        cardsList.remove(position);
                        // Notificar al adaptador del RecyclerView que se ha eliminado un elemento
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cardsList.size());
                    }
                });
            }


            public class CardViewHolder extends RecyclerView.ViewHolder {
                ImageView cardImage;
                TextView cardName;
                MaterialButton deleteCard;

                public CardViewHolder(@NonNull View itemView) {
                    super(itemView);
                    cardImage = itemView.findViewById(R.id.category_card_data);
                    cardName = itemView.findViewById(R.id.category_carddata_name);
                    deleteCard = itemView.findViewById(R.id.deleteCard); // Agregar la referencia del botón deleteCard
                }

                public void bind(Bitmap bitmap, String cardNameText) {
                    cardImage.setImageBitmap(bitmap);
                    cardName.setText(cardNameText);
                }
            }
        }
    */
    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*evitar que se pete debido a ciertas imagenes sobre todo las de camara*/
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
    private void InsertCategory(String categoryName,String categoryImage, List<Element> elements, int userId){
        for(int i = 0; i < elements.size(); i++){
            Log.e("CategoryActivity", "Nombre: " + elements.get(i).getName_elem());
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("name_cat", categoryName);
            jsonRequest.put("img_cat", categoryImage);
            jsonRequest.put("id_user", userId);

            JSONArray elementsArray = new JSONArray();
            for (Element element : elements) {
                JSONObject elementObject = new JSONObject();
                elementObject.put("name_elem", element.getName_elem());
                elementObject.put("img_elem", element.getImg_elem());
                elementObject.put("victories", element.getVictories());
                elementObject.put("id_cat", element.getId_cat());
                elementsArray.put(elementObject);
            }
            jsonRequest.put("elements", elementsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        Call<Void> call = categoriesAPI.createCategory(requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {


                    // La solicitud fue exitosa
                    Log.d("CategoryActivity", "Categoría creada exitosamente");
                    create_new_category_alert_dialog();
                } else {
                    if (response.code() == 400) {
                        Toast.makeText(requireContext(), "Ya existe una categoria con ese nombre", Toast.LENGTH_SHORT).show();
                        Log.e("CategoryActivity", "Error: Ya existe una categoría con ese nombre");
                    } else {
                        // Otro código de estado, maneja según sea necesario
                        Log.e("CategoryActivity", "Error al crear la categoría: " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error de red o error en la respuesta
                Log.e("CategoryActivity", "Error en la llamada: " + t.getMessage());
            }
        });
    }


    public void Conectar(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
    }
}