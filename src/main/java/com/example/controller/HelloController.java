package com.example.controller;

import com.example.service.DataAccessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {
    List<String> correctAnswers = new ArrayList<>();

    @Autowired
    private DataAccessService dataAccessService;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello Aditya";
    }

    @RequestMapping(value = "/fetchQuiz")
    public String fetchQuiz(@RequestParam String id,
                            @RequestParam int amount,
                            @RequestParam int category,
                            @RequestParam String difficulty,
                            @RequestParam String type) throws JsonProcessingException {
        return dataAccessService.fetchQuiz(id, amount, category, difficulty, type);
    }

    @RequestMapping("/fetchQuizByQues")
    public String fetchQuizByQues(@RequestParam String id,
                                  @RequestParam int amount) throws JsonProcessingException {
        return dataAccessService.fetchQuizByQues(id, amount);
    }

    @PostMapping(value = "/attemptQuiz", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int attemptQuiz(@RequestBody String ans) throws JsonProcessingException {
        return dataAccessService.testScore(correctAnswers, ans);
    }

    @PostMapping("/login")
    String userLogin(@RequestParam String studentId,
                     @RequestParam String password) {
        return dataAccessService.userLogin(studentId, password);
    }
}
