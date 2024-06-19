package com.example.proyectoufc.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoufc.R;

public class NuevaSugerenciaActivity extends AppCompatActivity implements View.OnClickListener {

    Button sugBtnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nueva_sugerencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sugBtnCancelar=findViewById(R.id.SugBtnCancelar);
        sugBtnCancelar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.SugBtnCancelar){
            cancelar();
        }
    }

    private void cancelar() {
        Intent iCancelar = new Intent(this, BienvenidaActivity.class);
        startActivity(iCancelar);
        finish();
    }
}