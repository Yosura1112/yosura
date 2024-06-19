package com.example.proyectoufc.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectoufc.R;
import com.example.proyectoufc.actividades.CarritoActivity;
import com.example.proyectoufc.sqlite.ProyectoUFC;
import com.google.android.material.textfield.TextInputEditText;

public class FarmaciaFragment extends Fragment implements View.OnClickListener{

    TextInputEditText nombreBusqueda;
    Button btnBuscar;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FarmaciaFragment() {
        // Required empty public constructor
    }

    public static FarmaciaFragment newInstance(String param1, String param2) {
        FarmaciaFragment fragment = new FarmaciaFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmacia, container, false);

        Button frgBtnCarrito = view.findViewById(R.id.btnCarrito);
        frgBtnCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CarritoActivity.class);
                startActivity(intent);
            }
        });

        nombreBusqueda = view.findViewById(R.id.nombreMedicina);
        btnBuscar = view.findViewById(R.id.btnBuscarMedicina);
        btnBuscar.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnBuscarMedicina) {
            String terminoBusqueda = nombreBusqueda.getText().toString().trim().toLowerCase();
            if (terminoBusqueda.isEmpty()) {
                nombreBusqueda.setError("Por favor, ingrese un término de búsqueda");
            } else {
                guardarBusqueda(terminoBusqueda);
            }
        }
    }

    private void guardarBusqueda(String terminoBusqueda) {
        ProyectoUFC proyectoUFC = new ProyectoUFC(getActivity());
        try {
            proyectoUFC.agregarHistorial(terminoBusqueda);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al guardar el historial de búsqueda", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
