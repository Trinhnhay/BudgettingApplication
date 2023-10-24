package com.example.demo.Exception;

public class SuccessfulRequest extends RuntimeException{

    public SuccessfulRequest (String message) {
        super(message);
    }
}
