package com.example.service;

import com.example.entity.QuizInfo;
import com.example.entity.UserDetails;
import com.example.repository.QuizRepository;
import com.example.repository.UserRepository;
import com.example.util.APIResponse;
import com.example.util.ConfigUtility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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
}
