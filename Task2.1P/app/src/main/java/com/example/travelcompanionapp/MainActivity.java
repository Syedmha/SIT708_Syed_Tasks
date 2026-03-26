package com.example.travelcompanionapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Spinner sourceSpinner, destSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Keep the XML padding and add system bar insets to it
            int left = v.getPaddingLeft() + systemBars.left;
            int top = v.getPaddingTop() + systemBars.top;
            int right = v.getPaddingRight() + systemBars.right;
            int bottom = v.getPaddingBottom() + systemBars.bottom;
            v.setPadding(left, top, right, bottom);
            return insets;
        });

        sourceSpinner = findViewById(R.id.sourceSpinner);
        destSpinner = findViewById(R.id.destSpinner);

        String[] currency = {"USD", "AUD", "EUR", "JPY", "GBP"};
        String[] fuel = {"mpg", "km/l", "gallon", "liters", "nautical mile", "kilometers"};
        String[] temp = {"Fahrenheit", "Celsius", "Kelvin"};

        findViewById(R.id.btnCurrency).setOnClickListener(v -> updateSpinners(currency));
        findViewById(R.id.btnFuel).setOnClickListener(v -> updateSpinners(fuel));
        findViewById(R.id.btnTemp).setOnClickListener(v -> updateSpinners(temp));
    }

    private void updateSpinners(String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(adapter);
        destSpinner.setAdapter(adapter);
    }
}