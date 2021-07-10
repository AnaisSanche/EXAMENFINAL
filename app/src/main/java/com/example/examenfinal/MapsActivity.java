package com.example.examenfinal;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.examenfinal.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    String latitudeP;
    String longitudeP;
    String namePoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        latitudeP = getIntent().getStringExtra("latitude");
        longitudeP = getIntent().getStringExtra("longitude");
        namePoke = getIntent().getStringExtra("name");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        float latitude = Float.parseFloat(latitudeP);
        float longitude = Float.parseFloat(longitudeP);


        LatLng sydneyMap = new LatLng(-7.155535515528898, -78.51738888552639);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydneyMap, 15));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title(namePoke));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}