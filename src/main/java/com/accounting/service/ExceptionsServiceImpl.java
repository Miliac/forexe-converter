package com.accounting.service;

import com.accounting.reader.ExceptionsReader;
import com.accounting.writer.ExceptionsWriter;
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
public class ExceptionsServiceImpl implements ExceptionsService {

    private static final Logger logger = LogManager.getLogger(ExceptionsServiceImpl.class);
    private static final String RESOURCE_PATH = "src/main/resources/exceptions.json";

    private final ExceptionsReader reader;
    private final ExceptionsWriter writer;
    private final ParserService parserService;

    @Autowired
    public ExceptionsServiceImpl(ExceptionsReader reader, ExceptionsWriter writer, ParserService parserService) {
        this.reader = reader;
        this.writer = writer;
        this.parserService = parserService;
    }

    @Override
    public Map<String, String> read() {
        return reader.read(RESOURCE_PATH);
    }

    @Override
    public InputStream readFile() {
        Map<String, String> exceptions = reader.read(RESOURCE_PATH);
        byte[] exceptionsContent = new byte[0];
        try {
            exceptionsContent = parserService.toBytes(exceptions);
        } catch (IOException e) {
            logger.error("Could not parse content into exceptions: {} ", e.getMessage());
        }
        return new ByteArrayInputStream(exceptionsContent);
    }

    @Override
    public boolean write(InputStream inputStream) {
        Map<String, String> exceptions = null;
        try {
            exceptions = parserService.toObject(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            logger.error("Could not parse content into exceptions: {} ", e.getMessage());
        }
        return writer.write(exceptions, RESOURCE_PATH);
    }
}
