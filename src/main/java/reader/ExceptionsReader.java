package reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ClassSymbols;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExceptionsReader {

    private static final String RESOURCE_PATH = "src/main/resources/exceptions.json";

    private ObjectMapper objectMapper;

    public ExceptionsReader() {
        objectMapper = new ObjectMapper();
    }

    public Map<String, String> read() {
        Map<String, String> result = new HashMap<>();
        try {
            result = objectMapper.readValue(new File(RESOURCE_PATH), new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            System.out.println("Could not read exceptions " + e.getMessage());
        }
        return result;
    }
}
