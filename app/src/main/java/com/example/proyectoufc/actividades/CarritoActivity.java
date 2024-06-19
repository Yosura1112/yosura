package com.example.proyectoufc.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoufc.R;

public class CarritoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCarritoPagar;
    private Spinner spinnerQuantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerQuantity = findViewById(R.id.spinner_quantity);

        // Crear un ArrayAdapter usando un array de strings para los números del 1 al 10
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quantities, android.R.layout.simple_spinner_item);

        // Especificar el diseño de la lista desplegable
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el ArrayAdapter al Spinner
        spinnerQuantity.setAdapter(adapter);

        btnCarritoPagar=findViewById(R.id.btnPagar);
        btnCarritoPagar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnPagar)
            pagar();
    }

    private void pagar() {
        Intent iPagar = new Intent(this, PagarActivity.class);
        startActivity(iPagar);
        finish();
    }
}