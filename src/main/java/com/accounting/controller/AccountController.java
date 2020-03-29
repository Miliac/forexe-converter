package com.accounting.controller;

import com.accounting.model.AccountDTO;
import com.accounting.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/save")
    public String saveAccount(@Valid @ModelAttribute("account") AccountDTO account, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("hasErrors", true);
            model.addAttribute("modalId", account.getModalId());
        } else {
            if(accountService.getAccountByName(account.getUsername()).isPresent()) {
                model.addAttribute("existUsername", true);
                model.addAttribute("modalId", account.getModalId());
            } else {
                accountService.saveAccount(account);
            }
        }

        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable int id) {
        accountService.deleteAccount(id);
        return "redirect:/admin";
    }
}
