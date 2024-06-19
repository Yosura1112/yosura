package com.example.proyectoufc.actividades;

import android.content.Intent;
import android.content.pm.ActivityInfo; // Importa la clase correcta
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoufc.R;
import com.example.proyectoufc.clases.Menu;
import com.example.proyectoufc.clases.Paciente;

public class PagarActivity extends AppCompatActivity implements Menu {

    Paciente paciente;
    TextView lblSaludo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bloquear la orientación de la pantalla en vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        lblSaludo = findViewById(R.id.bieLblSaludo);
    }

    @Override
    public void onClickMenu(int idBoton) {
        Intent iMenu = new Intent(this, MenuPagarActivity.class);
        iMenu.putExtra("idBoton", idBoton);
        startActivity(iMenu);
        finish();
    }
}
