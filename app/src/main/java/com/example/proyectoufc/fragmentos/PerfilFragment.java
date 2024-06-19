package com.example.proyectoufc.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.proyectoufc.R;
import com.example.proyectoufc.actividades.EditarPerfilActivity;
import com.example.proyectoufc.actividades.InicioSesionActivity;
import com.example.proyectoufc.actividades.NuevaSugerenciaActivity;
import com.example.proyectoufc.sqlite.ProyectoUFC;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener {

    CheckBox chkNotificaciones, chkSonido;
    Spinner cboIdiomas;
    Button btnGuardar,btnCerrarSesion,btnEditarPerfil;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
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
        //return inflater.inflate(R.layout.fragment_perfil, container, false);

        View view =inflater.inflate(R.layout.fragment_perfil,container,false);



        chkNotificaciones=view.findViewById(R.id.frgCfgNotificaciones);
        chkSonido=view.findViewById(R.id.frgCfgSonido);
        cboIdiomas=view.findViewById(R.id.frgCfgIdioma);
        btnGuardar=view.findViewById(R.id.perBtnGuardarCambios);
        btnCerrarSesion=view.findViewById(R.id.perBtnCerrarSesion);
        btnEditarPerfil=view.findViewById(R.id.LogBtnEditarPerfil);

        btnGuardar.setOnClickListener(this);
        btnCerrarSesion.setOnClickListener(this);
        btnEditarPerfil.setOnClickListener(this);

        llenarIdiomas();

        cargarPreferencias();
        return view;

    }

    private void llenarIdiomas(){
        String[] idiomas={"Español","Ingles","Portugues","Qechua"};
        cboIdiomas.setAdapter(new ArrayAdapter<String>(getContext(),
                                                        android.R.layout.simple_spinner_dropdown_item,
                                                        idiomas));
    }

    private void cargarPreferencias(){
        SharedPreferences preferences=getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        boolean notificaciones=preferences.getBoolean("Notificaciones",false);
        boolean sonido=preferences.getBoolean("Sonido",false);
        int idioma=preferences.getInt("Idioma",0);
        chkNotificaciones.setChecked(notificaciones);
        chkSonido.setChecked(sonido);
        cboIdiomas.setSelection(idioma);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.perBtnGuardarCambios){
            guardar();
        }
        else if (v.getId()==R.id.perBtnCerrarSesion) {
            cerrarSesion();
            
        }else {
            abrirEditarPerfil();
        }
    }

    private void abrirEditarPerfil() {
        Intent intent= new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }

    private void cerrarSesion() {
        ProyectoUFC py=new ProyectoUFC(getContext());
        py.eliminarUsuario();
        py.eliminarTablaHistorial();
        getActivity().finish();
        Intent inicioSesion =new Intent(getContext(), InicioSesionActivity.class);
        startActivity(inicioSesion);
    }

    private void guardar() {
        String error="";
        if (cboIdiomas.getSelectedItemPosition()!=0){

            SharedPreferences preferences= getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("Notificaciones",chkNotificaciones.isChecked());
            editor.putBoolean("Sonido",chkSonido.isChecked());
            editor.putInt("Idioma",cboIdiomas.getSelectedItemPosition());
            editor.commit();
            Toast.makeText(getContext(),"Preferencias Guardadas",Toast.LENGTH_SHORT).show();

        }
        else
            error+="Debe elegir un idioma";

        if(error!=null)
            Toast.makeText(getContext(),"Preferencias Guardadas",Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(getContext(),error, Toast.LENGTH_SHORT).show();
        }

    }

}