package com.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationMsg {

    private int code;
    private String message;

    ValidationMsg(int code,String message)
    {
        this.code = code;
        this.message = message;
    }
}
