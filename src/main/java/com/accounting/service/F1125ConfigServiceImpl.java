package com.accounting.service;

import com.accounting.model.F1125Row;
import com.accounting.reader.F1125ConfigReader;
import com.accounting.writer.F1125ConfigWriter;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class F1125ConfigServiceImpl implements F1125ConfigService{

    private static final Logger logger = LogManager.getLogger(F1125ConfigServiceImpl.class);
    private static final String RESOURCE_PATH = "f1125-config.json";

    private final F1125ConfigReader reader;
    private final F1125ConfigWriter writer;
    private final ParserService parserService;

    @Autowired
    public F1125ConfigServiceImpl(F1125ConfigReader reader, F1125ConfigWriter writer, ParserService parserService) {
        this.reader = reader;
        this.writer = writer;
        this.parserService = parserService;
    }

    @Override
    public List<F1125Row> read() {
        return reader.read(RESOURCE_PATH);
    }

    @Override
    public InputStream readFile() {
        List<F1125Row> config = reader.read(RESOURCE_PATH);
        byte[] configContent = new byte[0];
        try {
            configContent = parserService.toBytes(config);
        } catch (IOException e) {
            logger.error("Could not parse content into f1125 config: {} ", e.getMessage());
        }
        return new ByteArrayInputStream(configContent);
    }

    @Override
    public boolean write(InputStream inputStream) {
        List<F1125Row> config = null;
        try {
            config = parserService.toObject(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            logger.error("Could not parse content into f1125 config: {} ", e.getMessage());
        }
        return writer.write(config, RESOURCE_PATH);
    }
}
