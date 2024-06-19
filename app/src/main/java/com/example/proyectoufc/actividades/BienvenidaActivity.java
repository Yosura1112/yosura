package com.example.proyectoufc.actividades;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectoufc.R;
import com.example.proyectoufc.fragmentos.BuzonFragment;
import com.example.proyectoufc.fragmentos.CitasFragment;
import com.example.proyectoufc.fragmentos.FarmaciaFragment;
import com.example.proyectoufc.fragmentos.NuevasCitasFragment;
import com.example.proyectoufc.fragmentos.PerfilFragment;
import com.example.proyectoufc.fragmentos.MapsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BienvenidaActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        bottomNavigationView=findViewById(R.id.bottomNavView);
        frameLayout=findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId=item.getItemId();

                if (itemId==R.id.navCitas){
                    loadFragment(new CitasFragment(),false);
                } else if (itemId==R.id.navFarmacia) {
                    loadFragment(new FarmaciaFragment(),false);
                } else if (itemId==R.id.navNewCitas) {
                    loadFragment(new NuevasCitasFragment(),false);
                } else if (itemId==R.id.navBuzon) {
                    loadFragment(new BuzonFragment(),false);
                }else {
                    loadFragment(new PerfilFragment(),false);
                }


                return true;
            }
        });
        loadFragment(new CitasFragment(),true);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        if (isAppInitialized){
            fragmentTransaction.add(R.id.frameLayout,fragment);
        }else {
            fragmentTransaction.replace(R.id.frameLayout,fragment);
        }

        fragmentTransaction.commit();
    }
}