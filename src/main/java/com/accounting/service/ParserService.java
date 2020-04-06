package com.accounting.service;

import com.accounting.model.AccountSymbols;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class ParserService {

    private ObjectMapper mapper = new ObjectMapper();

    <T> T toObject(InputStream input, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(input, typeReference);
    }

    public <T> T toObject(File file, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(file, typeReference);
    }

    public byte[] toBytes(Object object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mapper.writerWithDefaultPrettyPrinter().writeValue(bos,object);
        return bos.toByteArray();
    }

}
