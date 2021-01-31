package com.example.service;

import com.example.entity.QuizInfo;
import com.example.entity.UserDetails;
import com.example.repository.QuizRepository;
import com.example.repository.UserRepository;
import com.example.util.APIResponse;
import com.example.util.ConfigUtility;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.Mockito.when;

public class DataAccessServiceTest {

    private DataAccessService dataAccessService;

    private final ConfigUtility config = Mockito.mock(ConfigUtility.class);
    private final QuizRepository repository = Mockito.mock(QuizRepository.class);
    private final UserRepository userRepository= Mockito.mock(UserRepository.class);

    private static final String responseCodeZero = "{\"response_code\":0,\"results\":[{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many spaces are there on a standard Monopoly board?\",\"correct_answer\":\"40\",\"incorrect_answers\":[\"28\",\"55\",\"36\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many pieces are there on the board at the start of a game of chess?\",\"correct_answer\":\"32\",\"incorrect_answers\":[\"16\",\"20\",\"36\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"How many dots are on a single die?\",\"correct_answer\":\"21\",\"incorrect_answers\":[\"24\",\"15\",\"18\"]},{\"category\":\"Entertainment: Board Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"Which one of these is not a real game in the Dungeons &amp; Dragons series?\",\"correct_answer\":\"Extreme Dungeons &amp; Dragons\",\"incorrect_answers\":[\"Advanced Dungeons &amp; Dragons\",\"Dungeons &amp; Dragons 3.5th edition\",\"Advanced Dungeons &amp; Dragons 2nd edition\"]}]}";

    private static final String responseCodeOne = "{\"response_code\":1,\"results\":[]}";

    private static final String responseCodeTwo = "{\"response_code\":2,\"results\":[]}";

    private static final String responseCodeThree = "{\"response_code\":3,\"results\":[]}";

    private static final String responseCodeFour = "{\"response_code\":4,\"results\":[]}";

    private static final Integer UniqueID = 21;

    private static final String id = "2019MT011";

    private static final LocalDateTime time = LocalDateTime.now();

    @Before
    public void setup(){
        dataAccessService = new DataAccessService(config,repository,userRepository);
        System.out.println("Hello");
    }


    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeZeroTest() {
        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeZero);
        Assert.assertNotNull(t);
        Assert.assertEquals("[{category=Entertainment: Board Games, type=multiple, difficulty=easy, question=How many spaces are there on a standard Monopoly board?, correct_answer=40, incorrect_answers=[28, 55, 36]}, {category=Entertainment: Board Games, type=multiple, difficulty=easy, question=How many pieces are there on the board at the start of a game of chess?, correct_answer=32, incorrect_answers=[16, 20, 36]}, {category=Entertainment: Board Games, type=multiple, difficulty=easy, question=How many dots are on a single die?, correct_answer=21, incorrect_answers=[24, 15, 18]}, {category=Entertainment: Board Games, type=multiple, difficulty=easy, question=Which one of these is not a real game in the Dungeons &amp; Dragons series?, correct_answer=Extreme Dungeons &amp; Dragons, incorrect_answers=[Advanced Dungeons &amp; Dragons, Dungeons &amp; Dragons 3.5th edition, Advanced Dungeons &amp; Dragons 2nd edition]}]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(0, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeOneTest() {

        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeOne);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(1, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeTwoTest() {

        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeTwo);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(2, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeThreeTest() {

        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeThree);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(3, t.getResponse_code());
    }

    @SneakyThrows
    @Test
    public void fetchValFromJSONResponseCodeFourTest() {

        APIResponse t = dataAccessService.fetchValFromJSON(responseCodeFour);
        Assert.assertNotNull(t);
        Assert.assertEquals("[]", t.getResults().toString());
        Assert.assertNotNull(t.getResponse_code());
        Assert.assertEquals(4, t.getResponse_code());
    }

    @Test
    public void getCorrectAnsTest() {
        when(repository.findAll()).thenReturn(Stream.of
        (new QuizInfo(21,1,"In a standard game of Monopoly, what colour are the two cheapest properties?",
                "Brown"),new QuizInfo(21,2,"Which of these games includes the phrase &quot;Do not pass Go, do not collect $200&quot;?",
                "Monopoly"),new QuizInfo(21,3,"Which one of these is not a real game in the Dungeons &amp; Dragons series?",
                "Extreme Dungeons &amp; Dragons")).collect(Collectors.toList()));
        Assert.assertEquals(3,dataAccessService.getCorrectAns(UniqueID).size());
    }

    @Test
    public void fetchUniqueIdTest() {

        when(userRepository.findAll()).thenReturn(Stream.of
                (new UserDetails("2019MT011", time)).collect(Collectors.toList()));

        Assert.assertEquals("2019MT011",dataAccessService.fetchUniqueId("2019MT011").getUserId());

        Assert.assertEquals(String.valueOf(time),String.valueOf(dataAccessService.fetchUniqueId("2019MT011").getDateOfAttempt()));
    }

}
