package com.example.task31c;

public class QuizData {

    // Each question has: question text, 4 options, correct answer index (0-based)
    public static String[] questions = {
            "What is the ideal sleep duration recommended for muscle recovery?",
            "What does BMI stand for?",
            "Which vitamin does sunlight help your body produce?",
            "What is the recommended daily water intake for an average adult?",
            "How much protein per kilogram of body weight is generally recommended for someone who trains regularly?",
    };

    public static String[][] options = {
            {"4-5 hours", "6-7 hours", "7-9 hours", "10-12 hours"},
            {"Body Mass Index", "Body Muscle Indicator", "Basic Metabolic Info", "Bone Mass Intake"},
            {"Vitamin A", "Vitamin B12", "Vitamin C", "Vitamin D"},
            {"1 litre", "1.5 litres", "2 litres", "3 litres"},
            {"0.5g", "1-2g", "3-4g", "5g"},
    };

    // Index of the correct answer in the options array
    public static int[] correctAnswers = {2, 0, 3, 3, 1};
}