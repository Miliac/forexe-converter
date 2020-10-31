package com.accounting.controller;

import com.accounting.model.AccountDTO;
import com.accounting.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;

import static com.accounting.config.Utils.*;

@Controller
public class AdminController {

    private static final Logger logger = LogManager.getLogger(AdminController.class);

    private final AccountService accountService;

    public AdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/admin")
    public String getAdminView(Model model, HttpServletRequest request) {
        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        model.addAttribute("accounts", accountService.getAccounts());
        if (Objects.isNull(model.getAttribute(CREATE_MODEL))) {
            model.addAttribute(CREATE_MODEL, new AccountDTO());
        }
        if (Objects.isNull(model.getAttribute(EDIT_MODEL))) {
            model.addAttribute(EDIT_MODEL, new AccountDTO());
        }
        if (Objects.isNull(model.getAttribute(HAS_ERRORS))) {
            model.addAttribute(HAS_ERRORS, false);
        }
        model.addAttribute("accountStatuses", Arrays.asList("active", "inactive"));
        model.addAttribute("accountRoles", Arrays.asList("USER", "ADMIN"));
        return "admin";
    }
}
