package com.example;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index()
    {
        return "Hi I am Ankit";
    }

    @RequestMapping("/hello/ques/{amount}/cat/{category}/diff/{difficulty}")
    public String fetchQues(@PathVariable int amount, @PathVariable int category,@PathVariable String difficulty)
    {
        GlobalUtility gu = new GlobalUtility(); // Autowire needed
        QuizDtls qdtls = new QuizDtls(amount,category,difficulty);
        String response = gu.hitOpenAPI(qdtls);
        System.out.println(response);
        return response;
    }
}
