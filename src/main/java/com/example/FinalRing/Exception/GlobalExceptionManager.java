package com.example.FinalRing.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionManager {
    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<String> exception(PlayerNotFoundException e){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> Exception(InvalidCredentialsException e){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(JwtInvalid.class)
    public ResponseEntity<String> JwtException(JwtInvalid e){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationException(MethodArgumentNotValidException e) {

        BindingResult result=e.getBindingResult();
        List<FieldError> errors=result.getFieldErrors();
        Map<String,String>errorMap=new HashMap<>();
        for(FieldError error : errors){
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorMap);
    }
}
