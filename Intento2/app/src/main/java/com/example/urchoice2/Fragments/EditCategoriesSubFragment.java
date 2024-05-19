package com.example.urchoice2.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urchoice2.API.CategoriesAPI;
import com.example.urchoice2.API.ElementsAPI;
import com.example.urchoice2.Adapters.CreateCategory_CardAdapter;
import com.example.urchoice2.Classes.Category;
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

public class EditCategoriesSubFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ID_CAT = "id_cat";
    private String mParam1;
    private String mParam2;
    Bitmap selectedBitmap;
    private TextView empty_text;
    Bitmap selectedBitmap2;
    private static final int PICK_IMAGE_REQUEST1 = 0;
    private static final int PICK_IMAGE_REQUEST2 = 1;
    private int idCat;
    private CategoriesAPI categoriesAPI;
    private ElementsAPI elementApi;
    private int userId;
    private Category category;
    List<Element> cardsList;
    RecyclerView recyclerView;
    TextView alert_card_textview;
    private MaterialButton add_category_image_button;
    private MaterialButton close_add_card_alert;
    MaterialButton add_new_card_button;
    MaterialButton create_category_button;
    MaterialButton cancel_edit_button;
    MaterialButton go_back_profile;
    MaterialButton keep_editing;
    private BitmapDrawable bitmapDrawable;
    private AlertDialog alertDialog;
    private androidx.appcompat.app.AlertDialog waitalertDialog;
    RoundedImageView edit_image_button;
    MaterialButton setCard_data_button;
    MaterialButton cancel_edit;


    public EditCategoriesSubFragment() {}

    public static EditCategoriesSubFragment newInstance(int idCat) {
        EditCategoriesSubFragment fragment = new EditCategoriesSubFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_CAT, idCat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCat = getArguments().getInt(ARG_ID_CAT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f5___x_sub__fragment_profile_my_categories_edit, container, false);
        cardsList = new ArrayList<>();
        Conectar(view);
        waitAlert();

        cardsList = new ArrayList<>();
        add_new_card_button = view.findViewById(R.id.edit_new_card);

        add_new_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_card_alertDialog();
            }
        });
        add_category_image_button = view.findViewById(R.id.edit_new_category_image_button);
        add_category_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategoryImageFromGallery();
            }
        });
        cardsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.edit_recycler_card_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new CreateCategory_CardAdapter(cardsList));

        create_category_button = view.findViewById(R.id.edit_new_category_button);
        create_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText textInputEditText = view.findViewById(R.id.edit_cat_name_insert);
                String categoryName = textInputEditText.getText().toString();

                if(categoryName.isEmpty() || cardsList.isEmpty() || add_category_image_button.getBackground().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Los campos son obligatorios", Toast.LENGTH_SHORT).show();
                }else if(cardsList.size() < 4){
                    Toast.makeText(getActivity(), "Ponga mas de 4 Cartas", Toast.LENGTH_SHORT).show();
                }else if((cardsList.size() & (cardsList.size() - 1)) != 0){
                    Toast.makeText(getActivity(), "El número de cartas tiene que ser potencia de 2", Toast.LENGTH_SHORT).show();
                }else{
                    // Paso 1: Obtener el Drawable del botón
                    Drawable drawable = add_category_image_button.getBackground();

                    Bitmap bitmap;

                    if (drawable instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) drawable).getBitmap();
                    } else {
                        bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                    }


                    String IMGString = bitmapToBase64(bitmap);
                    UpdateCategory(categoryName, IMGString,cardsList);


                }
            }
        });

        cancel_edit_button = view.findViewById(R.id.cancel_edit_button);
        cancel_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_edit_alert_dialog();
            }
        });

        empty_text = view.findViewById(R.id.empty_card_views_text);

        if(!cardsList.isEmpty()){
            empty_text.setVisibility(View.VISIBLE);
            empty_text.setElevation(0);
        }else{
            empty_text.setVisibility(View.INVISIBLE);

            empty_text.setElevation(0);
        }
        return view;
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
                add_category_image_button.setBackground(bitmapDrawable);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Conectar(View view){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://railwayserver-production-7692.up.railway.app")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        categoriesAPI = retrofit.create(CategoriesAPI.class);
        elementApi = retrofit.create(ElementsAPI.class);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UrChoice", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("id_user", 0);
        GetCategory(view);
    }

    public void GetCategory(View view){
        Call<Category> call = categoriesAPI.getCategory(idCat);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    category = response.body();
                    // Aquí puedes manejar la categoría recibida
                    Log.d("Categoría", "ID: " + category.getId_cat() + ", Nombre: " + category.getName_cat());
                    GetElemetsCategory(view);
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
    public void GetElemetsCategory(View view){
        elementApi.getElementsByCategory(idCat).enqueue(new Callback<List<Element>>() {
            @Override
            public void onResponse(Call<List<Element>> call, Response<List<Element>> response) {
                if (response.isSuccessful()) {
                    List<Element> elementList = response.body();
                    Log.e("SQL","DATOS: " + elementList.get(0).getName_elem());
                    TextInputEditText textInputEditText = view.findViewById(R.id.edit_cat_name_insert);
                    textInputEditText.setText(category.getName_cat());
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), base64ToBitmap(category.getImg_cat()));
                    add_category_image_button.setBackground(bitmapDrawable);
                    cardsList.addAll(elementList);

                    // Notificar al adaptador del RecyclerView que se ha agregado una nueva carta
                    recyclerView.getAdapter().notifyItemInserted(cardsList.size() - 1);
                    dismissWaitAlert();
                }
            }
            @Override
            public void onFailure(Call<List<Element>> call, Throwable t) {
                Log.e("SQL","ERROR");

            }
        });
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*evitar que se pete debido a ciertas imagenes sobre todo las de camara*/
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    public void cancel_edit_alert_dialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f5___xx_alert__cancel_edit_category, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        final AlertDialog alertDialog3 = builder.create();
        alertDialog3.setCanceledOnTouchOutside(false);
        alertDialog3.setCancelable(false);
        alertDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog3.show();

        go_back_profile = view.findViewById(R.id.go_profile);
        keep_editing = view.findViewById(R.id.keep_edit);

        go_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelEdit();
                alertDialog3.dismiss();
            }
        });

        keep_editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog3.dismiss();
            }
        });
    }


    public void update_successful_alert_dialog() {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f5___xx_alert__category_nice_update, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        final AlertDialog alertDialog4 = builder.create();
        alertDialog4.setCanceledOnTouchOutside(false);
        alertDialog4.setCancelable(false);
        alertDialog4.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog4.show();

        go_back_profile = view.findViewById(R.id.gotoprofile);

        go_back_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelEdit();
                alertDialog4.dismiss();
            }
        });
    }


    public void CancelEdit() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.owo, profileFragment);
        fragmentTransaction.addToBackStack(null);

        // Realizamos el cambio de fragmento
        fragmentTransaction.commit();
    }


    private void UpdateCategory(String categoryName, String categoryImage, List<Element> elements){
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("id_cat", idCat);
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
        Call<Void> call = categoriesAPI.updateCategory(requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // La solicitud fue exitosa
                    Log.d("CategoryActivity", "Categoría creada exitosamente");
                    update_successful_alert_dialog();
                } else {
                    // La solicitud no fue exitosa
                    Log.e("CategoryActivity", "Error al crear la categoría: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Error de red o error en la respuesta
                Log.e("CategoryActivity", "Error en la llamada: " + t.getMessage());
            }
        });
    }


    public void waitAlert(){
        // Construir el nuevo AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.ff___all_fragments_loading_alert_dialog, null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setView(view);

        waitalertDialog = builder.create();
        waitalertDialog.setCanceledOnTouchOutside(false);
        waitalertDialog.setCancelable(false);
        waitalertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        waitalertDialog.show();
    }


    public void dismissWaitAlert() {
        waitalertDialog.dismiss();
    }
}