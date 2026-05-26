package com.example.lostandfound;

import android.Manifest;
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
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    private RadioGroup        radioGroupType;
    private RadioButton       radioLost, radioFound;
    private TextInputEditText etName, etPhone, etDesc, etDate, etLocation;
    private Spinner           spinnerCategory;
    private ImageView         imagePreview;
    private Button            btnPickImage, btnSave;
    private TextView          tvImageError; // shows "Image is required" message

    private Uri selectedImageUri = null;
    private DatabaseHelper dbHelper;

    private final String[] categories = {
            "Select Category", "Electronics", "Pets",
            "Wallets", "Keys", "Bags", "Clothing", "Documents", "Other"
    };

    // ── Image picker launcher ────────────────────────────────────────
    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;

                            // Take persistent permission so URI stays readable
                            // after app is closed and reopened
                            try {
                                getContentResolver().takePersistableUriPermission(
                                        uri,
                                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                                );
                            } catch (Exception e) {
                                // Some emulators don't support this — safe to ignore
                                e.printStackTrace();
                            }

                            // Show preview
                            Glide.with(this).load(uri).into(imagePreview);
                            imagePreview.setVisibility(View.VISIBLE);

                            // Hide the error message once image is picked
                            tvImageError.setVisibility(View.GONE);
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

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
        tvImageError    = findViewById(R.id.tvImageError);

        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        btnPickImage.setOnClickListener(v -> pickImageFromGallery());
        btnSave.setOnClickListener(v -> saveAdvert());
    }

    // ── pickImageFromGallery ─────────────────────────────────────────
    private void pickImageFromGallery() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        100);
                return;
            }
        }
        imagePickerLauncher.launch("image/*");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imagePickerLauncher.launch("image/*");
        } else {
            Toast.makeText(this,
                    "Storage permission needed to pick images",
                    Toast.LENGTH_LONG).show();
        }
    }

    // ── saveAdvert ───────────────────────────────────────────────────
    private void saveAdvert() {
        String type     = radioLost.isChecked() ? "Lost" : "Found";
        String name     = etName.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String desc     = etDesc.getText().toString().trim();
        String date     = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        // ── Validation ───────────────────────────────────────────────
        boolean hasError = false;

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this,
                    "Name and Phone are required!", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        if (category.equals("Select Category")) {
            Toast.makeText(this,
                    "Please select a category", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        // Image is now a required field — show inline error if missing
        if (selectedImageUri == null) {
            tvImageError.setVisibility(View.VISIBLE);
            hasError = true;
        }

        if (hasError) return;

        // ── Auto-generate the timestamp ──────────────────────────────
        // Format: "15 Jun 2025, 10:34 AM"
        String timestamp = new SimpleDateFormat(
                "dd MMM yyyy, hh:mm a", Locale.getDefault()
        ).format(new Date());

        // ── Build and save item ──────────────────────────────────────
        LostFoundItem item = new LostFoundItem(
                type, name, phone, desc, date,
                location, category, selectedImageUri.toString()
        );

        // Store the formatted timestamp separately so we can display it
        item.setTimestamp(timestamp);

        long result = dbHelper.insertItem(item);

        if (result != -1) {
            Toast.makeText(this, "Advert saved!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving advert", Toast.LENGTH_SHORT).show();
        }
    }
}