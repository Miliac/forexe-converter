package com.accounting.config;

import java.math.BigDecimal;

public class Utils {

    public static final BigDecimal ZERO_DECIMAL = BigDecimal.valueOf(0.0);
    public static final int SYMBOL_LENGTH = 7;
    public static final int CF_CE_LENGTH = 6;
    public static final int STRCONT_LENGTH = 40;
    static final String INDEX = "/";
    static final String HOME = "/home";
    static final String ADMIN = "/admin";
    static final String LOGOUT = "/logout";
    public static final String CREATE_MODEL = "newAccount";
    public static final String EDIT_MODEL = "editAccount";
    public static final String F1102_MODEL = "f1102Type";
    public static final String CREATE_MODAL = "modal-create";
    public static final String EDIT_MODAL = "modal-edit-%s";
    public static final String CHANGE_PASSWORD_MODAL = "modal-change-password";
    public static final String REDIRECT_ADMIN = "redirect:" + ADMIN;
    public static final String REDIRECT_HOME = "redirect:/home";
    public static final String HAS_ERRORS = "hasErrors";
    public static final String BINDING_RESULT = "org.springframework.validation.BindingResult.%s";
    public static final String MODAL_ID = "modalId";
    public static final String USERNAME = "username";
    public static final String CONFIRM_PASSWORD = "confirmPassword";
    public static final String NEW_PASSWORD = "newPassword";
    public static final String USERNAME_ERROR = "Username already exist!";
    public static final String CONFIRM_PASSWORD_ERROR = "Confirm password should be same as password!";
    public static final String NEW_PASSWORD_ERROR = "New password should not be empty!";
    public static final String ERROR_CODE = "error";
    public static final String ZERO = "0";
    public static final String LINE = "-";
    public static final String SPACE = " ";
    public static final String X = "X";
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String F1102_DATE_FORMAT = "dd.MM.yyyy";
    public static final String USER_MESSAGE_LOG = "User {} with IP: {} Executed {} request on endpoint: {}";
    public static final String ACCOUNT_GET_SYMBOLS_ERROR_MESSAGE = "Error occurred while getting account symbols!!!";
    public static final String ACCOUNT_ADD_SYMBOLS_ERROR_MESSAGE = "Error occurred while adding account symbols!!!";
    public static final String ACCOUNT_ADD_SYMBOLS_SUCCESS_MESSAGE = "Account symbols file added with success!!!";
    public static final String EXCEPTIONS_GET_ERROR_MESSAGE = "Error occurred while getting exceptions!!!";
    public static final String EXCEPTIONS_ADD_ERROR_MESSAGE = "Error occurred while adding exceptions!!!";
    public static final String EXCEPTIONS_ADD_SUCCESS_MESSAGE = "Exceptions file added with success!!!";

    private Utils() {
    }

}
