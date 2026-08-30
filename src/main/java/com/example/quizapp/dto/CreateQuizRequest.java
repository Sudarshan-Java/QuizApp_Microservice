package com.example.quizapp.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateQuizRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String topic;

    private String description;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
