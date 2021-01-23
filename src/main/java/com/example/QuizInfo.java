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

    public Long getqId() {
        return qId;
    }

    public void setqId(Long qId) {
        this.qId = qId;
    }

    public Long getUniqId() {
        return uniqId;
    }

    public void setUniqId(Long uniqId) {
        this.uniqId = uniqId;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getCorectAns() {
        return corectAns;
    }

    public void setCorectAns(String corectAns) {
        this.corectAns = corectAns;
    }

    public String getIncorectAns() {
        return incorectAns;
    }

    public void setIncorectAns(String incorectAns) {
        this.incorectAns = incorectAns;
    }

    private String ques;
    private String corectAns;
    private String incorectAns;

    protected QuizInfo(){}

    public QuizInfo(long uniqId,long qId, String ques, String corectAns)
    {
        this.uniqId = uniqId;
        this.qId = qId;
        this.ques = ques;
        this.corectAns = corectAns;
        //this.incorectAns = incorectAns;
    }

    public String getQues() {
        return ques;
    }


}
