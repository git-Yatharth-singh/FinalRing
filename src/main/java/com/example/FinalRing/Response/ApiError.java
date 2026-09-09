package com.example.FinalRing.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public class ApiError {
    private int status;
    private String message;
    private Instant timestamp;
    private Map<String, String> errors;
}
