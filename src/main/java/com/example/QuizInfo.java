package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class QuizInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long qId;
    private Long uniqId;
    private String ques;
    private String corectAns;


    protected QuizInfo(){}

    public QuizInfo(long uniqId,long qId, String ques, String corectAns)
    {
        this.uniqId = uniqId;
        this.qId = qId;
        this.ques = ques;
        this.corectAns = corectAns;
    }

    public Long getUniqId() {
//        return uniqId;
        return 1091438L;
    }

    public String getCorectAns() {
        return corectAns;
    }
}
