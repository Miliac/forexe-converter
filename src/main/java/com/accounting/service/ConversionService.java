package com.accounting.service;

import com.accounting.model.F1102Type;
import com.accounting.model.F1102TypeDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ConversionService {
    void convert(MultipartFile multipartFile, F1102Type f1102Type);

    F1102Type getFromDTO(F1102TypeDTO f1102TypeDTO);
}
