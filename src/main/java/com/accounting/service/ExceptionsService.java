package com.accounting.service;

import java.io.InputStream;
import java.util.Map;

public interface ExceptionsService {

    Map<String, String> read();

    InputStream readFile();

    boolean write(InputStream inputStream);
}
