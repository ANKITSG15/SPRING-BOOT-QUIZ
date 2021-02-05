package com.example.service;

import com.example.util.APIResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;


public class DataAccessServiceTest {

    private static final String responseCodeZero = "{\"response_code\":0,\"results\":[{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many spaces are there on a standard Monopoly board?\",\"correct_answer\":\"40\",\"incorrect_answers\":[\"28\",\"55\",\"36\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many pieces are there on the board at the start of a game of chess?\",\"correct_answer\":\"32\",\"incorrect_answers\":[\"16\",\"20\",\"36\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many dots are on a single die?\",\"correct_answer\":\"21\",\"incorrect_answers\":[\"24\",\"15\",\"18\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"Which one of these is not a real game in the Dungeons &amp; Dragons series?\",\"correct_answer\":\"Extreme Dungeons &amp; Dragons\",\"incorrect_answers\":[\"Advanced Dungeons &amp; Dragons\",\"Dungeons &amp; Dragons 3.5th edition\",\"Advanced Dungeons &amp; Dragons 2nd edition\"]}]}";

    private static final String responseCodeOne = "{\"response_code\":1,\"results\":[]}";

    private static final String responseCodeTwo = "{\"response_code\":2,\"results\":[]}";

    private static final String responseCodeThree = "{\"response_code\":3,\"results\":[]}";

    private static final String responseCodeFour = "{\"response_code\":4,\"results\":[]}";


    DataAccessService dataAccessService;


    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeZeroTest() {

        dataAccessService = new DataAccessService();
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeZero);
        Assert.assertNotNull(t);
        Assert.assertEquals("[{category=Entertainment: Board Games, type=multiple, difficulty=easy, question=How many spaces are there on a standard Monopoly board?, correct_answer=40, incorrect_answers=[28, 55, 36]}, {category=Entertainment: Board Games, type=multiple, difficulty=easy, question=How many pieces are there on the board at the start of a game of chess?, correct_answer=32, incorrect_answers=[16, 20, 36]}, {category=Entertainment: Board Games, type=multiple, difficulty=easy, question=How many dots are on a single die?, correct_answer=21, incorrect_answers=[24, 15, 18]}, {category=Entertainment: Board Games, type=multiple, difficulty=easy, question=Which one of these is not a real game in the Dungeons &amp; Dragons series?, correct_answer=Extreme Dungeons &amp; Dragons, incorrect_answers=[Advanced Dungeons &amp; Dragons, Dungeons &amp; Dragons 3.5th edition, Advanced Dungeons &amp; Dragons 2nd edition]}]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(0, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeOneTest() {

        dataAccessService = new DataAccessService();
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeOne);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(1, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeTwoTest() {

        dataAccessService = new DataAccessService();
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeTwo);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(2, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeThreeTest() {

        dataAccessService = new DataAccessService();
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeThree);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(3, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeFourTest() {

        dataAccessService = new DataAccessService();
        ObjectMapper obj = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeFour);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(4, t.getResponse_code());
    }

}
