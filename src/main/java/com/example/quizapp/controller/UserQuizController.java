package com.example.quizapp.controller;

import com.example.quizapp.dto.QuizDetailsResponse;
import com.example.quizapp.dto.QuizListResponse;
import com.example.quizapp.dto.ScoreResponse;
import com.example.quizapp.dto.SubmitQuizRequest;
import com.example.quizapp.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class UserQuizController {

    private final QuizService quizService;

    public UserQuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public List<QuizListResponse> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/{quizId}")
    public QuizDetailsResponse getQuiz(@PathVariable Long quizId) {
        return quizService.getQuizForUser(quizId);
    }

    @PostMapping("/{quizId}/submit")
    public ScoreResponse submitQuiz(@PathVariable Long quizId,
                                    @Valid @RequestBody SubmitQuizRequest request) {
        return quizService.submitQuiz(quizId, request);
    }
}
