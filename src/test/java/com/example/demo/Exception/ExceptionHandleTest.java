package com.example.demo.Exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ExceptionHandleTest {

    @Test
    void testSuccessfulRequest() {
        ExceptionHandle exceptionHandle = new ExceptionHandle();
        ResponseEntity<Object> actualSuccessfulRequestResult = exceptionHandle
                .SuccessfulRequest(new SuccessfulRequest("An error occurred"));
        assertEquals("An error occurred", ((Exception) actualSuccessfulRequestResult.getBody()).getMessage());
        assertEquals(200, actualSuccessfulRequestResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, ((Exception) actualSuccessfulRequestResult.getBody()).getHttpStatus());
        assertTrue(actualSuccessfulRequestResult.hasBody());
        assertTrue(actualSuccessfulRequestResult.getHeaders().isEmpty());
    }
}
