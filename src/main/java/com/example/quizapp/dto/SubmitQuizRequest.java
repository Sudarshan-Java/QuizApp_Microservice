package com.example.quizapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class SubmitQuizRequest {

    @NotBlank
    private String userName;

    @NotEmpty
    private List<String> options;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
}
