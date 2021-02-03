package com.example.controller;

import com.example.Application;
import com.example.dto.InpCorrectAns;
import com.example.dto.QuizDtls;
import com.example.entity.Student;
import com.example.entity.UserDetails;
import com.example.repository.StudentRepository;
import com.example.service.DataAccessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.util.GlobalUtility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class MainController {
    List<String> correctAnswers = new ArrayList<>();
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

    @RequestMapping(value = "/fetchQues")
    public ResponseEntity<String> fetchQues(@RequestParam String id,
                                            @RequestParam int amount,
                                            @RequestParam int category,
                                            @RequestParam String difficulty,
                                            @RequestParam String type) throws Exception {
        log.info("fetchQues API called");
        try {
            if (globalUtility.isValidAmount(amount).isFlag() && globalUtility.isValidCat(category).isFlag() &&
                    globalUtility.isValidDiffLevel(difficulty).isFlag() && globalUtility.isValidType(type).isFlag()) {
                log.info("fetchQues API : fields are validated successfully");
                QuizDtls qdtls = new QuizDtls(amount, category, difficulty, type);
                String response = null;

                response = globalUtility.hitOpenAPI(qdtls, 4);
                if (response != null) {
                    if (dataAccessService.saveQuestions(response, id)) {
                        log.info("fetchQues API : Questions are saved successfully");
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        log.error("invalid url");
                        return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error in getting response from API "+ex.getMessage());
            return new ResponseEntity<String>("Error in getting response from API ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.error("invalid url");
        return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
    }


    @PostMapping(value = "/attemptQuiz", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> attemptQuiz(@RequestBody String ans) throws JsonProcessingException {
        Integer score = 0;
        List<String> markedAnswers = null;
        UserDetails usr = null;
        try {
            ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InpCorrectAns jsonOut = obj.readValue(ans, InpCorrectAns.class);

            if (jsonOut.getMarkedAnswers() != null)
                markedAnswers = jsonOut.getMarkedAnswers();
            if (markedAnswers.size() == 0) {
                return new ResponseEntity<String>("No answer is submitted", HttpStatus.BAD_REQUEST);
            }
            if (jsonOut.getStdId() != null)
                usr = dataAccessService.fetchUniqueId(jsonOut.getStdId());
            correctAnswers = dataAccessService.getCorrectAns(usr.getId());
            if (markedAnswers.size() != correctAnswers.size())
                return new ResponseEntity<String>("Please answer all the questions", HttpStatus.BAD_REQUEST);
            for (int i = 0; i < markedAnswers.size(); i++) {
                if (markedAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i)))
                    score++;
            }
        } catch (Exception ex) {
            log.error("Error while calculating score " + ex.getMessage());
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Score is successfully displayed");
        return new ResponseEntity<String>("Score is - " + String.valueOf(score), HttpStatus.OK);
    }

    @RequestMapping("/fetchByQues")
    public ResponseEntity<String> fetchByQues(@RequestParam String id,
                                              @RequestParam Integer amount) throws Exception {
        log.info("fetchByQues API called");
        if (globalUtility.isValidAmount(amount).isFlag()) {
            log.info("fetchByQues API : field validation successful");
            QuizDtls qdtls = new QuizDtls(amount);
            String response = null;
            try {
                response = globalUtility.hitOpenAPI(qdtls, 1);
            } catch (Exception exception) {
                log.error("Function : hitOpenAPI, Controller : fetchByQues - Error in getting response from API " + exception.getMessage());
                return new ResponseEntity<String>("Function : hitOpenAPI, Controller : fetchByQues - Error in getting response from API " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            try {
                if (dataAccessService.saveQuestions(response, id)) {
                    log.info("fetchByQues API : Questions are saved successfully");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    log.error("invalid url");
                    return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception ex) {
                log.error("Function : saveQuestions, Controller : fetchByQues - Error in saving to database" +ex.getMessage());
                return new ResponseEntity<>("Function : saveQuestions, Controller : fetchByQues - Error in saving to database " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        log.error("Check the url and pass the valid inputs");
        return new ResponseEntity<>("Invalid response code : Check the url and pass the valid inputs", HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/login")
    public @ResponseBody
    ResponseEntity<String> addNewUser(@RequestParam String name, String password) {
        log.info("/login is called");
        try {
            List<Student> student = StreamSupport.stream(studentRepository.findAll().spliterator(), false).
                    filter((Student s) -> s.getStudentId().equals(name) && s.getPassword().equals(password)).collect(Collectors.toList());
            if (student.size() == 0){
                log.info("User is not registered");
                return new ResponseEntity<>("Authentication Failure", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            log.error("Authentication failure due to - "+ ex.getMessage());
            return new ResponseEntity<String>("Authentication Failure due to internal error " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Authentication Success - Valid User");
        return new ResponseEntity<>("Authentication SUCCESS", HttpStatus.OK);
    }
}
