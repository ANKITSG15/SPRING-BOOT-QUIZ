package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ResultOutput {

    private String category;
    private String type;
    private String difficulty;
    private String question;
    private String correctAnswer;
    private ArrayList<String> incorrectAnswers;

}
