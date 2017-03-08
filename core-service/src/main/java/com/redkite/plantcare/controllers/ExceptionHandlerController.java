package com.redkite.plantcare.controllers;

import com.redkite.plantcare.PlantCareException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleException(HttpServletRequest req, Exception ex) {
        if (ex instanceof PlantCareException) {
            log.error(ex.getMessage(), ex);
            PlantCareException pcException = (PlantCareException) ex;
            return new ResponseEntity<String>(pcException.getMessage(), pcException.getStatus());
        } else {
            log.error(ex.getMessage(), ex);
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
