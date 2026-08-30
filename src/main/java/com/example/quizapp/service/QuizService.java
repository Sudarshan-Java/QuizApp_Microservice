package com.example.quizapp.service;

import com.example.quizapp.dto.*;
import com.example.quizapp.entity.Question;
import com.example.quizapp.entity.Quiz;
import com.example.quizapp.repository.QuestionRepository;
import com.example.quizapp.repository.QuizRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public Quiz createQuiz(CreateQuizRequest request) {
        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setTopic(request.getTopic());
        quiz.setDescription(request.getDescription());
        return quizRepository.save(quiz);
    }

    public Question addQuestion(Long quizId, CreateQuestionRequest request) {
        Quiz quiz = getQuiz(quizId);
        validateCorrectOption(request.getCorrectOption());

        Question question = new Question();
        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectOption(request.getCorrectOption().toUpperCase());
        question.setQuiz(quiz);
        return questionRepository.save(question);
    }

    public List<QuizListResponse> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(q -> new QuizListResponse(q.getId(), q.getTitle(), q.getTopic(), q.getDescription()))
                .toList();
    }

    public QuizDetailsResponse getQuizForUser(Long quizId) {
        Quiz quiz = getQuiz(quizId);
        List<QuestionResponse> questions = questionRepository.findByQuizId(quizId).stream()
                .map(q -> new QuestionResponse(q.getId(), q.getQuestionText(), q.getOptionA(), q.getOptionB(), q.getOptionC(), q.getOptionD()))
                .toList();

        return new QuizDetailsResponse(quiz.getId(), quiz.getTitle(), quiz.getTopic(), quiz.getDescription(), questions);
    }

    public ScoreResponse submitQuiz(Long quizId, SubmitQuizRequest request) {
        getQuiz(quizId);
        List<Question> questions = questionRepository.findByQuizId(quizId);

        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz has no questions");
        }

        List<String> options = request.getOptions();
        if (options.size() != questions.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Number of options (" + options.size() + ") does not match number of questions (" + questions.size() + ")");
        }

        int correct = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getCorrectOption().equalsIgnoreCase(options.get(i))) {
                correct++;
            }
        }

        int total = questions.size();
        int wrong = total - correct;
        double percentage = Math.round((correct * 10000.0 / total)) / 100.0;

        return new ScoreResponse(request.getUserName(), quizId, total, correct, wrong, correct, percentage);
    }

    public void deleteQuiz(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found");
        }
        quizRepository.deleteById(quizId);
    }

    private Quiz getQuiz(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    private void validateCorrectOption(String option) {
        if (option == null || !List.of("A", "B", "C", "D").contains(option.toUpperCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "correctOption must be A, B, C or D");
        }
    }
}
