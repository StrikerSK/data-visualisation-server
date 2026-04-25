package com.charts.files.utils;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;

@Component
public class CsvFileHandler {

    public <T> List<T> readEntries(InputStream inputStream, Class<T> clazz) throws Exception {
        return CsvProcessor.readEntries(inputStream, clazz);
    }

    public <T> void writeEntries(Writer writer, List<T> entries) {
        CsvProcessor.writeEntries(writer, entries);
    }
}
