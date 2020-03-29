package com.accounting.controller;

import com.accounting.model.F1102Type;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Arrays;

@Controller
public class MainController {

    @GetMapping("/")
    public String getXls() {
        return "login";
    }

    @GetMapping("/home")
    public String getHomeView(Model model) {
        LocalDateTime dateTime = LocalDateTime.now();
        int currentYear = dateTime.getYear();
        int currentMonth = dateTime.getMonthValue();
        model.addAttribute("f1102Type", new F1102Type());
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("years", Arrays.asList(String.valueOf(currentYear-1), String.valueOf(currentYear)));
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("months", Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12"));
        model.addAttribute("sectors", Arrays.asList("01 - Buget stat","02 - Buget local"));
        return "home";
    }
}
