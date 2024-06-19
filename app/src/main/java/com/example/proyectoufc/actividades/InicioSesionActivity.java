package com.example.proyectoufc.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoufc.R;
import com.example.proyectoufc.clases.Paciente;
import com.example.proyectoufc.sqlite.ProyectoUFC;
import com.example.proyectoufc.clases.Hash;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class InicioSesionActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String urlIniciarSesion="http://clinica-consultas.atwebpages.com/servicios/iniciarSesion.php";
       EditText txtCorreo,txtClave;
       CheckBox checkRecordar;
       Button BtnIniciarSesion,BtnRegistrarte,BtnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_sesion);

        txtCorreo = findViewById(R.id.logTxtCorreo);
        txtClave=findViewById(R.id.logTxtClave);
        checkRecordar =findViewById(R.id.logChkRecordar);
        BtnIniciarSesion =findViewById(R.id.logBtnIniciarSesión);
        BtnRegistrarte =findViewById(R.id.logBtnRegistrate);
        BtnSalir =findViewById(R.id.logBtnSalir);

        BtnIniciarSesion.setOnClickListener(this);
        BtnRegistrarte.setOnClickListener(this);
        BtnSalir.setOnClickListener(this);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        validarRecordarSesion();
    }

    private void validarRecordarSesion() {
        ProyectoUFC py=new ProyectoUFC(this);
        if(py.recordoSesion())
            InciarSesion(py.getValue("correo"),py.getValue("clave"),true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.logBtnSalir)
            salir();
        else if(v.getId()==R.id.logBtnIniciarSesión)
            InciarSesion(txtCorreo.getText().toString().trim().toLowerCase(),txtClave.getText().toString(),false);
        else if (v.getId()==R.id.logBtnRegistrate)
             Registrarte();

    }

    private void salir() {
        System.exit(0);
    }


    private void InciarSesion(String correo_electronico,String contrasena,boolean recordar) {
        ProyectoUFC py=new ProyectoUFC(this);
        Hash hash=new Hash();
        AsyncHttpClient ahcIniciarSesion=new AsyncHttpClient();
        RequestParams params =new RequestParams();

        contrasena = recordar == true ? contrasena : hash.StringToHash(contrasena, "SHA256").toLowerCase();

        params.add("correo_electronico",correo_electronico);
        params.add("contrasena",contrasena);

        ahcIniciarSesion.post(urlIniciarSesion, params, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if(statusCode==200){
                    try {
                        JSONArray jsonArray=new JSONArray(rawJsonResponse);
                        if (jsonArray.length()>0){
                            int id=jsonArray.getJSONObject(0).getInt("id_paciente");
                            if (id==-1)
                                // Después de agregar los parámetros a la solicitud
                                Toast.makeText(getApplicationContext(), "Datos incorrectos", Toast.LENGTH_LONG).show();

                            else {
                                Paciente paciente=new Paciente();
                                paciente.setId(id);
                                paciente.setDni_paciente(jsonArray.getJSONObject(0).getString("dni_paciente"));
                                paciente.setNombres(jsonArray.getJSONObject(0).getString("nombres"));
                                paciente.setApellidos(jsonArray.getJSONObject(0).getString("apellidos"));
                                paciente.setCorreo_electronico(jsonArray.getJSONObject(0).getString("correo_electronico"));
                                paciente.setContrasena(jsonArray.getJSONObject(0).getString("contrasena"));
                                paciente.setId_distrito(jsonArray.getJSONObject(0).getInt("id_distrito"));
                                paciente.setDirecion(jsonArray.getJSONObject(0).getString("direccion"));
                                DateFormat format=new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                                Date fecha_nac=format.parse(jsonArray.getJSONObject(0).getString("fecha_nac"));
                                paciente.setFecha_nac(fecha_nac);

                                if(checkRecordar.isChecked())
                                    py.agregarUsuario(paciente.getId(),paciente.getCorreo_electronico(),paciente.getContrasena());

                                Intent iBienvenido=new Intent(getApplicationContext(),BienvenidaActivity.class);
                                startActivity(iBienvenido);
                                finish();


                            }
                        }
                    }catch (JSONException | ParseException e){
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(),"Error: "+statusCode,Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
    }

    private void Registrarte() {
        Intent iRegistro =new Intent(this,RegistrodeUsuarioActivity.class);
        startActivity(iRegistro);
        finish();
    }

}

