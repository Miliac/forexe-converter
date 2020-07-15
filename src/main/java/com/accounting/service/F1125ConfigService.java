package com.accounting.service;

import com.accounting.model.F1125Row;

import java.io.InputStream;
import java.util.List;

public interface F1125ConfigService {

    List<F1125Row> read();

    InputStream readFile();

    boolean write(InputStream inputStream);
}
