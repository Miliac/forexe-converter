package com.accounting.service;

import com.accounting.model.F1102TypeDTO;

import javax.servlet.http.HttpServletResponse;

public interface ConversionService {
    void convert(F1102TypeDTO f1102TypeDTO, HttpServletResponse response);
}
