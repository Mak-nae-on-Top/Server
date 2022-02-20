package com.maknaeontop.communication.controller;

import com.maknaeontop.communication.Response;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Response response = new Response();

    @ExceptionHandler(Exception.class)
    public String handleLineException(Exception e) {
        return response.statusResponse("success", e.toString());
    }
}
