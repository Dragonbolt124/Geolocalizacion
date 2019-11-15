package com.example.geolocalizacion;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;

public class MapsActivity extends Activity implements View.OnClickListener
{
    private GoogleMap mMap;
    private LocationManager locManager;
    private Button btn_act, btn_des;
    private EditText jedlat, jedlon, jedpres, jedprov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        jedlat = findViewById(R.id.xetlat);
        jedlon = findViewById(R.id.xetlon);
        jedpres = findViewById(R.id.xetpres);
        jedprov = findViewById(R.id.xetprov);

        btn_act = findViewById(R.id.xbtnact);
        btn_act.setOnClickListener(this);

        btn_des = findViewById(R.id.xbtndes);
        btn_des.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.xbtnact)
            comenzarLocalizacion();
        else if (id == R.id.xbtndes)
            locManager.removeUpdates(locListener);
    }

    public void comenzarLocalizacion()
    {

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnable = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnable)
        {
            Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settings);
        }
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},101);
            return;
        }
        Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mostrarPosicion(loc);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,locListener);
    }

    public void mostrarPosicion(Location l)
    {
        if(l!=null)
        {
            jedlat.setText("Latitud: "+String.valueOf(l.getLatitude()));
            jedlon.setText("Longitud: "+String.valueOf(l.getLongitude()));
            jedpres.setText("Precision: "+String.valueOf(l.getAccuracy()));
        }
        else
        {
            jedlat.setText("Latitud: (sin datos)");
            jedlon.setText("Longitud: (sin datos)");
            jedpres.setText("Precision: (sin datos)");
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mostrarPosicion(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            jedprov.setText("Provider Status: "+i);
        }

        @Override
        public void onProviderEnabled(String s) {
            jedprov.setText("Proveedor en ON");
        }

        @Override
        public void onProviderDisabled(String s) {
            jedprov.setText("Proveedor en OFF");
        }
    };
}
