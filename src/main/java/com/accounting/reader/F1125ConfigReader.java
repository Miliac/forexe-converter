package com.accounting.reader;

import com.accounting.model.F1125Row;
import com.accounting.service.ParserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class F1125ConfigReader {

    private static final Logger logger = LogManager.getLogger(F1125ConfigReader.class);

    private final ParserService parserService;

    @Autowired
    public F1125ConfigReader(ParserService parserService) {
        this.parserService = parserService;
    }

    public List<F1125Row> read(String path) {
        List<F1125Row>  result = new ArrayList<>();
        try {
            result = parserService.toObject(new File(path), new TypeReference<>() {});
        } catch (IOException e) {
            logger.error("Could not read exceptions file {} ",e.getMessage());
        }
        return result;
    }
}
