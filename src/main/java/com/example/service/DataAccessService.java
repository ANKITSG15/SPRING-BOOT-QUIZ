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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public DataAccessService(ConfigUtility config, QuizRepository quizRepository, UserRepository userRepository, GlobalUtility globalUtility, StudentRepository studentRepository) {
    }

    public APIResponse fetchValFromJSON(String response) throws Exception {
        APIResponse t ;
        try{
            ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            t = obj.readValue(response, APIResponse.class);
        }catch(Exception ex){
            throw new Exception();
        }
        return t;
    }

    public boolean saveQuestions(String output, String id) throws Exception {
        APIResponse jsonOut;
        UserDetails attemptDetails = null;
        Integer num;
        try {
            jsonOut = fetchValFromJSON(output);
            num = (Integer) jsonOut.getResponse_code();
            if (num == 0) {
                if (userRepository.save(new UserDetails(id, LocalDateTime.now())) != null)
                    attemptDetails = fetchUniqueId(id);
                List<Map<String, String>> results = (List<Map<String, String>>) jsonOut.getResults();
                if (results != null && attemptDetails != null) {
                    int i = 0;
                    for (Map<String, String> ro : results) {
                        i++;
                        repository.save(new QuizInfo(attemptDetails.getId(), i,
                                ro.get(config.getProperty("endpoint.results.question")),
                                ro.get(config.getProperty("endpoint.results.correctans"))));
                    }
                }else{
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new Exception();
        }
        return true;
    }

    public List<String> getCorrectAns(Integer uniqueId) throws Exception {
        List<String> correctAns;
        try {
            List<QuizInfo> listInfo = StreamSupport.stream(repository.findAll().spliterator(), false).
                    filter((QuizInfo qi) -> qi.getUniqueId().equals(uniqueId)).collect(Collectors.toList());
            correctAns = new ArrayList<>();
            if (listInfo.size() == 0) {
                for (QuizInfo quiz : listInfo) {
                    correctAns.add(quiz.getCorrectAns());
                }
            }
        } catch (Exception ex) {
            throw new Exception();
        }
        return correctAns;
    }

    public UserDetails fetchUniqueId(String id) throws Exception {
        UserDetails recentDetails = null;
        try {
            List<UserDetails> ud = StreamSupport.stream(userRepository.findAll().spliterator(), false).
                    filter((UserDetails qi) -> qi.getUserId().equalsIgnoreCase(id)).collect(Collectors.toList());
            if(ud.stream().max(Comparator.comparing(UserDetails::getId)).isPresent())
                recentDetails = ud.stream().max(Comparator.comparing(UserDetails::getId)).get();
        } catch (Exception ex) {
            throw new Exception();
        }
        return recentDetails;
    }

    public String fetchQuiz(String id, int amount, int category, String difficulty, String type, int param) {
        try {
            if (globalUtility.isValidAmount(amount).isFlag() && globalUtility.isValidCat(category).isFlag() &&
                    globalUtility.isValidDiffLevel(difficulty).isFlag() && globalUtility.isValidType(type).isFlag()) {
                log.info("fetchQuiz API : fields are validated successfully");
                QuizDtls qdtls = new QuizDtls(amount, category, difficulty, type);
                String response = null;

                response = globalUtility.hitOpenAPI(qdtls, param);
                if (response != null) {
                    if (saveQuestions(response, id)) {
                        log.info("fetchQues API : Questions are saved successfully");
                        return response;
                    } else {
                        log.error("invalid url");
                        return "1";
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error in getting response from API " + ex.getMessage());
            return "2";
        }
        log.error("invalid url");
        return "1";
    }

    public int attemptQuiz(String ans) throws JsonProcessingException {
        int score = 0;
        List<String> markedAnswers = null;
        List<String> correctAnswers = new ArrayList<>();
        UserDetails usr = null;
        try {
            ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            InpCorrectAns jsonOut = obj.readValue(ans, InpCorrectAns.class);

            if (jsonOut.getMarkedAnswers() != null)
                markedAnswers = jsonOut.getMarkedAnswers();
            if (markedAnswers.size() == 0) {
                return 1;
            }
            if (jsonOut.getStdId() != null)
                usr = fetchUniqueId(jsonOut.getStdId());
            assert usr != null;
            correctAnswers = getCorrectAns(usr.getId());
            if (markedAnswers.size() != correctAnswers.size())
                return 2;
            for (int i = 0; i < markedAnswers.size(); i++) {
                if (markedAnswers.get(i).equalsIgnoreCase(correctAnswers.get(i)))
                    score++;
            }
        } catch (Exception ex) {
            log.error("Error while calculating score " + ex.getMessage());
            return 3;
        }
        log.info("Score is successfully displayed");
        return score;
    }

    public int login(String studentId, String password) {
        try {
            List<Student> student = StreamSupport.stream(studentRepository.findAll().spliterator(), false).
                    filter((Student s) -> s.getStudentId().equals(studentId) && s.getPassword().equals(password)).collect(Collectors.toList());
            if (student.size() == 0) {
                log.info("User is not registered");
                return 1;
            }
        } catch (Exception ex) {
            log.error("Authentication failure due to - " + ex.getMessage());
            return 2;
        }
        log.info("Authentication Success - Valid User");
        return 3;
    }
}
