package com.accounting.reader;

import com.accounting.model.ClassSymbols;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClassSymbolsReader {

    private static final Logger logger = LogManager.getLogger(ClassSymbolsReader.class);
    private static final String RESOURCE_PATH = "src/main/resources/account-symbols.json";

    private ObjectMapper objectMapper;

    public ClassSymbolsReader() {
        objectMapper = new ObjectMapper();
    }

    public Map<String, ClassSymbols> read() {
        Map<String, ClassSymbols> result = new HashMap<>();
        try {
            result = objectMapper.readValue(new File(RESOURCE_PATH), new TypeReference<Map<String, ClassSymbols>>() {
            });
            logger.info("Account symbols file loaded in memory!");
        } catch (IOException e) {
            logger.error("Could not read account symbols file: {} ", e.getMessage());
        }
        return result;
    }
}
