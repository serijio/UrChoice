package com.example.urchoice2.Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.R;
import com.example.urchoice2.Screens_activities.StartScreen;
import com.example.urchoice2.Screens_activities.TapToStartScreen;
import com.google.android.material.button.MaterialButton;

public class PrivateFragment extends Fragment {
   MaterialButton create_category_button;
   MaterialButton create_private_room_button;

   public PrivateFragment(){

   }
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.f5___fragment_private_screen,container,false);

      create_private_room_button = view.findViewById(R.id.create_private_room_button);
      create_private_room_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Create_room_button();
         }
      });



      return view;
   }

   public void Create_room_button() {
      Fragment createRoomFragment = new CreateRoomFragment();
      FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
      transaction.replace(R.id.private_screen, createRoomFragment);
      transaction.addToBackStack(null); // Opcional: añadir a la pila de retroceso
      transaction.commit();
   }

}