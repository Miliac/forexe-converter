package com.accounting.service;

import com.accounting.model.ConversionType;
import com.accounting.model.FormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("conversion")
public class ConversionServiceImpl implements ConversionService {

    private List<ConversionService> conversionServices;

    @Autowired
    ConversionServiceImpl(List<ConversionService> conversionServices) {
        this.conversionServices = conversionServices;
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response) {
        Optional<ConversionService> optionalConversionService = conversionServices.stream()
                .filter(conversionService -> conversionService.getType().equals(formData.getConversionType())).findFirst();
        optionalConversionService.ifPresent(conversionService -> conversionService.convert(formData, response));
    }

    @Override
    public ConversionType getType() {
        return null;
    }
}
