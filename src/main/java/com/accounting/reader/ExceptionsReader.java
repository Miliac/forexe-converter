package com.accounting.reader;

import com.accounting.service.ParserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionsReader {
    private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private static final Logger logger = LogManager.getLogger(ExceptionsReader.class);

    private final ParserService parserService;

    @Autowired
    public ExceptionsReader(ParserService parserService) {
        this.parserService = parserService;
    }

    public Map<String, String> read(String path) {
        Map<String, String> result = new HashMap<>();
        try {
            result = parserService.toObject(resolver.getResource(path).getInputStream(), new TypeReference<>() {});
        } catch (IOException e) {
            logger.error("Could not read exceptions file {} ",e.getMessage());
        }
        return result;
    }
}
