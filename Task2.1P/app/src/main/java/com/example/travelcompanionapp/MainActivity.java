package com.example.travelcompanionapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Spinner sourceSpinner, destSpinner;
    EditText inputValue;
    TextView tvResult;
    String currentCategory = "Currency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            int left = v.getPaddingLeft() + systemBars.left;
            int top = v.getPaddingTop() + systemBars.top;
            int right = v.getPaddingRight() + systemBars.right;
            int bottom = v.getPaddingBottom() + systemBars.bottom;
            v.setPadding(left, top, right, bottom);
            return insets;
        });

        sourceSpinner = findViewById(R.id.sourceSpinner);
        destSpinner = findViewById(R.id.destSpinner);
        inputValue = findViewById(R.id.inputValue);
        tvResult = findViewById(R.id.tvResult);
        Button btnConvert = findViewById(R.id.button4);

        String[] currency = {"USD", "AUD", "EUR", "JPY", "GBP"};
        String[] fuel = {"mpg", "km/l", "gallon", "liters", "nautical mile", "kilometers"};
        String[] temp = {"Fahrenheit", "Celsius", "Kelvin"};

        updateSpinners(currency, "Currency");


        findViewById(R.id.btnCurrency).setOnClickListener(v -> updateSpinners(currency, "Currency"));
        findViewById(R.id.btnFuel).setOnClickListener(v -> updateSpinners(fuel, "Fuel"));
        findViewById(R.id.btnTemp).setOnClickListener(v -> updateSpinners(temp, "Temp"));

        btnConvert.setOnClickListener(v -> performConversion());
    }

    private void updateSpinners(String[] data, String category) {
        this.currentCategory = category; // Update current category
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(adapter);
        destSpinner.setAdapter(adapter);
    }

    private void performConversion() {
        String inputStr = inputValue.getText().toString().trim();

        // 1. Validation: Empty Check
        if (inputStr.isEmpty()) {
            inputValue.setError("Enter a value");
            return;
        }

        double value = Double.parseDouble(inputStr);
        String from = sourceSpinner.getSelectedItem().toString();
        String to = destSpinner.getSelectedItem().toString();
        double result = 0;

        // 2. Identity Check (e.g., USD to USD)
        if (from.equals(to)) {
            tvResult.setText(String.format("%.2f", value));
            return;
        }

        // 3. Validation: Negative Checks (Blocked for Currency and Fuel)
        if (!currentCategory.equals("Temp") && value < 0) {
            Toast.makeText(this, "Value cannot be negative for this category", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Conversion Logic
        switch (currentCategory) {
            case "Currency":
                result = convertCurrency(from, to, value);
                break;
            case "Fuel":
                result = convertFuel(from, to, value);
                break;
            case "Temp":
                result = convertTemp(from, to, value);
                break;
        }

        tvResult.setText(String.format("%.2f", result));
    }
    private double convertCurrency(String from, String to, double val) {
        // Step A: Convert any to USD (2026 rates)
        double inUSD = val;
        if (from.equals("AUD")) inUSD = val / 1.55;
        else if (from.equals("EUR")) inUSD = val / 0.92;
        else if (from.equals("JPY")) inUSD = val / 148.50;
        else if (from.equals("GBP")) inUSD = val / 0.78;

        // Step B: Convert USD to Target
        if (to.equals("AUD")) return inUSD * 1.55;
        if (to.equals("EUR")) return inUSD * 0.92;
        if (to.equals("JPY")) return inUSD * 148.50;
        if (to.equals("GBP")) return inUSD * 0.78;
        return inUSD;
    }

    private double convertFuel(String from, String to, double val) {
        if (from.equals("mpg") && to.equals("km/l")) return val * 0.425;
        if (from.equals("km/l") && to.equals("mpg")) return val / 0.425;
        if (from.equals("gallon") && to.equals("liters")) return val * 3.785;
        if (from.equals("liters") && to.equals("gallon")) return val / 3.785;
        if (from.equals("nautical mile") && to.equals("kilometers")) return val * 1.852;
        if (from.equals("kilometers") && to.equals("nautical mile")) return val / 1.852;
        return val;
    }

    private double convertTemp(String from, String to, double val) {
        if (from.equals("Celsius") && to.equals("Fahrenheit")) return (val * 1.8) + 32;
        if (from.equals("Fahrenheit") && to.equals("Celsius")) return (val - 32) / 1.8;
        if (from.equals("Celsius") && to.equals("Kelvin")) return val + 273.15;
        if (from.equals("Kelvin") && to.equals("Celsius")) return val - 273.15;
        if (from.equals("Fahrenheit") && to.equals("Kelvin")) return ((val - 32) / 1.8) + 273.15;
        return val;
    }

}