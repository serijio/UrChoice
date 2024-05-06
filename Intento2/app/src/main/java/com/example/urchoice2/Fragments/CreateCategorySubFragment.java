package com.example.urchoice2.Fragments;

import static android.app.Activity.RESULT_OK;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateCategorySubFragment extends Fragment {
    MaterialButton add_new_card_button;
    MaterialButton add_category_image_button;
    RoundedImageView edit_image_button;
    Bitmap selectedBitmap;
    TextView alert_card_textview;
    MaterialButton setCard_data_button;
    List<Element> cardsList;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    private Uri selectedImageUri;

    MaterialButton create_category_button;
    MaterialButton go_to_main_screen;


    private static final int PICK_IMAGE_REQUEST1 = 0;
    private static final int PICK_IMAGE_REQUEST2 = 1;

    public CreateCategorySubFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.f3__sub__fragment_create_category_screen, container, false);
        cardsList = new ArrayList<>();
        /*add_new_card_button = view.findViewById(R.id.add_new_card);

        add_new_card_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_card_alertDialog();
            }
        });
        add_category_image_button = view.findViewById(R.id.add_new_category_image_button);
        add_category_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategoryImageFromGallery();
            }
        });
        cardsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_card_category);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new CardAdapter(cardsList));*/

        return view;
    }

    private void create_card_alertDialog(){
        // Inflar el diseño del AlertDialog
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3__x__fragment_alert_add_category_card_data,null);
        edit_image_button = view.findViewById(R.id.card_image_data);
        setCard_data_button = view.findViewById(R.id.set_data_alert_addcard_button);
        alert_card_textview = view.findViewById(R.id.card_textEdittextlayout);

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

                // Establecer la imagen y el nombre de la carta donde lo desees
                Element newCard = new Element(0, base64Image, cardName, 0);
                cardsList.add(newCard);

                // Notificar al adaptador del RecyclerView que se ha agregado una nueva carta
                recyclerView.getAdapter().notifyItemInserted(cardsList.size() - 1);

                Log.d("TAG", "Tamaño de cardlist : " + cardsList.size() +  " y su primera carta con nombre: " + (cardsList.isEmpty() ? "Lista vacía" : cardsList.get(0).getName_elem()));
                Log.d("TAG", "Tamaño de cardlist : " + cardsList.size() +  " y su primera carta con nombre: " + (cardsList.isEmpty() ? "Lista vacía" : cardsList.get(0).getImg_element()));

                // Cerrar el AlertDialog
                alertDialog.dismiss();
            }
        });

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }
    public void create_new_category_alert_dialog(){
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.f3__x__fragment_category_new_category_alertdialog,null);
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
                Bitmap selectedBitmap2 = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                // Convierte el Bitmap en un Drawable
                //Drawable drawable = new BitmapDrawable(getResources(), selectedBitmap2);
                // Establece el Drawable como fondo del MaterialButton
                //add_category_image_button.setBackground(drawable);
                // Convertir Bitmap a Drawable
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), selectedBitmap2);
                // Establecer el Drawable como fondo del botón
                add_category_image_button.setBackground(bitmapDrawable);

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

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

        private List<Element> cardsList;

        public CardAdapter(List<Element> cardsList) {
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

    public String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        /*evitar que se pete debido a ciertas imagenes sobre todo las de camara*/
             bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }



}