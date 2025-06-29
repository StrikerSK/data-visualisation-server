package com.charts.files.exception;

public class CsvContentException extends RuntimeException {

    public CsvContentException(String message) {
        super(message);
    }

    public CsvContentException(String message, Throwable cause) {
        super(message, cause);
    }

}
