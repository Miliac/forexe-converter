package com.accounting.controller;

import com.accounting.model.AccountDTO;
import com.accounting.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Controller
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public RedirectView createAccount(@Valid @ModelAttribute("account") AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes) {
        return getRedirect(account, result, redirectAttributes, "modal-create");
    }

    @PostMapping("/edit")
    public RedirectView editAccount(@Valid @ModelAttribute("account") AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes) {
        String modalId = "modal-edit" + account.getIdAccount();
        return getRedirect(account, result, redirectAttributes, modalId);
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable int id) {
        accountService.deleteAccount(id);
        return "redirect:/admin";
    }

    private RedirectView getRedirect(AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes, String modalId) {
        RedirectView redirectView = new RedirectView("/admin", true);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("hasErrors", true);
            redirectAttributes.addFlashAttribute("modalId", modalId);
        } else {
            if (accountService.getAccountByName(account.getUsername()).isPresent()) {
                redirectAttributes.addFlashAttribute("existUsername", true);
                redirectAttributes.addFlashAttribute("modalId", modalId);
            } else {
                accountService.saveAccount(account);
            }
        }
        redirectView.setAttributesMap(redirectAttributes.getFlashAttributes());
        return redirectView;
    }
}
