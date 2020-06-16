package com.accounting.service;

import com.accounting.model.Columns;
import com.accounting.model.ConversionType;
import com.accounting.model.F1115Config;
import com.accounting.model.FormData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.SPACE;
import static com.accounting.config.Utils.XML_CONTENT_TYPE;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
public class F1115ConversionService extends AbstractConversionService implements ConversionService {

    private static final Logger logger = LogManager.getLogger(F1115ConversionService.class);

    private final ConfigsProviderService configsProviderService;
    private F1115Config f1115Config;

    public F1115ConversionService(MailService mailService, ConfigsProviderService configsProviderService) {
        super(mailService);
        this.configsProviderService = configsProviderService;
        this.f1115Config = configsProviderService.getF1115Config();
    }

    @Override
    public void convert(FormData formData, HttpServletResponse response) {
        response.setContentType(XML_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=f1115.xml");

        Map<String, Map<Columns, List<Cell>>> extractedColumns = xlsReader.read(formData.getXlsFile());
        Map<Columns, List<Cell>> filtered;
        filtered = extractedColumns.entrySet().stream()
                .filter(entry -> entry.getKey().equals("Clasa 5"))
                .map(entry -> entry.getValue().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
        .findFirst().orElseGet(LinkedHashMap::new);

        List<Cell> symbolColumn = filtered.get(Columns.SIMBOL);
        List<Cell> debitorColumn = filtered.get(Columns.DEBITOR);
        List<Cell> creditorColumn = filtered.get(Columns.CREDITOR);

        List<Cell> finalSymbolColumn = symbolColumn.stream()
                .map(this::getCellTrimValue)
                .filter(cell -> f1115Config.getAccounts().contains(cell.getStringCellValue()))
                .collect(Collectors.toList());

        debitorColumn = debitorColumn.stream()
                .filter(cell -> filterCell(cell, finalSymbolColumn))
                .collect(Collectors.toList());
        creditorColumn = creditorColumn.stream()
                .filter(cell -> filterCell(cell, finalSymbolColumn))
                .collect(Collectors.toList());



    }

    private Cell getCellTrimValue(Cell cell) {
        cell.setCellValue(cell.getStringCellValue().replace(SPACE, EMPTY));
        return cell;
    }

    private boolean filterCell(Cell cell, List<Cell> cells) {
        for (Cell c : cells) {
            if (c.getRowIndex() == cell.getRowIndex()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConversionType getType() {
        return ConversionType.F1115;
    }

}
