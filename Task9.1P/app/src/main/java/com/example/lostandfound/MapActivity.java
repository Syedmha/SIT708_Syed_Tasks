package com.example.lostandfound;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

// MapActivity.java — complete file
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap               mMap;
    private DatabaseHelper          dbHelper;
    private FusedLocationProviderClient fusedLocationClient;
    private Spinner                 spinnerRadius;
    private Button                  btnApplyRadius;

    // Radius options — 0 means 'show all'
    private final String[] radiusLabels  = {"All", "1 km", "5 km", "10 km", "25 km"};
    private final double[] radiusValues  = { 0,      1,      5,      10,      25 };

    // User's current location (set after permission granted)
    private double userLat = 0;
    private double userLng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        dbHelper            = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        spinnerRadius       = findViewById(R.id.spinnerRadius);
        btnApplyRadius      = findViewById(R.id.btnApplyRadius);

        // Populate radius spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, radiusLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRadius.setAdapter(adapter);

        // Apply radius filter when button tapped
        btnApplyRadius.setOnClickListener(v -> {
            int pos = spinnerRadius.getSelectedItemPosition();
            double radiusKm = radiusValues[pos];
            loadMarkers(radiusKm);
        });

        // Initialise the map asynchronously
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // ── onMapReady ───────────────────────────────────────────────
    // Called automatically when the Google Map is fully loaded
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Request location permission then load map
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableMyLocationAndLoad();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 300);
        }
    }

    // ── enableMyLocationAndLoad ──────────────────────────────────
    @SuppressLint("MissingPermission")
    private void enableMyLocationAndLoad() {
        mMap.setMyLocationEnabled(true); // shows blue dot at user's location

        // Get user's current location, then load all markers
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                userLat = location.getLatitude();
                userLng = location.getLongitude();
                // Move camera to user's location
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(userLat, userLng), 12f));
            }
            // Load all markers (no radius filter yet)
            loadMarkers(0);
        });
    }

    // ── loadMarkers ──────────────────────────────────────────────
    // radiusKm = 0 means show all items regardless of distance
    private void loadMarkers(double radiusKm) {
        mMap.clear(); // remove existing markers before redrawing

        List<LostFoundItem> items = dbHelper.getAllItems();
        int shown = 0;

        for (LostFoundItem item : items) {
            double lat = item.getLatitude();
            double lng = item.getLongitude();

            // Skip items with no coordinates saved
            if (lat == 0.0 && lng == 0.0) continue;

            // If a radius is selected, check distance
            if (radiusKm > 0 && userLat != 0) {
                double distanceKm = distanceBetween(userLat, userLng, lat, lng);
                if (distanceKm > radiusKm) continue; // outside radius — skip
            }

            // Add marker to map
            // title = shown in the info window header
            // snippet = shown in the info window subtitle
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, lng))
                    .title(item.getType() + ": " + item.getName())
                    .snippet(item.getLocation()));
            shown++;
        }

        // Let user know if nothing is nearby
        if (shown == 0) {
            Toast.makeText(this,
                    radiusKm > 0
                            ? "No items found within " + (int)radiusKm + " km"
                            : "No items with location saved yet",
                    Toast.LENGTH_LONG).show();
        }
    }

    // ── distanceBetween ──────────────────────────────────────────
    // Uses Android's Location.distanceBetween to calculate km
    // between two lat/lng points
    private double distanceBetween(double lat1, double lng1,
                                   double lat2, double lng2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, results);
        return results[0] / 1000.0; // convert metres → kilometres
    }

    // ── onRequestPermissionsResult ───────────────────────────────
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 300 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableMyLocationAndLoad();
        } else {
            // Permission denied — still load markers, just no user location
            Toast.makeText(this,
                    "Location permission denied — showing all items",
                    Toast.LENGTH_LONG).show();
            loadMarkers(0);
        }
    }
}
