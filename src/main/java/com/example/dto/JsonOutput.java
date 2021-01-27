package com.example.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class JsonOutput {

    private String responseCode;
    private ArrayList<ResultOutput> results;
}
