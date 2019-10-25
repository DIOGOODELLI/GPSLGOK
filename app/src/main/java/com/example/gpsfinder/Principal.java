package com.example.gpsfinder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Principal extends Activity {

    private TextView precisao;
    private TextView longitudade;
    private TextView latidade;
    private Button buscar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);

        longitudade = findViewById(R.id.longitude);
        latidade =  findViewById(R.id.latitude);
        precisao = findViewById(R.id.acuracy);

        buscar = findViewById(R.id.buscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(Principal.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Principal.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    LocationFinder finder;
                    double longitude, latitude, acuracy;
                    finder = new LocationFinder(Principal.this);
                    if (finder.canGetLocation()) {

                        //AQUI PODERIA SER FEITO UM WHILE PRA PEGAR A MELHOR LOCALIZAÇÃO

                        latitude = finder.getLatitude();
                        longitude = finder.getLongitude();
                        acuracy = finder.getAccuracy();

                        longitudade.setText(longitude + "");
                        latidade.setText(latitude + "");
                        precisao.setText(acuracy + "");

                        String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Local" + ")";
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

                        startActivity(intent);
                        Toast.makeText(Principal.this, "lat-lng " + latitude + " — " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        finder.showSettingsAlert();
                    }
                }
            }
        });

    }

}
