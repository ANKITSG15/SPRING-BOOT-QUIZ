package com.example.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class JsonOutput {

    private String response_code;
    private ArrayList<ResultOutput> results;
}
