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

public class MainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public MainFragment() {
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.f1___fragment_main, container, false);

        // Obtener referencias de TabLayout y ViewPager
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        // Crear un adaptador para el ViewPager y agregar las páginas
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MainFragment_Category_SubFragment(), "CATEGORY");
        adapter.addFragment(new MainFragment_Room_SubFragment(), "ROOM");

        // Asignar el adaptador al ViewPager
        viewPager.setAdapter(adapter);

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