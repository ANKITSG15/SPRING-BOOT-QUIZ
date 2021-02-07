package com.example.controller;

import com.example.Application;
import com.example.dto.QuizDtls;
import com.example.repository.StudentRepository;
import com.example.service.DataAccessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.util.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GlobalUtility globalUtility;
    @Autowired
    private DataAccessService dataAccessService;
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/Up")
    public String index() {
        return "Up and Running";
    }

    @RequestMapping(value = "/fetchQuiz")
    public ResponseEntity<String> fetchQuiz(@RequestParam String id,
                                            @RequestParam int amount,
                                            @RequestParam int category,
                                            @RequestParam String difficulty,
                                            @RequestParam String type) throws Exception {
        log.info("fetchQues API called");
        String response = dataAccessService.fetchQuiz(id, amount, category, difficulty, type, 5);

        if (response.equals("1"))
            return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
        else if (response.equals("2"))
            return new ResponseEntity<String>("Error in getting response from API ", HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/fetchQuizByQues")
    public ResponseEntity<String> fetchQuizByQues(@RequestParam String id,
                                              @RequestParam int amount) throws Exception {

        int category = -1;
        String difficulty = "";
        String type = "";

        log.info("fetchQuizByQues API called");
        String response = dataAccessService.fetchQuiz(id, amount, category, difficulty, type, 1);

        if (response.equals("1"))
            return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
        else if (response.equals("2"))
            return new ResponseEntity<String>("Error in getting response from API ", HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/fetchQuizByCategory")
    public ResponseEntity<String> fetchQuizByCategory(@RequestParam String id,
                                                  @RequestParam int category) throws Exception {

        int amount = 10;
        String difficulty = "";
        String type = "";

        log.info("fetchQuizByCategory API called");
        String response = dataAccessService.fetchQuiz(id, amount, category, difficulty, type, 2);

        if (response.equals("1"))
            return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
        else if (response.equals("2"))
            return new ResponseEntity<String>("Error in getting response from API ", HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/fetchQuizByDifficulty")
    public ResponseEntity<String> fetchQuizByDifficulty(@RequestParam String id,
                                                      @RequestParam String difficulty) throws Exception {

        int amount = 10;
        int category = -1;
        String type = "";

        log.info("fetchQuizByDifficulty API called");
        String response = dataAccessService.fetchQuiz(id, amount, category, difficulty, type, 3);

        if (response.equals("1"))
            return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
        else if (response.equals("2"))
            return new ResponseEntity<String>("Error in getting response from API ", HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/fetchQuizByType")
    public ResponseEntity<String> fetchQuizByType(@RequestParam String id,
                                                      @RequestParam String type) throws Exception {

        int amount = 10;
        int category = -1;
        String difficulty = "";

        log.info("fetchQuizByType API called");
        String response = dataAccessService.fetchQuiz(id, amount, category, difficulty, type, 4);

        if (response.equals("1"))
            return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
        else if (response.equals("2"))
            return new ResponseEntity<String>("Error in getting response from API ", HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/attemptQuiz", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> attemptQuiz(@RequestBody String ans) throws JsonProcessingException {

        int response = dataAccessService.attemptQuiz(ans);
        switch (response) {
            case 1: {
                return new ResponseEntity<String>("No answer is submitted", HttpStatus.BAD_REQUEST);
            }
            case 2: {
                return new ResponseEntity<String>("Please answer all the questions", HttpStatus.BAD_REQUEST);
            }
            case 3: {
                return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            default: {
                return new ResponseEntity<String>("Score is - " + String.valueOf(response), HttpStatus.OK);
            }
        }
    }

    @PostMapping("/login")
    public @ResponseBody
    ResponseEntity<String> userLogin(@RequestParam String studentId, @RequestParam String password) {
        log.info("/login is called");

        int response = dataAccessService.login(studentId, password);

        switch (response) {
            case 1:
                return new ResponseEntity<>("Authentication Failure", HttpStatus.UNAUTHORIZED);
            case 2:
                return new ResponseEntity<String>("Authentication Failure due to internal error ", HttpStatus.INTERNAL_SERVER_ERROR);
            case 3:
                return new ResponseEntity<>("Authentication SUCCESS", HttpStatus.OK);
            default:
                System.out.println("Login Failure");
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
