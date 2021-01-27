package com.example.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationMsg {

    private int code;
    private String message;
    private boolean flag;

    ValidationMsg(int code, String message, boolean flag) {
        this.code = code;
        this.message = message;
        this.flag = flag;
    }
}
