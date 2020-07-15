package com.accounting.reader;

import com.accounting.model.AccountSymbols;
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
public class AccountSymbolsReader {

    private static final Logger logger = LogManager.getLogger(AccountSymbolsReader.class);

    private final ParserService parserService;

    @Autowired
    public AccountSymbolsReader(ParserService parserService) {
        this.parserService = parserService;
    }

    public Map<String, AccountSymbols> read(String path) {
        Map<String, AccountSymbols> result = new HashMap<>();
        try {
            result = parserService.toObject(new File(path), new TypeReference<>() {});
        } catch (IOException e) {
            logger.error("Could not read account symbols file: {} ", e.getMessage());
        }
        return result;
    }
}
