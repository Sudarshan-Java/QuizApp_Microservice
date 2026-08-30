package com.example.quizapp.controller;

import com.example.quizapp.dto.CreateQuestionRequest;
import com.example.quizapp.dto.CreateQuizRequest;
import com.example.quizapp.entity.Question;
import com.example.quizapp.entity.Quiz;
import com.example.quizapp.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/quizzes")
public class AdminQuizController {

    private final QuizService quizService;

    public AdminQuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Quiz createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        return quizService.createQuiz(request);
    }

    @PostMapping("/{quizId}/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public Question addQuestion(@PathVariable Long quizId,
                                 @Valid @RequestBody CreateQuestionRequest request) {
        return quizService.addQuestion(quizId, request);
    }

    @DeleteMapping("/{quizId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable Long quizId) {
        quizService.deleteQuiz(quizId);
    }
}
