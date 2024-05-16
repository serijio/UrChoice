package com.example.urchoice2.Fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.R;
import com.google.android.material.button.MaterialButton;

public class CreateCatRoomFragment extends Fragment {
   MaterialButton create_category_button;
   MaterialButton create_private_room_button;

   public CreateCatRoomFragment() {}

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.f3___fragment_create_cat_room_screen, container, false);

      create_category_button = view.findViewById(R.id.create_category_button);
      create_private_room_button = view.findViewById(R.id.create_private_room_button);

      create_category_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            //deshabilitar los botones antes de iniciar la transacción
            create_private_room_button.setEnabled(false);
            create_category_button.setEnabled(false);
            Create_category_button();
         }
      });

      create_private_room_button.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            //deshabilitar los botones antes de iniciar la transacción
            create_category_button.setEnabled(false);
            create_private_room_button.setEnabled(false);
            Create_room_button();
         }
      });

      return view;
   }

   public void Create_room_button() {
      Fragment createRoomFragment = new CreateRoomSubFragment();
      FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
      transaction.replace(R.id.private_screen, createRoomFragment);
      transaction.addToBackStack(null); // Opcional: añadir a la pila de retroceso
      transaction.commit();
   }

   /*public void Create_category_button() {
      try {
         Fragment createCategory = new CreateCategorySubFragment();
         FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
         transaction.replace(R.id.private_screen, createCategory);
         transaction.addToBackStack(null);
         transaction.commit();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }*/

   public void Create_category_button() {
      Fragment createCategoryFragment = new CreateCategorySubFragment();
      FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
      transaction.replace(R.id.private_screen, createCategoryFragment);
      transaction.addToBackStack(null);
      transaction.commit();
   }
}
