package com.accounting.controller;

import com.accounting.model.AccountDTO;
import com.accounting.model.F1102TypeDTO;
import com.accounting.service.AccountService;
import com.accounting.service.ConversionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.accounting.config.Utils.F1102_MODEL;

@Controller
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    private ConversionService conversionService;
    private AccountService accountService;

    @Autowired
    public MainController(ConversionService conversionService, AccountService accountService) {
        this.conversionService = conversionService;
        this.accountService = accountService;
    }

    @GetMapping("/")
    public String getXls(HttpServletRequest request) {
        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        return "login";
    }

    @GetMapping("/home")
    public String getHomeView(Model model, HttpServletRequest request) {
        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        LocalDate date = LocalDate.now();
        int currentYear = date.getYear();
        int currentMonth = date.getMonthValue();
        String dataDocument = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (Objects.isNull(model.getAttribute(F1102_MODEL))) {
            Optional<AccountDTO> account = accountService.getAccountByName(request.getRemoteUser());
            if (account.isPresent()) {
                model.addAttribute(F1102_MODEL, new F1102TypeDTO(currentYear, currentMonth, dataDocument, account.get().getName(), account.get().getCui()));
            } else {
                model.addAttribute(F1102_MODEL, new F1102TypeDTO(currentYear, currentMonth, dataDocument, Strings.EMPTY, Strings.EMPTY));
            }
        }
        model.addAttribute("years", Arrays.asList(String.valueOf(currentYear - 1), String.valueOf(currentYear)));
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("months", Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        model.addAttribute("sectors", Collections.singletonList("02 - Buget local"));
        return "home";
    }

    @PostMapping("/home")
    public void convertXLSFile(@ModelAttribute F1102TypeDTO f1102TypeDTO, HttpServletResponse response, HttpServletRequest request) {

        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=result.xml");
        conversionService.convert(f1102TypeDTO.getXlsFile(), conversionService.getFromDTO(f1102TypeDTO), response);
        try {
            response.flushBuffer();
        } catch (IOException ex) {
            logger.error("Error occurred while downloading XML file {} ", ex.getMessage());
        }
    }
}
