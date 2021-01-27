package com.example.service;

import com.example.entity.QuizInfo;
import com.example.entity.UserDetails;
import com.example.repository.QuizRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dto.JsonOutput;
import com.example.dto.ResultOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("dataAccessService")
public class DataAccessService {

    @Autowired
    QuizRepository repository;

    @Autowired
    UserRepository userRepository;

    public boolean saveToDb(String output, String id) throws JsonProcessingException {

        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonOutput jsonOut = obj.readValue(output, JsonOutput.class);

        int num = 0;
        try {
            num = Integer.parseInt(jsonOut.getResponseCode());
            if (num == 0) {
                userRepository.save(new UserDetails(id, LocalDateTime.now()));
                UserDetails attemptDetails = fetchUniqueId(id);
                ArrayList<ResultOutput> results = jsonOut.getResults();
                int i = 0;
                for (ResultOutput ro : results) {
                    i++;
                    repository.save(new QuizInfo(attemptDetails.getId(), i, ro.getQuestion(), ro.getCorrectAnswer()));
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
            //System.out.print(quiz+" ");
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
