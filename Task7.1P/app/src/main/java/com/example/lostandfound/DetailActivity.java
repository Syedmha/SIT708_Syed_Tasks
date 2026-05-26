package com.example.lostandfound;

import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    private int itemId; // We need this to delete the correct database row

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Receive all data passed via Intent from the adapter
        // getIntent().getExtras() holds the key-value pairs we packed in
        itemId = getIntent().getIntExtra("id", -1);
        String type        = getIntent().getStringExtra("type");
        String name        = getIntent().getStringExtra("name");
        String phone       = getIntent().getStringExtra("phone");
        String description = getIntent().getStringExtra("description");
        String date        = getIntent().getStringExtra("date");
        String location    = getIntent().getStringExtra("location");
        String category    = getIntent().getStringExtra("category");
        String imageUri    = getIntent().getStringExtra("imageUri");
        String timestamp = getIntent().getStringExtra("timestamp");


        // Populate all views with the received data
        ((TextView) findViewById(R.id.tvDetailType)).setText(type);
        ((TextView) findViewById(R.id.tvDetailName)).setText(name);
        ((TextView) findViewById(R.id.tvDetailCategory)).setText("Category: " + category);
        ((TextView) findViewById(R.id.tvDetailDescription)).setText("Description: " + description);
        ((TextView) findViewById(R.id.tvDetailDate)).setText("Date: " + date);
        ((TextView) findViewById(R.id.tvDetailLocation)).setText("Location: " + location);
        ((TextView) findViewById(R.id.tvDetailPhone)).setText("Phone: " + phone);
        ((TextView) findViewById(R.id.tvDetailTimestamp))
                .setText("Posted: " + timestamp);

        // Load image if URI exists
        ImageView ivImage = findViewById(R.id.ivDetailImage);
        if (imageUri != null && !imageUri.isEmpty()) {
            Glide.with(this).load(Uri.parse(imageUri)).into(ivImage);
        }

        // Delete button — removes from DB and goes back
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.deleteItem(itemId);  // Pass the ID to delete correct row
            Toast.makeText(this, "Advert removed", Toast.LENGTH_SHORT).show();
            finish(); // Return to list screen
        });
    }
}
