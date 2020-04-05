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

import static com.accounting.config.Utils.*;

@Controller
@RequestMapping("account")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public String createAccount(@Valid @ModelAttribute(CREATE_MODEL) AccountDTO account, BindingResult result,
                                RedirectAttributes redirectAttributes, HttpServletRequest request) {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        return getRedirect(account, result, redirectAttributes, CREATE_MODAL, CREATE_MODEL);
    }

    @PostMapping("/edit")
    public String editAccount(@Valid @ModelAttribute(EDIT_MODEL) AccountDTO account, BindingResult result,
                              RedirectAttributes redirectAttributes, HttpServletRequest request) {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        return getRedirect(account, result, redirectAttributes, String.format(EDIT_MODAL, account.getIdAccount()), EDIT_MODEL);
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable int id, HttpServletRequest request) {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        accountService.deleteAccount(id);
        return REDIRECT_ADMIN;
    }

    @PostMapping("/changePassword")
    public String changePasswordAccount(@ModelAttribute(EDIT_MODEL) AccountDTO account, BindingResult result,
                                        RedirectAttributes redirectAttributes, HttpServletRequest request) {
        logger.info(USER_MESSAGE_LOG,
                request.getRemoteUser(), request.getRemoteAddr(), request.getMethod(), request.getRequestURI());
        if (StringUtils.isEmpty(account.getNewPassword())) {
            result.rejectValue(NEW_PASSWORD, ERROR_CODE, NEW_PASSWORD_ERROR);
            redirectAttributes.addFlashAttribute(HAS_ERRORS, true);
            redirectAttributes.addFlashAttribute(String.format(BINDING_RESULT, EDIT_MODEL), result);
            redirectAttributes.addFlashAttribute(EDIT_MODEL, account);
            redirectAttributes.addFlashAttribute(MODAL_ID, CHANGE_PASSWORD_MODAL);
        } else {
            if (!account.getNewPassword().equals(account.getConfirmPassword())) {
                result.rejectValue(CONFIRM_PASSWORD, ERROR_CODE, CONFIRM_PASSWORD_ERROR);
                redirectAttributes.addFlashAttribute(HAS_ERRORS, true);
                redirectAttributes.addFlashAttribute(String.format(BINDING_RESULT, EDIT_MODEL), result);
                redirectAttributes.addFlashAttribute(EDIT_MODEL, account);
                redirectAttributes.addFlashAttribute(MODAL_ID, CHANGE_PASSWORD_MODAL);
            } else {
                accountService.changePasswordAccount(account.getIdAccount(), account.getNewPassword());
            }
        }

        return REDIRECT_ADMIN;
    }

    private String getRedirect(AccountDTO account, BindingResult result, RedirectAttributes redirectAttributes, String modalId, String accountModel) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(HAS_ERRORS, true);
            redirectAttributes.addFlashAttribute(String.format(BINDING_RESULT, accountModel) , result);
            redirectAttributes.addFlashAttribute(accountModel, account);
            redirectAttributes.addFlashAttribute(MODAL_ID, modalId);
        } else {
            if (accountService.getAccountByName(account.getUsername()).isPresent()) {
                result.rejectValue(USERNAME, ERROR_CODE,USERNAME_ERROR);
                redirectAttributes.addFlashAttribute(HAS_ERRORS, true);
                redirectAttributes.addFlashAttribute(String.format(BINDING_RESULT, accountModel), result);
                redirectAttributes.addFlashAttribute(accountModel, account);
                redirectAttributes.addFlashAttribute(MODAL_ID, modalId);
            } else {
                if(modalId.equals(CREATE_MODAL)) {
                    account.setPassword(accountService.getEncryptedPassword(account.getPassword()));
                }
                accountService.saveAccount(account);
            }
        }
        return REDIRECT_ADMIN;
    }
}
