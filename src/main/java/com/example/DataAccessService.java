package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.util.JsonOutput;
import com.util.ResultOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service("dataAccessService")
public class DataAccessService {

    @Autowired
    QuizRepository repository;

    public boolean saveToDb(String output) throws JsonProcessingException {

        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonOutput jsonOut = obj.readValue(output,JsonOutput.class);

        if(Integer.parseInt(jsonOut.getResponse_code())==0)
        {
            int totalQues = jsonOut.getResults().size();

            ArrayList<ResultOutput> results = jsonOut.getResults();
            int i=0;
            for(ResultOutput ro : results)
            {
                i++;
                System.out.println(ro.getCorrect_answer());
                repository.save(new QuizInfo(1091438,i,ro.getQuestion(),ro.getCorrect_answer(),ro.getType()));
            }

        }else{

            System.out.println("No Questions Fetched");
        }

        Optional<QuizInfo> storedResults = repository.findById(Long.valueOf(1091438)) ;



        return  true;
    }
}
