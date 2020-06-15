package com.accounting.service;

import com.accounting.model.FormData;

import javax.servlet.http.HttpServletResponse;

public interface ConversionService {
    void convert(FormData formData, HttpServletResponse response);
}
