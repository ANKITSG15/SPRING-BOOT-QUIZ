package com.example.service;

import com.example.dto.InpCorrectAns;
import com.example.entity.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateScore {

    private int score = 0;

    @Autowired
    DataAccessService dataAccessService;

    public int TestScore(List<String> correctAnswers, String ans) throws JsonProcessingException {
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InpCorrectAns jsonOut = obj.readValue(ans, InpCorrectAns.class);

        List<String> markedAnswers = jsonOut.getMarkedAnswers();

        UserDetails usr = dataAccessService.fetchUniqueId(jsonOut.getStdId());

        correctAnswers = dataAccessService.getCorrectAns(usr.getId());

        if (markedAnswers.size() != correctAnswers.size())
            return -1;

        for (int i = 0; i < markedAnswers.size(); i++) {
            if (markedAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i)))
                score++;
        }
        return score;
    }
}
