package com.example;

import com.example.repository.QuizRepository;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dto.JsonOutput;
import com.example.dto.ResultOutput;
import org.apache.catalina.User;
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

    public boolean saveToDb(String output,String id) throws JsonProcessingException
    {

        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonOutput jsonOut = obj.readValue(output,JsonOutput.class);

        if(Integer.parseInt(jsonOut.getResponse_code())==0)
        {
            userRepository.save(new UserDetails(id,LocalDateTime.now()));
            UserDetails attemptDtls = fetchuniqueId(id);
            ArrayList<ResultOutput> results = jsonOut.getResults();
            int i=0;
            for(ResultOutput ro : results)
            {
                i++;
                repository.save(new QuizInfo(attemptDtls.getId(),i,ro.getQuestion(),ro.getCorrect_answer()));
            }

        }else{

            return false;
        }

        return  true;
    }

    public ArrayList<String> getCorrectAns(Integer uniqId)
    {
        List<QuizInfo> listInfo = StreamSupport.stream(repository.findAll().spliterator(),false).
                filter((QuizInfo qi)->qi.getUniqId()==uniqId).collect(Collectors.toList());

        ArrayList<String> correctAns = new ArrayList<String>();

        for(QuizInfo quiz : listInfo)
        {
            //System.out.print(quiz+" ");
            correctAns.add(quiz.getCorectAns());
        }
        return correctAns;
    }

    public UserDetails fetchuniqueId(String id)
    {
         List<UserDetails> ud = StreamSupport.stream(userRepository.findAll().spliterator(),false).
                filter((UserDetails qi)->qi.getUserId().equalsIgnoreCase(id)).collect(Collectors.toList());

        UserDetails recentDetails = ud.stream().max(Comparator.comparing(UserDetails::getUserId)).get();

        System.out.println(recentDetails.getDateofAttempt()+" "+recentDetails.getUserId()+" "+recentDetails.getId());

        return recentDetails;
    }
}
