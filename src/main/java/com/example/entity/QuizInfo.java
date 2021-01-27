package com.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class QuizInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer qId;
    private Integer uniqueId;
    private String ques;
    private String correctAns;


    protected QuizInfo() {
    }

    public QuizInfo(Integer uniqueId, Integer qId, String ques, String correctAns) {
        this.uniqueId = uniqueId;
        this.qId = qId;
        this.ques = ques;
        this.correctAns = correctAns;
    }
}
