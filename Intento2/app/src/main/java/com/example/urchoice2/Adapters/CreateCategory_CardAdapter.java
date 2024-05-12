package com.example.urchoice2.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urchoice2.Classes.Element;
import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CreateCategory_CardAdapter extends RecyclerView.Adapter<CreateCategory_CardAdapter.CardViewHolder> {
    private List<Element> cardsList;

    public CreateCategory_CardAdapter(List<Element> cardsList) {
        this.cardsList = cardsList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_create_category_card, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Element element = cardsList.get(position);
        Bitmap bitmap = base64ToBitmap(element.getImg_elem());
        holder.bind(bitmap, element.getName_elem());
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardName;
        MaterialButton deleteCard;



        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.category_card_data);
            cardName = itemView.findViewById(R.id.category_carddata_name);
            deleteCard = itemView.findViewById(R.id.deleteCard);

        }

        public void bind(Bitmap bitmap, String cardNameText) {
            cardImage.setImageBitmap(bitmap);
            cardName.setText(cardNameText);
            /*
            if (cardsList.isEmpty()){
                emptyText.setVisibility(View.VISIBLE);
            }else{
                emptyText.setVisibility(View.INVISIBLE);
            }
            */
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