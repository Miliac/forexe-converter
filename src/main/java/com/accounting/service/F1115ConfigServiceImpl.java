package com.accounting.service;

import com.accounting.model.F1115Config;
import com.accounting.reader.F1115ConfigReader;
import com.accounting.writer.F1115ConfigWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class F1115ConfigServiceImpl implements F1115ConfigService {

    private static final Logger logger = LogManager.getLogger(ExceptionsServiceImpl.class);
    private static final String RESOURCE_PATH = "src/main/resources/f1115-config.json";

    private F1115ConfigReader reader;
    private F1115ConfigWriter writer;
    private ParserService parserService;

    @Autowired
    public F1115ConfigServiceImpl(F1115ConfigReader reader, F1115ConfigWriter writer, ParserService parserService) {
        this.reader = reader;
        this.writer = writer;
        this.parserService = parserService;
    }

    @Override
    public F1115Config read() {
        return reader.read(RESOURCE_PATH);
    }

    @Override
    public InputStream readFile() {
        F1115Config config = reader.read(RESOURCE_PATH);
        byte[] configContent = new byte[0];
        try {
            configContent = parserService.toBytes(config);
        } catch (IOException e) {
            logger.error("Could not parse content into f1115 config: {} ", e.getMessage());
        }
        return new ByteArrayInputStream(configContent);
    }

    @Override
    public boolean write(InputStream inputStream) {
        F1115Config config = null;
        try {
            config = parserService.toObject(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            logger.error("Could not parse content into f1115 config: {} ", e.getMessage());
        }
        return writer.write(config, RESOURCE_PATH);
    }
}
