package com.example.quizapp.dto;

public record ScoreResponse(
        String userName,
        Long quizId,
        int totalQuestions,
        int correctAnswers,
        int wrongAnswers,
        int score,
        double percentage
) {}
