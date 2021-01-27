package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QuizInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer qId;
    private Integer uniqId;
    private String ques;
    private String corectAns;


    protected QuizInfo(){}

    public QuizInfo(Integer uniqId,Integer qId, String ques, String corectAns)
    {
        this.uniqId = uniqId;
        this.qId = qId;
        this.ques = ques;
        this.corectAns = corectAns;
    }

    public Integer getUniqId() {
        return uniqId;
    }

    public String getCorectAns() {
        return corectAns;
    }
}
