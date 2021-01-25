package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.util.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class HelloController {

    ArrayList<String> correctAnswers = new ArrayList<>();

    @Autowired
    GlobalUtility globalUtility;

    @Autowired
    DataAccessService dataAccessService;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/hello")
    public String index()
    {
        log.info("Up and Running");
        return "Up and Running";
    }

    @RequestMapping("/hello/ques/{amount}/cat/{category}/diff/{difficulty}/type/{type}")
    public String fetchQues(@PathVariable int amount, @PathVariable int category,@PathVariable String difficulty,@PathVariable String type) throws JsonProcessingException {

        log.info("fetchQues API called");

        if (globalUtility.isValidAmount(amount).isFlag() && globalUtility.isValidCat(category).isFlag() &&
            globalUtility.isValidDiffLevel(difficulty).isFlag() && globalUtility.isValidType(type).isFlag())
        {
            log.info("fetchQues API : fields are validated successfully");
            QuizDtls qdtls = new QuizDtls(amount, category, difficulty, type);

            String response = globalUtility.hitOpenAPI(qdtls,4);

            dataAccessService.saveToDb(response);

            log.info("fetchQues API : fields are validated successfully");

            return response;
        }
        else
        {
            log.info("invalid url");
            return "invalid url";
        }
    }


    @PostMapping(value = "/attemptQuiz")
    public int attemptQuiz(@RequestBody ArrayList<String> markedAnswers)
    {

        correctAnswers = dataAccessService.getCorrectAns(1091438L);
        if(markedAnswers.size() != correctAnswers.size())
            return -1;
        int score = 0;

        for (int i = 0; i < markedAnswers.size(); i++) {
            if(markedAnswers.get(i).equals(correctAnswers.get(i)))
                score++;
        }
        return score;
    }

    @RequestMapping("/hello/ques/{amount}")
    public String fetchByQues(@PathVariable int amount) throws JsonProcessingException {
        log.info("fetchByQues API called");

        if(globalUtility.isValidAmount(amount).isFlag())
        {
            log.info("fetchByQues API : field validation successful");
            QuizDtls qdtls = new QuizDtls(amount);

            String response = globalUtility.hitOpenAPI(qdtls,4);

            System.out.println(response);

            dataAccessService.saveToDb(response);

            log.info("fetchByQues API : success");
            return response;

        }

        log.info("Up and Running");
        return "invalid URL";
    }

}
