package com.example.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<Object> handleRequestException(RequestException e) {
        HttpStatus badRequest =HttpStatus.BAD_REQUEST;

        //Create exception detais
        Exception exception = new Exception(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        //Return response entity
        return new ResponseEntity<>(exception, badRequest);
    }

    @ExceptionHandler(value = {SuccessfulRequest.class})
    public ResponseEntity<Object> SuccessfulRequest(SuccessfulRequest e) {
        HttpStatus successRequest =HttpStatus.OK;

        //Create exception detais
        Exception exception = new Exception(
                e.getMessage(),
                successRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        //Return response entity
        return new ResponseEntity<>(exception, successRequest);
    }
}
