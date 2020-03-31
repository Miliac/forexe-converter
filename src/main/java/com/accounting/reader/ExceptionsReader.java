package com.accounting.reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExceptionsReader {

    private static final Logger logger = LogManager.getLogger(ExceptionsReader.class);
    private static final String RESOURCE_PATH = "src/main/resources/exceptions.json";

    private ObjectMapper objectMapper;

    public ExceptionsReader() {
        objectMapper = new ObjectMapper();
    }

    public Map<String, String> read() {
        Map<String, String> result = new HashMap<>();
        try {
            result = objectMapper.readValue(new File(RESOURCE_PATH), new TypeReference<Map<String, String>>() {});
            logger.info("Exceptions file loaded in memory!");
        } catch (IOException e) {
            logger.error("Could not read exceptions file {} ",e.getMessage());
        }
        return result;
    }
}
