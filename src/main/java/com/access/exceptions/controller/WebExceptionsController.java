package com.access.exceptions.controller;

import java.util.stream.Collectors;
import com.access.exceptions.NotFoundException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class WebExceptionsController {
    public static String TYPE_MISMATCH_MESSAGE = "URL parameter has type mismatch";
    public static String JSON_TYPE_MISMATCH_MESSAGE = "JSON contains field with type mismatch";

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> notFoundHandler(NotFoundException e) {
        return returnResponse(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<String> returnResponse(String message, HttpStatus status) {
        log.error(message);
        return new ResponseEntity<>(message, status);
    }

    @ExceptionHandler({ IllegalStateException.class, IllegalArgumentException.class })
    ResponseEntity<String> badRequestHandler(RuntimeException e) {
        return returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<String> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {
        String message = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));
        return returnResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ResponseEntity<String> methodValidationHandler(HandlerMethodValidationException e) {
        String message = e.getAllErrors().stream().map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(";"));

        return returnResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<String> methodArgumentTypeMismatch() {
        return returnResponse(TYPE_MISMATCH_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<String> jsonFieldTypeMismatchException() {
        return returnResponse(JSON_TYPE_MISMATCH_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}