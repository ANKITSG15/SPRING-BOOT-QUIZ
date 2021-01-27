package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class InpCorrectAns {

    private String stdId;
    private ArrayList<String> markedAnswers;
}
