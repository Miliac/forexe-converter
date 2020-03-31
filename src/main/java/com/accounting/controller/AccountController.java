package com.accounting.controller;

import com.accounting.model.AccountDTO;
import com.accounting.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute("account") AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes) {
        return getRedirect(account, result, redirectAttributes, "modal-create", "account");
    }

    @PostMapping("/edit")
    public String editAccount(@Valid @ModelAttribute("editAccount") AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes) {
        String modalId = "modal-edit" + account.getIdAccount();
        return getRedirect(account, result, redirectAttributes, modalId, "editAccount");
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable int id) {
        accountService.deleteAccount(id);
        return "redirect:/admin";
    }

    @PostMapping("/changePassword")
    public String changePasswordAccount(@ModelAttribute("editAccount") AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes) {
        if(StringUtils.isEmpty(account.getNewPassword())) {
            result.rejectValue("newPassword", null, "New password should not be empty!");
            redirectAttributes.addFlashAttribute("hasErrors", true);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editAccount", result);
            redirectAttributes.addFlashAttribute("editAccount", account);
            redirectAttributes.addFlashAttribute("modalId", "modal-change-password");
        } else {
            if (!account.getNewPassword().equals(account.getConfirmPassword())) {
                result.rejectValue("confirmPassword", null, "Confirm password should be same as password!");
                redirectAttributes.addFlashAttribute("hasErrors", true);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editAccount", result);
                redirectAttributes.addFlashAttribute("editAccount", account);
                redirectAttributes.addFlashAttribute("modalId", "modal-change-password");
            } else {
                accountService.changePasswordAccount(account.getIdAccount(), account.getNewPassword());
            }
        }

        return "redirect:/admin";
    }

    private String getRedirect(AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes, String modalId, String accountModel) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("hasErrors", true);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult."+accountModel, result);
            redirectAttributes.addFlashAttribute(accountModel, account);
            redirectAttributes.addFlashAttribute("modalId", modalId);
        } else {
            if (accountService.getAccountByName(account.getUsername()).isPresent()) {
                result.rejectValue("username", null,"Username already exist!");
                redirectAttributes.addFlashAttribute("hasErrors", true);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult."+accountModel, result);
                redirectAttributes.addFlashAttribute(accountModel, account);
                redirectAttributes.addFlashAttribute("modalId", modalId);
            } else {
                if(modalId.equals("modal-create")) {
                    account.setPassword(accountService.getEncryptedPassword(account.getPassword()));
                }
                accountService.saveAccount(account);
            }
        }
        return "redirect:/admin";
    }
}
