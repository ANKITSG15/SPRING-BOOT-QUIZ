package com.example.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest
@AutoConfigureMockMvc
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(HelloController.class)
public class HelloControllerTest {
    private static final String responseCodeZero = "{\"response_code\":0,\"results\":[{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many spaces are there on a standard Monopoly board?\",\"correct_answer\":\"40\",\"incorrect_answers\":[\"28\",\"55\",\"36\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many pieces are there on the board at the start of a game of chess?\",\"correct_answer\":\"32\",\"incorrect_answers\":[\"16\",\"20\",\"36\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many dots are on a single die?\",\"correct_answer\":\"21\",\"incorrect_answers\":[\"24\",\"15\",\"18\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"Which one of these is not a real game in the Dungeons &amp; Dragons series?\",\"correct_answer\":\"Extreme Dungeons &amp; Dragons\",\"incorrect_answers\":[\"Advanced Dungeons &amp; Dragons\",\"Dungeons &amp; Dragons 3.5th edition\",\"Advanced Dungeons &amp; Dragons 2nd edition\"]}]}";

    private static final String correctResponse = "{\"response_code\":0,\"results\":[{\"category\":\"General Knowledge\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"Which of the General Mills Corporation&#039;s monster cereals was the last to be released in the 1970&#039;s?\",\"correct_answer\":\"Fruit Brute\",\"incorrect_answers\":[\"Count Chocula\",\"Franken Berry\",\"Boo-Berry\"]},{\"category\":\"General Knowledge\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"What was Mountain Dew&#039;s original slogan?\",\"correct_answer\":\"Yahoo! Mountain Dew... It&#039;ll tickle your innards!\",\"incorrect_answers\":[\"Give Me A Dew\",\"Do The Dew\",\"Get&#039; that barefoot feelin&#039; drinkin&#039; Mountain Dew\"]}]}";

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    HelloController helloController;


    @Test
    public void hello() throws Exception {
        this.mockMvc.perform(get("/fetchQues?id=MT2019011&amount=2&category=9&difficulty=medium&type=multiple")).andExpect(content().string(containsString("Hello Aditya")));
    }

//    @Test
//    void fetchQues() throws Exception {
//        when(globalUtility.isValidAmount(5).isFlag()).thenReturn(true);
//        this.mockMvc.perform(get("/fetchQues?id=MT2019011&amount=2&category=9&difficulty=medium&type=multiple"))
//                .andExpect(status().isOk());
////            .andExpect(content().string(containsString(correctResponse)));
////        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
////                "/fetchQues?id=MT2019011&amount=5&category=9&difficulty=medium&type=multiple").accept(
////                MediaType.APPLICATION_JSON);
//    }

    @Test
    void attemptQuiz() throws Exception {
        String ans = "{\"stdId\":\"MT2019011\",\"markedAnswers\":\"}";
    }
//
//    @Test
//    void fetchByQues() {
//    }
//
    @Test
    void userLogin() {

    }
}