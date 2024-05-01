package com.example.urchoice2.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.urchoice2.R;
import com.google.android.material.textfield.TextInputLayout;

public class CreateRoom extends AppCompatActivity {
    TextInputLayout textInputLayout;
    Spinner spinner;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xx_fragment_create_room_screen);

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
        String[] options = {"CHOOSE CATEGORY","UMA USUME", "SIRGAY"};
        spinner = findViewById(R.id.Spinner01);
        CustomAdapter adapter = new CustomAdapter(this, options, images, backgrounds);
        spinner.setAdapter(adapter);
    }




    public class CustomAdapter extends ArrayAdapter<String> {
        private final Context mContext;
        private final String[] mOptions;
        private final int[] mImages;
        private final int[] mBackgrounds;

        public CustomAdapter(Context context, String[] options, int[] images, int[] backgrounds) {
            super(context, R.layout.xx_fragment_x_spinner_category_list, options);
            this.mContext = context;
            this.mOptions = options;
            this.mImages = images;
            this.mBackgrounds = backgrounds;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.xx_fragment_x_spinner_category_list, parent, false);

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
            View view = inflater.inflate(R.layout.xx_fragment_x_spinner_category_list, parent, false);

            ImageView imageViewIcon = view.findViewById(R.id.imageViewIcon);
            TextView textViewLabel = view.findViewById(R.id.textViewLabel);

            // Set image and background
            imageViewIcon.setImageResource(mImages[position]);
            view.setBackgroundResource(mBackgrounds[position]);

            // Establecer transparencia para el icono


            // Establecer transparencia para el texto
            textViewLabel.setText(mOptions[position]);


            return view;
        }

    }


}