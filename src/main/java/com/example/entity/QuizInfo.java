package com.example.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@IdClass(CompositeKey.class)
public class QuizInfo {
    @Id
    private Integer uniqueId;
    @Id
    private Integer qId;
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
