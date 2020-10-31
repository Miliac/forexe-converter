package com.accounting.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ParserService {

    private final ObjectMapper mapper = new ObjectMapper();

    <T> T toObject(InputStream input, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(input, typeReference);
    }

    public <T> T toObject(File file, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(file, typeReference);
    }

    public byte[] toBytes(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(bos, object);
        return bos.toByteArray();
    }
}
