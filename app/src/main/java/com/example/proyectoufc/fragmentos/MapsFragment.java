package com.example.proyectoufc.fragmentos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectoufc.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;

public class MapsFragment extends Fragment {

    private final static String urlMostrarMedicos = "http://clinica-consultas.atwebpages.com/servicios/mostrarTodosMedicos.php";
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.clear();
            LatLng lima = new LatLng(-12.044487, -77.02988);
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            AsyncHttpClient ahcMostrarMedicos = new AsyncHttpClient();

            ahcMostrarMedicos.get(urlMostrarMedicos, new BaseJsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    try {
                        JSONArray jsonArray = new JSONArray(rawJsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            LatLng doctor = new LatLng(jsonArray.getJSONObject(i).getDouble("latitud"),
                                    jsonArray.getJSONObject(i).getDouble("longitud"));
                            Marker marker = googleMap.addMarker(new MarkerOptions().position(doctor).title(jsonArray.getJSONObject(i).getString("nombre_medico")));
                            String genero=jsonArray.getJSONObject(i).getString("genero");
                            if(genero.equals("Masculino")){
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.doctor));
                            }else{
                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.doctora));
                            }
                            if (i == 0) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(doctor, 17));
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {

                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });

            googleMap.addMarker(new MarkerOptions().position(lima).title("Palacio de gobierno"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lima,17));

            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                }
                else
                    googleMap.setMyLocationEnabled(true);

            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}