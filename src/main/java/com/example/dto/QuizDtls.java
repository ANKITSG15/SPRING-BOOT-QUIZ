package com.example.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizDtls {

    private int amount;
    private int category;
    private String difficulty;
    private String type;

    public QuizDtls(int amount, int category, String difficulty, String type) {
        this.amount = amount;
        this.category = category;
        this.difficulty = difficulty;
        this.type = type;
    }

    public QuizDtls(int amount) {
        this.amount = amount;
    }
}
