package com.charts.files.utils;

import com.charts.files.exception.CsvContentException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvProcessor {

    /**
     * Method writes list of entries into CSV file
     *
     * @param writer Writer is used to write data to the response output
     * @param data List of entries to be written into CSV file
     * @param <T> Any class of entries that will be returned after parsing. These classes should have fields annotated with @CsvBindByName.
     */
    public static <T> void writeEntries(Writer writer, List<T> data) {
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            StatefulBeanToCsvBuilder<T> builder = new StatefulBeanToCsvBuilder<>(csvWriter);
            builder.build().write(data);
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new CsvContentException(e.getMessage(), e);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method read file into list of entries
     *
     * @param is InputStream for data to be read
     * @param clazz Class of entries to be resolved by function
     * @return List of entries that are processed into desired class
     * @param <T> Any class of entries that will be returned after parsing. These classes should have fields annotated with @CsvBindByName.
     * @throws IOException Exception coming from file reading
     */
    public static <T> List<T> readEntries(InputStream is, Class<T> clazz) throws IOException {
        try {
            InputStreamReader inputStream = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader fileReader = new BufferedReader(inputStream);
            String stringReader = IOUtils.toString(fileReader);
            CSVReader csvReader = new CSVReader(new StringReader(stringReader));
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (Exception e) {
            if (e.getCause() instanceof CsvRequiredFieldEmptyException) {
                Throwable cause = e.getCause();
                throw new CsvContentException(cause.getMessage(), cause);
            }

            throw e;
        }
    }

}
