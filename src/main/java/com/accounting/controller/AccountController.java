package com.accounting.controller;

import com.accounting.model.AccountDTO;
import com.accounting.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("account")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute("newAccount") AccountDTO account, BindingResult result,
                                RedirectAttributes redirectAttributes, HttpServletRequest request) {
        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        return getRedirect(account, result, redirectAttributes, "modal-create", "newAccount");
    }

    @PostMapping("/edit")
    public String editAccount(@Valid @ModelAttribute("editAccount") AccountDTO account, BindingResult result,
                              RedirectAttributes redirectAttributes, HttpServletRequest request) {
        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        String modalId = "modal-edit" + account.getIdAccount();
        return getRedirect(account, result, redirectAttributes, modalId, "editAccount");
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable int id, HttpServletRequest request) {
        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        accountService.deleteAccount(id);
        return "redirect:/admin";
    }

    @PostMapping("/changePassword")
    public String changePasswordAccount(@ModelAttribute("editAccount") AccountDTO account, BindingResult result,
                                        RedirectAttributes redirectAttributes, HttpServletRequest request) {
        logger.info("User {} with IP: {} Executed {} request on endpoint: {}",
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        if (StringUtils.isEmpty(account.getNewPassword())) {
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
