package com.accounting.service;

import com.accounting.model.AccountSymbols;
import com.accounting.model.Attachment;
import com.accounting.model.EmailDTO;
import com.accounting.reader.XLSReader;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.accounting.config.Utils.*;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public abstract class AbstractConversionService {
    XLSReader xlsReader;
    MailService mailService;
    ConfigsProviderService configsProviderService;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    List<Cell> cellsWithCF;
    List<Cell> cellsWithCFAndCE;

    AbstractConversionService(MailService mailService, ConfigsProviderService configsProviderService) {
        this.mailService = mailService;
        this.configsProviderService = configsProviderService;
        xlsReader = new XLSReader();
        cellsWithCF = new ArrayList<>();
        cellsWithCFAndCE = new ArrayList<>();
    }

    Cell getCellTrimValue(Cell cell) {
        cell.setCellValue(cell.getStringCellValue().replace(SPACE, EMPTY));
        return cell;
    }

    boolean filterCell(Cell cell, List<Cell> cells) {
        for (Cell c : cells) {
            if (c.getRowIndex() == cell.getRowIndex()) {
                return true;
            }
        }
        return false;
    }

    String dateFormatter(String dateString) {
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(INPUT_DATE_FORMAT));
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    EmailDTO buildEmailDto(EmailDTO emailDTO, String subject, String content, List<Attachment> attachments) {
        return emailDTO.setSubject(subject).setContent(content).setAttachments(attachments);
    }

    boolean filterByClass(String className, Cell cell, Map<String, AccountSymbols> symbols, List<Cell> symbolColumns, Map<String, String> exceptions) {
        AccountSymbols accountSymbols = symbols.getOrDefault(className, new AccountSymbols());

        if (exceptions.containsKey(cell.getStringCellValue())) {
            cell.setCellValue(exceptions.get(cell.getStringCellValue()));
            return true;
        } else {

            if (cell.getStringCellValue().length() == 3) {
                return filterCellsWith3Length(cell, symbolColumns, accountSymbols);
            }

            if (cell.getStringCellValue().length() == 5 || cell.getStringCellValue().length() == 7) {
                return filterCellsWithLengthBetween5And7(cell, symbolColumns, accountSymbols);
            }

            if (cell.getStringCellValue().length() >= 14 && cell.getStringCellValue().length() < 20) {
                return filterCellsWithLengthBetween14And20(cell, accountSymbols);
            }

            if (cell.getStringCellValue().length() >= 20 && cell.getStringCellValue().length() <= 22) {
                return filterCellsWithLengthBetween20And22(cell, symbolColumns, accountSymbols);
            }
        }

        return false;
    }

     boolean filterCellsWith3Length(Cell cell, List<Cell> symbolColumns, AccountSymbols accountSymbols) {
        String symbol = cell.getStringCellValue().concat(ZERO.repeat(4));
        if (accountSymbols.getAccountSymbolsEndInFourZeros()
                .contains(symbol)) {
            cellsWithCF = symbolColumns.stream()
                    .filter(nextCell -> nextCell.getStringCellValue().startsWith(symbol) && nextCell.getStringCellValue().length() == 16)
                    .collect(Collectors.toList());
            cellsWithCF = cellsWithCF.stream()
                    .filter(nextCell -> filterDifferentSymbol(nextCell, cellsWithCF))
                    .collect(Collectors.toList());
            return cellsWithCF.size() < 2;
        } else {
            cellsWithCF.clear();
            return false;
        }
    }

     boolean filterCellsWithLengthBetween5And7(Cell cell, List<Cell> symbolColumns, AccountSymbols accountSymbols) {
        String symbol = cell.getStringCellValue().length() == 5 ? cell.getStringCellValue().concat(ZERO.repeat(2)) : cell.getStringCellValue();
        if (accountSymbols.getAccountSymbolsWithCFAndCE().contains(symbol)) {
            cellsWithCFAndCE = symbolColumns.stream()
                    .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) &&
                            (nextCell.getStringCellValue().length() == 20 || nextCell.getStringCellValue().length() == 22))
                    .filter(nextCell -> !accountSymbols.getAccountSymbolsWithCFAndCE().contains(getSymbol(nextCell)))
                    .collect(Collectors.toList());
            return !cellsWithCFAndCE.isEmpty();
        }

        if (!symbol.endsWith(ZERO.repeat(2))) {
            return accountSymbols.getAccountSymbols().contains(symbol);
        }

        if (accountSymbols.getAccountSymbolsEndInTwoZeros().contains(symbol)) {
            cellsWithCF = symbolColumns.stream()
                    .filter(nextCell -> nextCell.getStringCellValue()
                            .startsWith(cell.getStringCellValue()) && nextCell.getStringCellValue().length() == 16)
                    .collect(Collectors.toList());
            cellsWithCF = cellsWithCF.stream()
                    .filter(nextCell -> filterDifferentSymbol(nextCell, cellsWithCF))
                    .collect(Collectors.toList());
            return cellsWithCF.size() < 2;
        } else {
            cellsWithCF.clear();
            return false;
        }
    }

     boolean filterCellsWithLengthBetween14And20(Cell cell, AccountSymbols accountSymbols) {
        String symbol = getSymbol(cell);
        if (accountSymbols.getAccountSymbolsWithCF().contains(symbol)) {
            cell.setCellValue(getCellContent(cell));
            return true;
        } else {
            if (cellsWithCF.size() >= 2) {
                if(accountSymbols.getAccountSymbols().contains(symbol) ||
                        accountSymbols.getAccountSymbolsEndInTwoZeros().contains(symbol) ||
                        accountSymbols.getAccountSymbolsEndInFourZeros().contains(symbol)) {
                    cell.setCellValue(cell.getStringCellValue().substring(0, 10));
                }
                cell.setCellValue(getCellContent(cell));
                return cellsWithCF.contains(cell);
            } else {
                cell.setCellValue(cell.getStringCellValue().substring(0, 10));
                return accountSymbols.getAccountSymbols().contains(symbol);
            }
        }
    }

     boolean filterCellsWithLengthBetween20And22(Cell cell, List<Cell> symbolColumns, AccountSymbols accountSymbols) {
        String symbol = getSymbol(cell);
        cell.setCellValue(getCellContent(cell));
        if (accountSymbols.getAccountSymbolsWithCFAndCE().contains(symbol) && cell.getStringCellValue().length() == 20) {
            cellsWithCFAndCE = symbolColumns.stream()
                    .filter(nextCell -> nextCell.getStringCellValue().startsWith(cell.getStringCellValue()) &&
                            nextCell.getStringCellValue().length() == 22)
                    .collect(Collectors.toList());
            return cellsWithCFAndCE.isEmpty();
        }

        return accountSymbols.getAccountSymbolsWithCFAndCE().contains(symbol);
    }

    private String getCellContent(Cell cell) {
        String content = cell.getStringCellValue();
        int indexG = content.indexOf('G') > -1 ? content.indexOf('G') : content.indexOf('A');
        if(indexG != 9) {
            String symbol = getSymbol(cell);
            String contSufix = content.substring(indexG-2);
            return symbol.concat(contSufix);
        }
        return content;
    }

    private boolean filterDifferentSymbol(Cell cell, List<Cell> symbolColumns) {
        String symbol = cell.getStringCellValue().substring(0, SYMBOL_LENGTH);
        boolean result = true;
        for (Cell nextCell : symbolColumns) {
            if (!nextCell.equals(cell) && nextCell.getStringCellValue()
                    .startsWith(symbol)) {
                result = false;
            }
        }
        return result;
    }

    private String getSymbol(Cell cell) {
        String content = cell.getStringCellValue();
        int indexOfSource = content.indexOf('G') > -1 ? content.indexOf('G') : content.indexOf('A');
        String symbol = cell.getStringCellValue().substring(0, indexOfSource-2);
        return symbol.concat(ZERO.repeat(SYMBOL_LENGTH - symbol.length()));
    }
}
