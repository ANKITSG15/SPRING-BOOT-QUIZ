package com.example;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Student {

    @Id
    private String student_id;
    private String password;

    protected Student(){}
}
