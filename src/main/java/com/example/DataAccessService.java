package com.example;

import com.example.repository.QuizRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dto.JsonOutput;
import com.example.dto.ResultOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service("dataAccessService")
public class DataAccessService {

    @Autowired
    QuizRepository repository;

    public boolean saveToDb(String output) throws JsonProcessingException
    {

        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonOutput jsonOut = obj.readValue(output,JsonOutput.class);

        if(Integer.parseInt(jsonOut.getResponse_code())==0)
        {
            ArrayList<ResultOutput> results = jsonOut.getResults();
            int i=0;
            for(ResultOutput ro : results)
            {
                i++;
                //System.out.println(ro.getCorrect_answer());

                repository.save(new QuizInfo(1091438L,i,ro.getQuestion(),ro.getCorrect_answer()));
            }

        }else{

            System.out.println("No Questions Fetched");
        }

        return  true;
    }

    public ArrayList<String> getCorrectAns(long uniqId)
    {
        List<QuizInfo> listInfo = StreamSupport.stream(repository.findAll().spliterator(),false).
                filter((QuizInfo qi)->qi.getUniqId()==1091438L).collect(Collectors.toList());

        ArrayList<String> correctAns = new ArrayList<String>();

        for(QuizInfo quiz : listInfo)
        {
            correctAns.add(quiz.getCorectAns());
        }

        return correctAns;
    }
}
