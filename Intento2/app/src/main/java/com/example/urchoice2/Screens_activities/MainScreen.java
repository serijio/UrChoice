package com.example.urchoice2.Screens_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.urchoice2.Fragments.CreateCatRoomFragment;
import com.example.urchoice2.Fragments.CreateCategorySubFragment;
import com.example.urchoice2.databinding.A6ActivityMainScreenBinding;
import com.example.urchoice2.Fragments.MainFragment;
import com.example.urchoice2.Fragments.FriendsFragment;

import com.example.urchoice2.Fragments.SavedFavFragment;
import com.example.urchoice2.Fragments.ProfileFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.urchoice2.R;

public class MainScreen extends AppCompatActivity {

    A6ActivityMainScreenBinding binding;
    ImageView backPic;
    BottomNavigationView bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = A6ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        backPic = findViewById(R.id.main_screen_back_pic);
        bottom = findViewById(R.id.bottomNavigationViewMain);

        replaceFragment(new MainFragment());

        binding.bottomNavigationViewMain.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    replaceFragment(new MainFragment());
                } else if (itemId == R.id.nav_friends) {
                    replaceFragment(new FriendsFragment());
                } else if (itemId == R.id.nav_add) {
                    replaceFragment (new CreateCatRoomFragment());
                    //replaceFragment (new CreateCategorySubFragment());
                } else if (itemId == R.id.nav_favs) {
                replaceFragment (new SavedFavFragment());
                } else if (itemId == R.id.nav_user) {
                    replaceFragment (new ProfileFragment());
                }

                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainframe, fragment);
        fragmentTransaction.commit();
    }
}