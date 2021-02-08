package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProfileDetails{
    private String loginId;
    private LocalDateTime dateLastAttempt;
    private Integer numberOfAttempt;
    private boolean noLastAttempt;

    public ProfileDetails(LocalDateTime dateLastAttempt, boolean noLastAttempt) {
        this.dateLastAttempt = dateLastAttempt;
        this.noLastAttempt = noLastAttempt;
    }

    public ProfileDetails(String loginId) {
        this.loginId = loginId;
    }

    public ProfileDetails(Integer numberOfAttempt) {
        this.numberOfAttempt = numberOfAttempt;
    }
}
