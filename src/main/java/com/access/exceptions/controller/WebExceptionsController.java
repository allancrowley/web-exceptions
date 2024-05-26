package com.access.exceptions.controller;

import java.util.stream.Collectors;
import com.access.exceptions.NotFoundException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class WebExceptionsController {
    public static String TYPE_MISMATCH_MESSAGE = "URL parameter has type mismatch";
    public static String MISSING_PARAMETER_MESSAGE = ": URL parameter is missing";
    public static String JSON_TYPE_MISMATCH_MESSAGE = "JSON contains field with type mismatch";
    public static String RESOURCE_NOT_FOUND_MESSAGE = "The requested resource was not found";

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

    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<String> missingServletRequestParameter(MissingServletRequestParameterException e) {
        return returnResponse(e.getParameterName() + MISSING_PARAMETER_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<String> noResourceFound() {
        return returnResponse(RESOURCE_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<String> jsonFieldTypeMismatchException() {
        return returnResponse(JSON_TYPE_MISMATCH_MESSAGE, HttpStatus.BAD_REQUEST);
    }
}
