package com.example.urchoice2.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.urchoice2.Adapters.MyPagerAdapter;
import com.example.urchoice2.R;
import com.google.android.material.tabs.TabLayout;

public class SavedFavFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public SavedFavFragment() {}

    public static SavedFavFragment newInstance(String param1, String param2) {
        SavedFavFragment fragment = new SavedFavFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f4__x__fragment_saved_favourite, container, false);
        TabLayout tabLayout = view.findViewById(R.id.ftab_layout);
        ViewPager viewPager = view.findViewById(R.id.fview_pager);

        MyPagerAdapter adapter2 = new MyPagerAdapter(getChildFragmentManager());
        adapter2.addFragment(new tab_SavedFragment(), "SAVED");
        adapter2.addFragment(new tab_FavouriteFragment(), "FAVOURITE");


        // Asignar el adaptador al ViewPager
        viewPager.setAdapter(adapter2);

        // Conectar el ViewPager con el TabLayout
        tabLayout.setupWithViewPager(viewPager);

        // Agregar un OnTabSelectedListener para cambiar entre las pestañas
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Cambiar al fragmento correspondiente al hacer clic en la pestaña
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No se requiere ninguna acción al deseleccionar una pestaña
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No se requiere ninguna acción al volver a seleccionar una pestaña
            }
        });

        return view;
    }
}