package com.example;

import com.example.dto.InpCorrectAns;
import com.example.dto.QuizDtls;
import com.example.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.example.util.GlobalUtility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class HelloController {

    ArrayList<String> correctAnswers = new ArrayList<>();

    @Autowired
    StudentRepository studentRepository;

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

    @RequestMapping("/hello/{id}/ques/{amount}/cat/{category}/diff/{difficulty}/type/{type}")
    public String fetchQues(@PathVariable String id, @PathVariable int amount, @PathVariable int category,@PathVariable String difficulty,@PathVariable String type) throws JsonProcessingException {

        log.info("fetchQues API called");

        if (globalUtility.isValidAmount(amount).isFlag() && globalUtility.isValidCat(category).isFlag() &&
            globalUtility.isValidDiffLevel(difficulty).isFlag() && globalUtility.isValidType(type).isFlag())
        {
            log.info("fetchQues API : fields are validated successfully");
            QuizDtls qdtls = new QuizDtls(amount, category, difficulty, type);

            String response = globalUtility.hitOpenAPI(qdtls,4);

             if(dataAccessService.saveToDb(response,id)){
                 log.info("fetchQues API : Questions are saved successfully");
                 return response;
             }
             else
             {
                 log.info("invalid url");
                 return "Invalid response code : Check the url and pass the valid inputs";
             }
        }
        else
        {
            log.info("invalid url");
            return "Check the url and pass the valid inputs";
        }
    }


    @PostMapping(value = "/attemptQuiz",consumes = MediaType.APPLICATION_JSON_VALUE)
    public int attemptQuiz(@RequestBody String ans) throws JsonProcessingException {

        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InpCorrectAns jsonOut = obj.readValue(ans,InpCorrectAns.class);

        ArrayList<String> markedAnswers = jsonOut.getMarkedAnswers();

        UserDetails usr = dataAccessService.fetchuniqueId(jsonOut.getStdId());

        correctAnswers = dataAccessService.getCorrectAns(usr.getId());

        if(markedAnswers.size() != correctAnswers.size())
            return -1;

        int score = 0;

        for (int i = 0; i < markedAnswers.size(); i++) {
            if(markedAnswers.get(i).equals(correctAnswers.get(i)))
                score++;
        }
        return score;
    }

    @RequestMapping("/hello/{id}/ques/{amount}")
    public String fetchByQues(@PathVariable String id, @PathVariable int amount) throws JsonProcessingException {
        log.info("fetchByQues API called");

        if(globalUtility.isValidAmount(amount).isFlag())
        {
            log.info("fetchByQues API : field validation successful");
            QuizDtls qdtls = new QuizDtls(amount);

            String response = globalUtility.hitOpenAPI(qdtls,1);

            if(dataAccessService.saveToDb(response,id)){
                log.info("fetchQues API : Questions are saved successfully");
                return response;
            }
            else
            {
                log.info("invalid url");
                return "Invalid response code : Check the url and pass the valid inputs";
            }

        }

        log.info("Check the url and pass the valid inputs");
        return "invalid URL";
    }

    @PostMapping("/hello/login")
    public @ResponseBody String addNewUser(@RequestParam String name,@RequestParam String password)
    {

        List<Student> student = StreamSupport.stream(studentRepository.findAll().spliterator(),false).
                filter((Student s)->s.getStudent_id().equals(name) && s.getPassword().equals(password)).collect(Collectors.toList());

        if(student.size() == 0)
            return "INVALID USER";
        return "SUCCESS";
    }
}
