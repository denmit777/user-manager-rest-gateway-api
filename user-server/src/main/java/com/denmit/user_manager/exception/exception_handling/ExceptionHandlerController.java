package com.denmit.user_manager.exception.exception_handling;

import com.denmit.user_manager.exception.UserIsPresentException;
import com.denmit.user_manager.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            WebRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        List<Map.Entry<String, String>> listErrors = new ArrayList<>(errors.entrySet());

        return ResponseEntity.status(status).body(listErrors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionInfo handleException(UserNotFoundException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return info;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionInfo handleException(UserIsPresentException e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return info;
    }
}
