package com.example.service;

import com.example.entity.QuizInfo;
import com.example.entity.UserDetails;
import com.example.repository.QuizRepository;
import com.example.repository.UserRepository;
import com.example.util.APIResponse;
import com.example.util.ConfigUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service("dataAccessService")
public class DataAccessService {
    @Autowired
    private final ConfigUtility config;
    @Autowired
    private final QuizRepository repository;
    @Autowired
    private final UserRepository userRepository;

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
}
