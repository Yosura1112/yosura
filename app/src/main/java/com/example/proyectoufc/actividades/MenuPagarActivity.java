package com.example.proyectoufc.actividades;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.content.pm.ActivityInfo;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.proyectoufc.R;
import com.example.proyectoufc.clases.Menu;
import com.example.proyectoufc.fragmentos.PagoEfectivoFragment;
import com.example.proyectoufc.fragmentos.PaypalFragment;
import com.example.proyectoufc.fragmentos.TarjetaFragment;
import com.example.proyectoufc.fragmentos.YapeFragment;

public class MenuPagarActivity extends AppCompatActivity implements Menu {

    Fragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_pagar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bloquear la orientación de la pantalla en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fragments = new Fragment[4];
        fragments[0] = new YapeFragment();
        fragments[1] = new PagoEfectivoFragment();
        fragments[2] = new TarjetaFragment();
        fragments[3] = new PaypalFragment();
        int idBoton = getIntent().getIntExtra("idBoton", -1);
        onClickMenu(idBoton);
    }

    @Override
    public void onClickMenu(int idBoton) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.menRelArea, fragments[idBoton]);
        ft.commit();
    }
}