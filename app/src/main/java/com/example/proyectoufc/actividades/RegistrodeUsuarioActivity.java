package com.example.proyectoufc.actividades;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoufc.R;
import com.example.proyectoufc.clases.Hash;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class RegistrodeUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private final String urlMostrarDistritos = "http://clinica-consultas.atwebpages.com/servicios/mostrarDistritos.php";
    private final String urlAgregarPaciente = "http://clinica-consultas.atwebpages.com/servicios/agregarPaciente.php";
    EditText txtNombreRegistro, txtApellidoRegistro, txtDniRegistro, txtFechaNac, txtTelefonoRegistro, txtDireccionRegistro, txtCorreoRegistro, txtClaveRegistro;
    Spinner cboDistritoRegistro;
    Button btnCrearRegistro, btnCancelarRegistro;
    private CheckBox chkTerminosCondiciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrode_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtNombreRegistro = findViewById(R.id.logTxtNombreRegistro);
        txtApellidoRegistro = findViewById(R.id.logTxtApellidoRegistro);
        txtDniRegistro = findViewById(R.id.logTxtDniRegistro);
        txtFechaNac = findViewById(R.id.logTxtFechaNac);
        txtDireccionRegistro = findViewById(R.id.logTxtDireccionRegistro);
        txtCorreoRegistro = findViewById(R.id.logTxtCorreoRegistro);
        txtClaveRegistro = findViewById(R.id.logTxtClaveRegistro);
        cboDistritoRegistro = findViewById(R.id.cboDistritoRegistro);
        btnCrearRegistro = findViewById(R.id.logBtnCrearRegistro);
        btnCancelarRegistro = findViewById(R.id.logBtnCancelarRegistro);
        chkTerminosCondiciones = findViewById(R.id.chkTerminosCondiciones);
        //Eventos Click
        txtFechaNac.setOnClickListener(this);
        btnCrearRegistro.setOnClickListener(this);
        btnCancelarRegistro.setOnClickListener(this);
        chkTerminosCondiciones.setOnClickListener(this);
        //llamar provincia

        //btnCrearRegistro.setEnabled(false);
        llenarDistritos();
    }

    private void llenarDistritos() {
        AsyncHttpClient ahcDistritos = new AsyncHttpClient();
        String distritos[];
        cboDistritoRegistro.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"-- Seleccione Distrito --"}));

        ahcDistritos.get(urlMostrarDistritos, new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        String[] distritos = new String[jsonArray.length() + 1];
                        distritos[0] = "-- Seleccione Distrito --";
                        for (int i = 1; i < jsonArray.length() + 1; i++)
                            distritos[i] = jsonArray.getJSONObject(i - 1).getString("nombre_distrito");
                        cboDistritoRegistro.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                distritos));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                Toast.makeText(getApplicationContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logTxtFechaNac)
            mostrarSelectorfechas();
        else if (v.getId() == R.id.logBtnCrearRegistro)
            procesarUsuario();
        else if (v.getId() == R.id.logBtnCancelarRegistro)
            cancelar();
        else if (v.getId()==R.id.chkTerminosCondiciones)
            mostrarTerminosyCondiciones(v);
    }

    private void cancelar() {
        finish();
        Intent iInicioSesion = new Intent(this, InicioSesionActivity.class);
        startActivity(iInicioSesion);
    }

    private void procesarUsuario() {
        AsyncHttpClient ahcregistrar = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        Hash hash = new Hash();

        if (validarFormulario()) {
            params.add("dni_paciente", txtDniRegistro.getText().toString());
            params.add("nombres", txtNombreRegistro.getText().toString());
            params.add("apellidos", txtApellidoRegistro.getText().toString());
            params.add("correo_electronico", txtCorreoRegistro.getText().toString());
            params.add("contrasena", hash.StringToHash(txtClaveRegistro.getText().toString(), "SHA-256").toLowerCase());
            params.add("id_distrito", String.valueOf(cboDistritoRegistro.getSelectedItemPosition()));
            params.add("direccion", txtDireccionRegistro.getText().toString());
            params.add("fecha_nac", txtFechaNac.getText().toString());

            ahcregistrar.post(urlAgregarPaciente, params, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if (statusCode == 200) {
                        try {
                            // Verifica si la respuesta contiene el error en HTML
                            if (rawJsonResponse.contains("<b>Fatal error</b>")) {
                                // Maneja el error de entrada duplicada
                                if (rawJsonResponse.contains("Duplicate entry")) {
                                    Toast.makeText(getApplicationContext(), "Error: Entrada duplicada", Toast.LENGTH_LONG).show();
                                } else {
                                    // Maneja otros errores
                                    Toast.makeText(getApplicationContext(), "Error en el servidor: " + rawJsonResponse, Toast.LENGTH_LONG).show();
                                }
                                return;
                            }

                            // Si la respuesta es válida, realiza la conversión
                            int retVal = rawJsonResponse.length() == 0 ? 0 : Integer.parseInt(rawJsonResponse);
                            if (retVal == 1) {
                                Toast.makeText(getApplicationContext(), "Usuario Agregado", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent iInicioSesion = new Intent(getApplicationContext(), InicioSesionActivity.class);
                                startActivity(iInicioSesion);
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getApplicationContext(), "Formato de número inválido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                    Toast.makeText(getApplicationContext(), "ERROR: " + statusCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });
        } else {
            Toast.makeText(this, "Complete Correctamente el formulario", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarSelectorfechas() {
        DatePickerDialog dpd;
        final Calendar fechaActual = Calendar.getInstance();
        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
        int mes = fechaActual.get(Calendar.MONTH);
        int year = fechaActual.get(Calendar.YEAR);
        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                txtFechaNac.setText(y + "-" + ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "-" + (d < 10 ? "0" + d : d));
            }
        }, year, mes, dia);
        dpd.show();
    }

    private boolean validarFormulario() {
        if (txtDniRegistro.getText().toString().isEmpty() || txtDniRegistro.getText().toString().length() != 8)
            return false;
        if (txtNombreRegistro.getText().toString().isEmpty())
            return false;
        if (txtApellidoRegistro.getText().toString().isEmpty())
            return false;
        if (txtClaveRegistro.getText().toString().isEmpty())
            return false;
        if (txtCorreoRegistro.getText().toString().isEmpty())
            return false;
        if (txtDireccionRegistro.getText().toString().isEmpty())
            return false;
        if (txtFechaNac.getText().toString().isEmpty())
            return false;
        if (cboDistritoRegistro.getSelectedItemPosition() == 0)
            return false;
        if (!chkTerminosCondiciones.isChecked())
            return false;

        return true;
    }

    public void mostrarTerminosyCondiciones(View view){
        if(chkTerminosCondiciones.isChecked()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Términos y Condiciones");
            builder.setMessage("Última actualización: 15/06/2024\n" +
                    "\n" +
                    "Bienvenido a “Hospital UPN”, una aplicación móvil diseñada para proporcionar servicios médicos y de salud. Antes de utilizar nuestra aplicación, te recomendamos leer detenidamente los siguientes términos y condiciones que rigen tu uso de esta.\n" +
                    "\n" +
                    "1. Aceptación de los Términos: Al descargar, registrarte o utilizar nuestra aplicación, aceptas cumplir con estos términos y condiciones, así como con nuestra Política de Privacidad.\n" +
                    "\n" +
                    "2. Descripción del Servicio: “Hospital UPN” ofrece a los usuarios la posibilidad de crear cuentas personales, iniciar sesión, recibir notificaciones de mensajes, acceder a una tienda virtual, participar en sesiones de videollamada médica, consultar historiales médicos, programar consultas y almacenar datos en nuestra base de datos segura.\n" +
                    "\n" +
                    "3. Uso Adecuado: Debes utilizar nuestra aplicación de manera adecuada y conforme a la ley. No debes usarla de forma que interfiera con el funcionamiento normal de la misma ni con la experiencia de otros usuarios.\n" +
                    "\n" +
                    "4. Registro de Cuenta: Para utilizar ciertos servicios de la aplicación, es necesario crear una cuenta personal. Debes proporcionar información precisa y actualizada durante el proceso de registro.\n" +
                    "\n" +
                    "5. Privacidad: La privacidad de nuestros usuarios es una prioridad para nosotros. Para obtener información detallada sobre cómo recopilamos, utilizamos y protegemos tus datos personales, consulta nuestra Política de Privacidad.\n" +
                    "\n" +
                    "6. Comunicaciones: Podemos enviarte comunicaciones relacionadas con tu cuenta o con el funcionamiento de la aplicación, incluidas notificaciones sobre mensajes, actualizaciones de servicio o cambios en los términos y condiciones.\n" +
                    "\n" +
                    "7. Compra en la Tienda Virtual: Al realizar compras a través de nuestra tienda virtual integrada, aceptas cumplir con los términos de pago y entrega especificados para cada transacción.\n" +
                    "\n" +
                    "8. Responsabilidad Médica: Hospital UPN proporciona servicios médicos directos y asesoramiento médico profesional. Las consultas y sesiones de videollamada son facilitadas por profesionales de la salud debidamente acreditados.\n" +
                    "\n" +
                    "9. Propiedad Intelectual: Todos los derechos de propiedad intelectual relacionados con la aplicación y su contenido pertenecen a Hospital UPN o a sus licenciantes. No tienes derecho a utilizar estos derechos de ninguna manera sin autorización expresa por escrito.\n" +
                    "\n" +
                    "10. Modificaciones y Actualizaciones: Nos reservamos el derecho de modificar estos términos y condiciones en cualquier momento. Las modificaciones entrarán en vigor inmediatamente después de su publicación en la aplicación.\n");

            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(this,"Por favor, acepta los terminos y condiciones primero",Toast.LENGTH_SHORT).show();
        }
    }
}
