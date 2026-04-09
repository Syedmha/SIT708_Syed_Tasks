package com.example.task31c;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText nameInput;
    Button startButton;
    Switch themeToggle;
    LinearLayout mainLayout;

    // SharedPreferences: simple key-value storage built into Android
    // We use it to remember the dark mode setting across screens
    SharedPreferences prefs;
    boolean isDarkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link Java variables to XML views using their IDs
        nameInput = findViewById(R.id.nameInput);
        startButton = findViewById(R.id.startButton);
        themeToggle = findViewById(R.id.themeToggle);
        mainLayout = findViewById(R.id.mainLayout);

        // Load saved preferences (dark mode state + previously entered name)
        prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);
        String savedName = prefs.getString("userName", "");

        // Pre-fill the name field (Subtask 3 - session persistence)
        nameInput.setText(savedName);

        // Apply the saved theme when the screen loads
        applyTheme(isDarkMode);
        themeToggle.setChecked(isDarkMode);

        // Listen for theme toggle changes (Subtask 5)
        themeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDarkMode = isChecked;
                // Save the preference so other screens know the theme
                prefs.edit().putBoolean("darkMode", isDarkMode).apply();
                applyTheme(isDarkMode);
            }
        });

        // Start Quiz button click
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();

                // Validate name is not empty
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the name so ResultActivity can pass it back
                prefs.edit().putString("userName", name).apply();

                // Start QuizActivity and pass the name
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("userName", name);
                startActivity(intent);
            }
        });
    }

    // Applies dark or light colors to all views on this screen
    void applyTheme(boolean dark) {
        TextView titleText = findViewById(R.id.titleText);
        TextView nameLabel = findViewById(R.id.nameLabel); // we'll add this ID below
        EditText nameInputField = findViewById(R.id.nameInput);

        if (dark) {
            mainLayout.setBackgroundColor(Color.parseColor("#121212"));
            titleText.setTextColor(Color.WHITE);
            nameInputField.setTextColor(Color.WHITE);
            nameInputField.setHintTextColor(Color.LTGRAY);
            themeToggle.setTextColor(Color.WHITE);
        } else {
            mainLayout.setBackgroundColor(Color.WHITE);
            titleText.setTextColor(Color.BLACK);
            nameInputField.setTextColor(Color.BLACK);
            nameInputField.setHintTextColor(Color.GRAY);
            themeToggle.setTextColor(Color.BLACK);
        }
    }
}