package com.accounting.reader;

import com.accounting.model.F1115Config;
import com.accounting.service.ParserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class F1115ConfigReader {

    private static final Logger logger = LogManager.getLogger(F1115ConfigReader.class);

    private final ParserService parserService;

    @Autowired
    public F1115ConfigReader(ParserService parserService) {
        this.parserService = parserService;
    }

    public F1115Config read(String path) {
        F1115Config result = new F1115Config();
        try {
            result = parserService.toObject(new File(path), new TypeReference<>() {});
        } catch (IOException e) {
            logger.error("Could not read exceptions file {} ",e.getMessage());
        }
        return result;
    }
}
