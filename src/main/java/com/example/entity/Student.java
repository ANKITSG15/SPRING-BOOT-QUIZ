package com.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Student {

    @Id
    private String studentId;
    private String password;

    protected Student() {
    }
}
