package com.accounting.service;

import com.accounting.model.Attachment;
import com.accounting.model.EmailDTO;
import com.accounting.reader.XLSReader;
import org.apache.poi.ss.usermodel.Cell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.accounting.config.Utils.*;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public abstract class AbstractConversionService {
    XLSReader xlsReader;
    MailService mailService;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    AbstractConversionService(MailService mailService) {
        this.mailService = mailService;
        xlsReader = new XLSReader();
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
}
