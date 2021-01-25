package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.util.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private ArrayList<String> correctAnswers = new ArrayList<>();

    @Autowired
    QuizRepository repository;

    @Autowired
    GlobalUtility globalUtility;

    @Autowired
    DataAccessService dataAccessService;

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/hello")
    public String index()
    {
        return "Hi I am Ankit";
    }

    @RequestMapping("/hello/ques/{amount}/cat/{category}/diff/{difficulty}/type/{type}")
    public String fetchQues(@PathVariable int amount, @PathVariable int category,@PathVariable String difficulty,@PathVariable String type) throws JsonProcessingException {

        ValidationMsg msg = globalUtility.isValidation(amount, category, difficulty, type);

        if (msg.getCode() == 1)
        {

            QuizDtls qdtls = new QuizDtls(amount, category, difficulty, type);

            String response = globalUtility.hitOpenAPI(qdtls);

            System.out.println(response);

        dataAccessService.saveToDb(response);

            return response;
        }
        else
        {
            return msg.getMessage();
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
}
