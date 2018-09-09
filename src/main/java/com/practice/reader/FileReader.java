package com.practice.reader;

import com.practice.entity.HeaderMap;
import lombok.Data;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by psingh15 on 5/17/17.
 */
@Slf4j
@Data
public class FileReader implements ItemReader<String> {
    static AtomicInteger index = new AtomicInteger(0);

    BufferedReader br;

    @Autowired
    HeaderMap headerMap;

    Map<String, Integer> positionMap = new HashMap<>();

    final String filePath = "/Users/psingh15/DelphiCode/PracticeFiles/user.csv";

    private static AtomicInteger maxcount = new AtomicInteger(0);

    @Synchronized
    public String read() throws Exception {
        StringBuilder inputFile = new StringBuilder();
        if (maxcount.get() == 1) {
            br.close();
            br = null;
            return null;
        }
        String line = null;

        if (index.get() == 0) {
            inputFile.append(filePath);
            try {
                br = new BufferedReader(new java.io.FileReader(inputFile.toString()));
                if (br != null) {
                    line = br.readLine();

                    String[] header = line.split(",");
                    for (int i = 0; i < header.length; i++) {
                        positionMap.put(header[i].trim(), i);
                    }

                    headerMap().setPositionMap(positionMap);
                    if (line != null && line.contains("id")) {
                        line = br.readLine();
                    }

                }
                positionMap = headerMap.getPositionMap();
                index.getAndIncrement();
                if (line != null) {
                    return line;
                } else {
                    return null;
                }
            } catch (IOException ex) {
                log.error(
                        "Error reading the CSV file:Exception Cause - [{}]. Exception stack trace"
                                + " is- [{}]",
                        ex.getCause(), ex);
            }
        } else {
            try {
                index.getAndIncrement();
                if (br == null) {
                    return null;
                }

                log.info("count:{[]}--", index.toString());
                while ((line = br.readLine()) != null) {
                    if (!line.isEmpty()) {
                        return line;
                    }
                }

            } catch (FileNotFoundException ex) {
                log.error(
                        "File Not Found - [{}]. Exception stack trace is- [{}]",
                        ex.getCause(), ex);

            } catch (IOException ex) {
                log.error(
                        "Error reading the CSV file:Exception Cause - [{}]. Exception stack trace"
                                + " is- [{}]",
                        ex.getCause(), ex);
            }
        }
        if (line != null && !line.isEmpty()) {
            maxcount.getAndIncrement();
            return line;
        }
        return null;
    }
    @Bean
    public HeaderMap headerMap() {
        return headerMap;
    }
}
