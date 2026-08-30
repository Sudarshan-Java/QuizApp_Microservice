package com.example.quizapp.dto;

import java.util.List;

public record QuizDetailsResponse(
        Long id,
        String title,
        String topic,
        String description,
        List<QuestionResponse> questions
) {}
