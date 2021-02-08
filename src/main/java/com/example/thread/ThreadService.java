package com.example.thread;

import com.example.dto.ProfileDetails;
import com.example.entity.UserDetails;
import com.example.repository.QuizRepository;
import com.example.repository.UserRepository;
import com.example.service.DataAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ThreadService {
    private DataAccessService dataAccessService;
    private static final Logger log = LoggerFactory.getLogger(ThreadService.class);
    @Autowired
    private UserRepository userRepository;

    @Async
    public CompletableFuture<ProfileDetails> getLastAttempt(String id) throws Exception {
        log.info("Looking up" + id);
        UserDetails recentDetails = null;

        List<UserDetails> ud = StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter((UserDetails qi) -> qi.getUserId().equalsIgnoreCase(id)).collect(Collectors.toList());
        if (ud.size() != 0)
            recentDetails = ud.stream().max(Comparator.comparing(UserDetails::getId)).get();
        Thread.sleep(1000L);
        //log.info("Date - " + String.valueOf(recentDetails.getDateOfAttempt()));
        if (recentDetails != null)
            return CompletableFuture.completedFuture(new ProfileDetails(recentDetails.getDateOfAttempt(),false));
        else{
            return CompletableFuture.completedFuture(new ProfileDetails(LocalDateTime.now(),true));
        }

    }

    @Async
    public CompletableFuture<ProfileDetails> getNoAttempts(String id) {
        log.info("number of attempt");
        List<UserDetails> ud = StreamSupport.stream(userRepository.findAll().spliterator(), false).
                filter((UserDetails qi) -> qi.getUserId().equalsIgnoreCase(id)).collect(Collectors.toList());
        if (ud.size() == 0)
            return CompletableFuture.completedFuture(new ProfileDetails(0));
        return CompletableFuture.completedFuture(new ProfileDetails(ud.size()));
    }

}
