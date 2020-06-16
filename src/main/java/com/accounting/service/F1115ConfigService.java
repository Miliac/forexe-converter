package com.accounting.service;

import com.accounting.model.F1115Config;

import java.io.InputStream;

public interface F1115ConfigService {

    F1115Config read();

    InputStream readFile();

    boolean write(InputStream inputStream);
}
