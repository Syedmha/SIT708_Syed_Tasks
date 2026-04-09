package com.example.task31c;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView congratsText, scoreText;
    Button takeNewQuizButton, finishButton;
    LinearLayout resultLayout;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        congratsText = findViewById(R.id.congratsText);
        scoreText = findViewById(R.id.scoreText);
        takeNewQuizButton = findViewById(R.id.takeNewQuizButton);
        finishButton = findViewById(R.id.finishButton);
        resultLayout = findViewById(R.id.resultLayout);

        // Retrieve score data passed from QuizActivity
        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        String userName = getIntent().getStringExtra("userName");

        // Display score: e.g. "YOUR SCORE: 4/5"
        scoreText.setText("YOUR SCORE: " + score + "/" + total);
        congratsText.setText("Congratulations " + userName + "!");

        // Apply saved theme (Subtask 5 - theme persists across screens)
        prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        applyTheme(isDarkMode);

        // Take New Quiz: go back to MainActivity
        // The name is already saved in SharedPreferences, so it auto-fills (Subtask 3)
        takeNewQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                // FLAG_ACTIVITY_CLEAR_TOP closes all activities in between
                // so we get a fresh MainActivity without stacking activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Finish: close the entire app (Subtask 3)
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // closes ALL activities in the app
            }
        });
    }

    void applyTheme(boolean dark) {
        if (dark) {
            resultLayout.setBackgroundColor(Color.parseColor("#121212"));
            congratsText.setTextColor(Color.WHITE);
            scoreText.setTextColor(Color.WHITE);
        } else {
            resultLayout.setBackgroundColor(Color.WHITE);
            congratsText.setTextColor(Color.BLACK);
            scoreText.setTextColor(Color.BLACK);
        }
    }
}