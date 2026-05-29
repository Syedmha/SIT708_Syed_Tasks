package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreate  = findViewById(R.id.btnCreateAdvert);
        Button btnShowAll = findViewById(R.id.btnShowAll);
        Button btnShowMap = findViewById(R.id.btnShowMap); // NEW

        btnCreate.setOnClickListener(v ->
                startActivity(new Intent(this, CreateAdvertActivity.class)));

        btnShowAll.setOnClickListener(v ->
                startActivity(new Intent(this, ListActivity.class)));

        // NEW: open the map screen
        btnShowMap.setOnClickListener(v ->
                startActivity(new Intent(this, MapActivity.class)));
    }
}
