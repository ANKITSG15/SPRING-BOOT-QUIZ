package com.example.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse<T> {
    private T response_code;
    private T results;
    private boolean isError;
    private String errorMessage;
    private String successMessage;
    private String otherError;

}
