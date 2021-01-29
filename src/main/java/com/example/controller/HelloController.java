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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class HelloController {
    List<String> correctAnswers = new ArrayList<>();
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GlobalUtility globalUtility;
    @Autowired
    private DataAccessService dataAccessService;
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/hello")
    public String index() {
        return "Up and Running";
    }
    @RequestMapping(value = "/fetchQues")
    public String fetchQues(@RequestParam String id,
                            @RequestParam int amount,
                            @RequestParam int category,
                            @RequestParam String difficulty,
                            @RequestParam String type)
            throws JsonProcessingException {

        log.info("fetchQues API called");
        if (globalUtility.isValidAmount(amount).isFlag() && globalUtility.isValidCat(category).isFlag() &&
                globalUtility.isValidDiffLevel(difficulty).isFlag() && globalUtility.isValidType(type).isFlag()) {
            log.info("fetchQues API : fields are validated successfully");
            QuizDtls qdtls = new QuizDtls(amount, category, difficulty, type);
            String response = globalUtility.hitOpenAPI(qdtls, 4);
            if (dataAccessService.saveToDb(response, id)) {
                log.info("fetchQues API : Questions are saved successfully");
                return response;
            } else {
                log.info("invalid url");
                return "Invalid response code : Check the url and pass the valid inputs";
            }
        } else {
            log.info("invalid url");
            return "Check the url and pass the valid inputs";
        }
    }

    @PostMapping(value = "/attemptQuiz", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int attemptQuiz(@RequestBody String ans) throws JsonProcessingException {

        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InpCorrectAns jsonOut = obj.readValue(ans, InpCorrectAns.class);
        List<String> markedAnswers = jsonOut.getMarkedAnswers();
        UserDetails usr = dataAccessService.fetchUniqueId(jsonOut.getStdId());
        correctAnswers = dataAccessService.getCorrectAns(usr.getId());
        if (markedAnswers.size() != correctAnswers.size())
            return -1;
        int score = 0;
        for (int i = 0; i < markedAnswers.size(); i++) {
            if (markedAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i)))
                score++;
        }
        return score;
    }

    @RequestMapping("/fetchByQues")
    public String fetchByQues(@RequestParam String id,
                              @RequestParam Integer amount) throws JsonProcessingException {
        log.info("fetchByQues API called");
        log.info("id : " + id);
        log.info("amount : " + amount);
        if (globalUtility.isValidAmount(amount).isFlag()) {
            log.info("fetchByQues API : field validation successful");
            QuizDtls qdtls = new QuizDtls(amount);
            String response = globalUtility.hitOpenAPI(qdtls, 1);
            if (dataAccessService.saveToDb(response, id)) {
                log.info("fetchQues API : Questions are saved successfully");
                return response;
            } else {
                log.info("invalid url");
                return "Invalid response code : Check the url and pass the valid inputs";
            }
        }
        log.info("Check the url and pass the valid inputs");
        return "invalid URL";
    }

    @PostMapping("/login")
    public @ResponseBody
    String addNewUser(@RequestParam String name, String password) {
        List<Student> student = StreamSupport.stream(studentRepository.findAll().spliterator(), false).
                filter((Student s) -> s.getStudentId().equals(name) && s.getPassword().equals(password)).collect(Collectors.toList());
        if (student.size() == 0)
            return "INVALID USER";
        return "SUCCESS";
    }
}
