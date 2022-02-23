package com.maknaeontop.communication.controller;

import com.maknaeontop.communication.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler is a class that handles all exceptions
 * that occur in the controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Response response = new Response();

    /**
     * Method to handles all exception in the controller.
     * @param e     all type of exception
     * @return      fail status and exception message
     */
    @ExceptionHandler(Exception.class)
    public String handleLineException(Exception e) {
        return response.statusResponse("fail", e.toString());
    }
}
