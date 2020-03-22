package reader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.ClassSymbols;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClassSymbolsReader {

    private static final String RESOURCE_PATH = "src/main/resources/account-symbols.json";

    private ObjectMapper objectMapper;

    public ClassSymbolsReader() {
        objectMapper = new ObjectMapper();
    }

    public Map<String, ClassSymbols> read() {
        Map<String, ClassSymbols> result = new HashMap<>();
        try {
            result = objectMapper.readValue(new File(RESOURCE_PATH), new TypeReference<Map<String, ClassSymbols>>() {
            });
        } catch (IOException e) {
            System.out.println("Could not read account symbols " + e.getMessage());
        }
        return result;
    }
}
