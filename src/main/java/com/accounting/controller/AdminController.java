package com.accounting.controller;

import com.accounting.model.AccountDTO;
import com.accounting.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Objects;

@Controller
public class AdminController {

    private AccountService accountService;

    public AdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/admin")
    public String getAdminView(Model model) {
        model.addAttribute("accounts",accountService.getAccounts());
        if(Objects.isNull(model.getAttribute("newAccount"))) {
            model.addAttribute("newAccount", new AccountDTO());
        }
        if(Objects.isNull(model.getAttribute("editAccount"))) {
            model.addAttribute("editAccount", new AccountDTO());
        }
        model.addAttribute("accountStatuses", Arrays.asList("active", "inactive"));
        model.addAttribute("accountRoles", Arrays.asList("USER", "ADMIN"));
        return "admin";
    }
}
