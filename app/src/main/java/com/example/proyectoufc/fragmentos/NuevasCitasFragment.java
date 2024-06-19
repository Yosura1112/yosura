package com.example.proyectoufc.fragmentos;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.proyectoufc.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class NuevasCitasFragment extends Fragment implements View.OnClickListener {

    final String urlMostrarDoctor = "http://clinica-consultas.atwebpages.com/servicios/mostrarMedicos.php";
    final String urlMostrarEspecialidad = "http://clinica-consultas.atwebpages.com/servicios/mostrarEspecialidades.php";
    EditText txtPaciente, txtFechaCita, txtCorreo;
    Button btnReservar, btnCancelar;
    Spinner cboEspecialidad, cboDoctor;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NuevasCitasFragment() {
        // Required empty public constructor
    }

    public static NuevasCitasFragment newInstance(String param1, String param2) {
        NuevasCitasFragment fragment = new NuevasCitasFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevas_citas, container, false);
        txtPaciente = view.findViewById(R.id.logTxtPaciente);
        txtCorreo = view.findViewById(R.id.logTxtCorreo);
        txtFechaCita = view.findViewById(R.id.logTxtFechaCita);
        cboDoctor = view.findViewById(R.id.cboDoctor);
        cboEspecialidad = view.findViewById(R.id.cboEspecialidad);
        btnReservar = view.findViewById(R.id.logBtnReservar);
        btnCancelar = view.findViewById(R.id.logBtnCancelar);

        // Asignar adaptadores iniciales
        cboDoctor.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"-- Seleccione al Doctor --"}));
        cboEspecialidad.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"-- Seleccione la Especialidad --"}));

        // Asignar listener de clic a txtFechaCita
        txtFechaCita.setOnClickListener(this);

        // Llenar datos
        llenarDoctor();
        llenarEspecialidad();
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logTxtFechaCita) {
            mostrarSelectorFechaHora();
        }
    }

    private void mostrarSelectorFechaHora() {
        final Calendar fechaActual = Calendar.getInstance();
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
        int mes = fechaActual.get(Calendar.MONTH);
        int year = fechaActual.get(Calendar.YEAR);

        DatePickerDialog dpd = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                // Ahora mostrar el TimePickerDialog para seleccionar la hora
                mostrarSelectorHora(y, m, d);
            }
        }, year, mes, dia);
        dpd.show();
    }

    private void mostrarSelectorHora(final int year, final int month, final int day) {
        final Calendar horaActual = Calendar.getInstance();
        int hora = horaActual.get(Calendar.HOUR_OF_DAY);
        int minuto = horaActual.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int h, int m) {
                // Actualizar el campo de texto con la fecha y hora seleccionadas
                txtFechaCita.setText(year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-" + (day < 10 ? "0" + day : day) + " " + (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m));
            }
        }, hora, minuto, true);
        tpd.show();
    }

    private void llenarEspecialidad() {
        AsyncHttpClient ahcEspecialidad = new AsyncHttpClient();
        ahcEspecialidad.get(urlMostrarEspecialidad, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        String[] especialidad = new String[jsonArray.length() + 1];
                        especialidad[0] = "-- Seleccione la Especialidad --";
                        for (int i = 1; i < jsonArray.length() + 1; i++) {
                            especialidad[i] = jsonArray.getJSONObject(i - 1).getString("nombre_especialidad");
                        }
                        if (getContext() != null) {
                            cboEspecialidad.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, especialidad));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    private void llenarDoctor() {
        AsyncHttpClient ahcDoctor = new AsyncHttpClient();
        ahcDoctor.get(urlMostrarDoctor, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        String[] doctores = new String[jsonArray.length() + 1];
                        doctores[0] = "-- Seleccione al Doctor --";
                        for (int i = 1; i < jsonArray.length() + 1; i++) {
                            doctores[i] = jsonArray.getJSONObject(i - 1).getString("nombre_medico");
                        }
                        if (getContext() != null) {
                            cboDoctor.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, doctores));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }
}
