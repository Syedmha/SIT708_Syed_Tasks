package com.example.task31c;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    // UI elements
    TextView progressText, questionTitle, questionText;
    ProgressBar progressBar;
    Button btnOption0, btnOption1, btnOption2, btnOption3, actionButton;
    Switch themeToggle;
    LinearLayout quizLayout;

    // Quiz state variables
    int currentQuestionIndex = 0;  // tracks which question we're on
    int score = 0;                  // tracks how many correct answers
    boolean answered = false;       // tracks if current question was submitted

    // Store all 4 buttons in an array so we can loop through them easily
    Button[] optionButtons;

    String userName;
    SharedPreferences prefs;
    boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get views
        progressText = findViewById(R.id.progressText);
        questionTitle = findViewById(R.id.questionTitle);
        questionText = findViewById(R.id.questionText);
        progressBar = findViewById(R.id.progressBar);
        btnOption0 = findViewById(R.id.btnOption0);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        actionButton = findViewById(R.id.actionButton);
        themeToggle = findViewById(R.id.themeToggle);
        quizLayout = findViewById(R.id.quizLayout);

        // Put buttons into array for easy access
        optionButtons = new Button[]{btnOption0, btnOption1, btnOption2, btnOption3};

        // Get data passed from MainActivity
        userName = getIntent().getStringExtra("userName");

        // Load theme preference
        prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);
        applyTheme(isDarkMode);
        themeToggle.setChecked(isDarkMode);

        // Theme toggle listener
        themeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDarkMode = isChecked;
                prefs.edit().putBoolean("darkMode", isDarkMode).apply();
                applyTheme(isDarkMode);
            }
        });

        // Load the first question
        loadQuestion(currentQuestionIndex);

        // Set click listeners for each answer button
        // When clicked, we record which index (0,1,2,3) was selected
        btnOption0.setOnClickListener(v -> selectAnswer(0));
        btnOption1.setOnClickListener(v -> selectAnswer(1));
        btnOption2.setOnClickListener(v -> selectAnswer(2));
        btnOption3.setOnClickListener(v -> selectAnswer(3));

        // The main action button toggles between "SUBMIT" and "NEXT"
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonLabel = actionButton.getText().toString();

                if (buttonLabel.equals("SUBMIT")) {
                    // Check if user actually selected an answer first
                    if (selectedIndex == -1) {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    submitAnswer();
                } else {
                    // Button says NEXT or FINISH
                    goToNextQuestion();
                }
            }
        });
    }

    // Loads question data into the UI for the given question index
    void loadQuestion(int index) {
        answered = false;
        actionButton.setText("SUBMIT");

        // Update progress text: e.g. "Question 2/5"
        progressText.setText("Question " + (index + 1) + "/" + QuizData.questions.length);

        // Update progress bar:
        // Formula: (questions completed so far / total) * 100
        int progressPercent = (index * 100) / QuizData.questions.length;
        progressBar.setProgress(progressPercent);

        questionTitle.setText("Android question title:");
        questionText.setText(QuizData.questions[index]);

        // Set button text from the options array and reset color to default
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(QuizData.options[index][i]);
            optionButtons[i].setBackgroundTintList(
                    getResources().getColorStateList(R.color.btn_default));
            optionButtons[i].setEnabled(true);  // re-enable all buttons
        }
    }

    // Tracks which button the user selected (highlights it slightly)
    int selectedIndex = -1;

    void selectAnswer(int index) {
        if (answered) return; // prevent re-selection after submit

        selectedIndex = index;

        // Visual feedback: highlight selected button, reset others
        for (int i = 0; i < optionButtons.length; i++) {
            if (i == selectedIndex) {
                optionButtons[i].setAlpha(0.7f); // slightly dim to show selection
            } else {
                optionButtons[i].setAlpha(1.0f);
            }
        }
    }

    void submitAnswer() {
        if (selectedIndex == -1) return; // no answer selected

        answered = true;
        int correctIndex = QuizData.correctAnswers[currentQuestionIndex];

        // Always turn the correct button GREEN
        optionButtons[correctIndex].setBackgroundTintList(
                getResources().getColorStateList(R.color.btn_correct));

        // If user selected wrong answer, turn their choice RED
        if (selectedIndex != correctIndex) {
            optionButtons[selectedIndex].setBackgroundTintList(
                    getResources().getColorStateList(R.color.btn_wrong));
        } else {
            score++; // correct answer — increment score
        }

        // Disable all buttons so user cannot change answer (Subtask 1)
        for (Button btn : optionButtons) {
            btn.setEnabled(false);
        }

        // Reset alpha on all buttons
        for (Button btn : optionButtons) {
            btn.setAlpha(1.0f);
        }

        // Change button label based on whether more questions remain
        boolean isLastQuestion = (currentQuestionIndex == QuizData.questions.length - 1);
        if (isLastQuestion) {
            actionButton.setText("FINISH");
        } else {
            actionButton.setText("NEXT");
        }

        selectedIndex = -1; // reset selection tracker
    }

    void goToNextQuestion() {
        currentQuestionIndex++;

        // Check if we've finished all questions
        if (currentQuestionIndex >= QuizData.questions.length) {
            // Go to results screen
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", QuizData.questions.length);
            intent.putExtra("userName", userName);
            startActivity(intent);
            finish(); // close this activity so back button doesn't return here
        } else {
            loadQuestion(currentQuestionIndex);
        }
    }

    void applyTheme(boolean dark) {
        if (dark) {
            quizLayout.setBackgroundColor(Color.parseColor("#121212"));
            questionText.setTextColor(Color.WHITE);
            questionTitle.setTextColor(Color.LTGRAY);
            progressText.setTextColor(Color.WHITE);
        } else {
            quizLayout.setBackgroundColor(Color.WHITE);
            questionText.setTextColor(Color.BLACK);
            questionTitle.setTextColor(Color.DKGRAY);
            progressText.setTextColor(Color.BLACK);
        }
    }
}