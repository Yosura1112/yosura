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

public class InfoCitaActivity extends AppCompatActivity implements View.OnClickListener {


    Button BtnUnirseConsulta, BtnCargarDocumento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_cita);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BtnUnirseConsulta=findViewById(R.id.btnUnirseconsulta);
        BtnCargarDocumento=findViewById(R.id.btnCargardocumento);

        BtnCargarDocumento.setOnClickListener(this);
        BtnUnirseConsulta.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnUnirseconsulta)
            UnirseConsulta();
        else if (v.getId()==R.id.btnCargardocumento) {
            CargarDocumento();
        }
    }

    private void UnirseConsulta() {
        Intent iUnirseConsulta =new Intent(this,UnirseConsultaActivity.class);
        startActivity(iUnirseConsulta);
        finish();
    }
    private void CargarDocumento() {
        Intent iCargarDocumento =new Intent(this,CargarDocumentoActivity.class);
        startActivity(iCargarDocumento);
        finish();
    }



}