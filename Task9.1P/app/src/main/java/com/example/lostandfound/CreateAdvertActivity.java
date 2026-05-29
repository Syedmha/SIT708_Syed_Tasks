package com.example.lostandfound;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// CreateAdvertActivity.java — complete file
public class CreateAdvertActivity extends AppCompatActivity {

    // UI
    private RadioGroup        radioGroupType;
    private RadioButton       radioLost, radioFound;
    private TextInputEditText etName, etPhone, etDesc, etDate, etLocation;
    private Spinner           spinnerCategory;
    private ImageView         imagePreview;
    private Button            btnPickImage, btnSave, btnGetLocation;
    private TextView          tvImageError;

    // Data
    private Uri    selectedImageUri = null;
    private double selectedLat      = 0.0;  // set by Places or GPS
    private double selectedLng      = 0.0;  // set by Places or GPS

    private DatabaseHelper           dbHelper;
    private FusedLocationProviderClient fusedLocationClient;

    private final String[] categories = {
            "Select Category","Electronics","Pets",
            "Wallets","Keys","Bags","Clothing","Documents","Other"
    };

    // ── Image picker ─────────────────────────────────────────────
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    try {
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception e) { e.printStackTrace(); }
                    Glide.with(this).load(uri).into(imagePreview);
                    imagePreview.setVisibility(View.VISIBLE);
                    tvImageError.setVisibility(View.GONE);
                }
            });

    // ── Places Autocomplete launcher ─────────────────────────────
    // Returns the address string + lat/lng from Google Places
    private final ActivityResultLauncher<Intent> placesLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Place place = Autocomplete.getPlaceFromIntent(result.getData());
                    // Fill location text box with the address
                    etLocation.setText(place.getAddress());
                    // Save lat/lng for database storage
                    if (place.getLatLng() != null) {
                        selectedLat = place.getLatLng().latitude;
                        selectedLng = place.getLatLng().longitude;
                    }
                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Toast.makeText(this, "Location search error", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        // Initialise Places SDK — must be done before using autocomplete
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),
                    getString(R.string.google_maps_key));
        }

        // Initialise GPS client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Bind views
        radioGroupType  = findViewById(R.id.radioGroupType);
        radioLost       = findViewById(R.id.radioLost);
        radioFound      = findViewById(R.id.radioFound);
        etName          = findViewById(R.id.etItemName);
        etPhone         = findViewById(R.id.etPhone);
        etDesc          = findViewById(R.id.etDescription);
        etDate          = findViewById(R.id.etDate);
        etLocation      = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imagePreview    = findViewById(R.id.imagePreview);
        btnPickImage    = findViewById(R.id.btnPickImage);
        btnSave         = findViewById(R.id.btnSave);
        btnGetLocation  = findViewById(R.id.btnGetLocation);
        tvImageError    = findViewById(R.id.tvImageError);

        dbHelper = new DatabaseHelper(this);

        // Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Location field tap → open Places Autocomplete
        etLocation.setOnClickListener(v -> openPlacesAutocomplete());

        // GPS button
        btnGetLocation.setOnClickListener(v -> getCurrentLocation());

        btnPickImage.setOnClickListener(v -> pickImageFromGallery());
        btnSave.setOnClickListener(v -> saveAdvert());
    }

    // ── openPlacesAutocomplete ───────────────────────────────────
    private void openPlacesAutocomplete() {
        // Tell Places which fields we want back
        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
        );
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        placesLauncher.launch(intent);
    }

    // ── getCurrentLocation ───────────────────────────────────────
    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        // Check permission first
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }
        // Get last known location from GPS
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                selectedLat = location.getLatitude();
                selectedLng = location.getLongitude();
                // Show coordinates in the location field
                String coords = selectedLat + ", " + selectedLng;
                etLocation.setText(coords);
                Toast.makeText(this, "Location captured!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Could not get location. Make sure GPS is on.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ── onRequestPermissionsResult ───────────────────────────────
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation(); // retry now permission is granted
        } else if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imagePickerLauncher.launch("image/*");
        }
    }

    // ── pickImageFromGallery ─────────────────────────────────────
    private void pickImageFromGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }
        imagePickerLauncher.launch("image/*");
    }

    // ── saveAdvert ───────────────────────────────────────────────
    private void saveAdvert() {
        String type     = radioLost.isChecked() ? "Lost" : "Found";
        String name     = etName.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String desc     = etDesc.getText().toString().trim();
        String date     = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        boolean hasError = false;
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Name and Phone are required!", Toast.LENGTH_SHORT).show();
            hasError = true;
        }
        if (category.equals("Select Category")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            hasError = true;
        }
        if (selectedImageUri == null) {
            tvImageError.setVisibility(View.VISIBLE);
            hasError = true;
        }
        if (hasError) return;

        String timestamp = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());

        LostFoundItem item = new LostFoundItem(
                type, name, phone, desc, date,
                location, category, selectedImageUri.toString());
        item.setTimestamp(timestamp);
        item.setLatitude(selectedLat);   // save GPS coords
        item.setLongitude(selectedLng);  // save GPS coords

        long result = dbHelper.insertItem(item);
        if (result != -1) {
            Toast.makeText(this, "Advert saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving advert", Toast.LENGTH_SHORT).show();
        }
    }
}
