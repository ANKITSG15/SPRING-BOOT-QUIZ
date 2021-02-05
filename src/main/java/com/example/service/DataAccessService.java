package com.example.service;

import com.example.Application;
import com.example.dto.InpCorrectAns;
import com.example.dto.QuizDtls;
import com.example.entity.QuizInfo;
import com.example.entity.Student;
import com.example.entity.UserDetails;
import com.example.repository.QuizRepository;
import com.example.repository.StudentRepository;
import com.example.repository.UserRepository;
import com.example.util.APIResponse;
import com.example.util.ConfigUtility;
import com.example.util.GlobalUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("dataAccessService")
public class DataAccessService {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Autowired
    private ConfigUtility config;
    @Autowired
    private QuizRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GlobalUtility globalUtility;
    @Autowired
    private StudentRepository studentRepository;

    @SneakyThrows
    public APIResponse fetchValFromJSON(String response) {
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        APIResponse t = obj.readValue(response, APIResponse.class);
        System.out.println(t.getResults() + " " + t.getResponse_code());
        return t;
    }

    public boolean saveToDb(String output, String id) throws JsonProcessingException {
        APIResponse jsonOut = fetchValFromJSON(output);
        Integer num;
        try {
            num = (Integer) jsonOut.getResponse_code();
            if (num == 0) {
                userRepository.save(new UserDetails(id, LocalDateTime.now()));
                UserDetails attemptDetails = fetchUniqueId(id);
                List<Map<String, String>> results = (List<Map<String, String>>) jsonOut.getResults();
                int i = 0;
                for (Map<String, String> ro : results) {
                    i++;

                    repository.save(new QuizInfo(attemptDetails.getId(), i,
                            ro.get(config.getProperty("endpoint.results.question")),
                            ro.get(config.getProperty("endpoint.results.correctans"))));
                }
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return true;
    }

    public List<String> getCorrectAns(Integer uniqueId) {
        List<QuizInfo> listInfo = StreamSupport.stream(repository.findAll().spliterator(), false).
                filter((QuizInfo qi) -> qi.getUniqueId().equals(uniqueId)).collect(Collectors.toList());
        List<String> correctAns = new ArrayList<>();
        for (QuizInfo quiz : listInfo) {
            correctAns.add(quiz.getCorrectAns());
        }
        return correctAns;
    }

    public UserDetails fetchUniqueId(String id) {
        List<UserDetails> ud = StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter((UserDetails qi) -> qi.getUserId().equalsIgnoreCase(id)).collect(Collectors.toList());
        UserDetails recentDetails = ud.stream().max(Comparator.comparing(UserDetails::getId)).get();
        System.out.println(recentDetails.getDateOfAttempt() + " " + recentDetails.getUserId() + " " + recentDetails.getId());
        return recentDetails;
    }

    public int testScore(List<String> correctAnswers, String ans) throws JsonProcessingException {
        int score = 0;
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InpCorrectAns jsonOut = obj.readValue(ans, InpCorrectAns.class);

        List<String> markedAnswers = jsonOut.getMarkedAnswers();

        UserDetails usr = fetchUniqueId(jsonOut.getStdId());

        correctAnswers = getCorrectAns(usr.getId());

        if (markedAnswers.size() != correctAnswers.size())
            return -1;

        for (int i = 0; i < markedAnswers.size(); i++) {
            if (markedAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i)))
                score++;
        }
        return score;
    }

    public String fetchQuiz(String id, int amount, int category, String difficulty, String type) throws JsonProcessingException {
        log.info("fetchQuiz API called");
        if (globalUtility.isValidAmount(amount).isFlag() && globalUtility.isValidCat(category).isFlag() &&
                globalUtility.isValidDiffLevel(difficulty).isFlag() && globalUtility.isValidType(type).isFlag()) {
            log.info("fetchQues API : fields are validated successfully");
            QuizDtls qdtls = new QuizDtls(amount, category, difficulty, type);
            String response = globalUtility.hitOpenAPI(qdtls, 4);
            if (saveToDb(response, id)) {
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

    public String fetchQuizByQues(String id, int amount) throws JsonProcessingException {
        log.info("fetchByQues API called");
        if (globalUtility.isValidAmount(amount).isFlag()) {
            log.info("fetchByQues API : field validation successful");
            QuizDtls qdtls = new QuizDtls(amount);
            String response = globalUtility.hitOpenAPI(qdtls, 1);
            if (saveToDb(response, id)) {
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

    public String userLogin(String studentId, String password) {
        List<Student> student = StreamSupport.stream(studentRepository.findAll().spliterator(), false).
                filter((Student s) -> s.getStudentId().equals(studentId) && s.getPassword().equals(password)).collect(Collectors.toList());
        if (student.size() == 0)
            return "INVALID USER";
        return "SUCCESS";
    }
}
