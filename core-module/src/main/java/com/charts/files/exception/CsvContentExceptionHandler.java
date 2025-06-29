package com.charts.files.exception;

import com.charts.general.exception.ExceptionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@Order(1)
@ControllerAdvice
public class CsvContentExceptionHandler {

    @ExceptionHandler(value = CsvContentException.class)
    public ResponseEntity<Map<String, String>> handleException(CsvContentException ex) {
        return ExceptionHandlers.createResponse(HttpStatus.BAD_REQUEST.value(), "CSV content error", ex);
    }

}
