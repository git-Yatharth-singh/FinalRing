package com.example.FinalRing.Exception;

import com.example.FinalRing.Response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionManager {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiError> emailExists(EmailAlreadyExistsException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ApiError> playerNotFound(PlayerNotFoundException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> InvalidCredentials(InvalidCredentialsException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(JwtInvalid.class)
    public ResponseEntity<ApiError> JwtException(JwtInvalid e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validationException(MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        List<FieldError> errors = result.getFieldErrors();

        Map<String, String> errorMap = new HashMap<>();

        for (FieldError error : errors) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }

        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(errorMap.toString());
        apiError.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiError);
    }

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ApiError> MatchException(MatchNotFoundException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(PlayerAlreadyJoined.class)
    public ResponseEntity<ApiError> playerJoined(PlayerAlreadyJoined e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MatchFullException.class)
    public ResponseEntity<ApiError> matchFull(MatchFullException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MatchStartedException.class)
    public ResponseEntity<ApiError> matchStarted(MatchStartedException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(PlayerNotInMatchException.class)
    public ResponseEntity<ApiError> playerInMatch(PlayerNotInMatchException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(NotCreatorException.class)
    public ResponseEntity<ApiError> notCreator(NotCreatorException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    @ExceptionHandler(InsufficientException.class)
    public ResponseEntity<ApiError> insufficient(InsufficientException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(MatchNotRunningException.class)
    public ResponseEntity<ApiError> notRunning(MatchNotRunningException e) {

        ApiError error = new ApiError();
        error.setStatus(HttpStatus.CONFLICT.value());
        error.setMessage(e.getMessage());
        error.setTimestamp(Instant.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}