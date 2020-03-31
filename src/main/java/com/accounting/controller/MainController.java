package com.accounting.controller;

import com.accounting.model.F1102TypeDTO;
import com.accounting.service.ConversionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Controller
public class MainController {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    private ConversionService conversionService;

    @Autowired
    public MainController(ConversionService conversionService) {
        this.conversionService = conversionService;
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
        LocalDateTime dateTime = LocalDateTime.now();
        int currentYear = dateTime.getYear();
        int currentMonth = dateTime.getMonthValue();
        if (Objects.isNull(model.getAttribute("f1102Type"))) {
            model.addAttribute("f1102Type", new F1102TypeDTO());
        }
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("years", Arrays.asList(String.valueOf(currentYear - 1), String.valueOf(currentYear)));
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("months", Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"));
        model.addAttribute("sectors", Arrays.asList("01 - Buget stat", "02 - Buget local"));
        return "home";
    }

    @PostMapping("/convertXML")
    public String convertXLSFile(@Valid @ModelAttribute("f1102Type") F1102TypeDTO f1102TypeDTO, BindingResult result, RedirectAttributes redirectAttributes, HttpServletResponse response, HttpServletRequest request) {

        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.f1102Type", result);
            redirectAttributes.addFlashAttribute("f1102Type", f1102TypeDTO);
            return "redirect:/home";
        }

        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=result.xml");
        conversionService.convert(f1102TypeDTO.getXlsFile(), conversionService.getFromDTO(f1102TypeDTO), response);
        try {
            response.flushBuffer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "";
    }
}
