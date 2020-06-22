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
    public static final String CHANGE_PASSWORD_MODAL = "modal-change-password%s";
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
    public static final String COMMA = ",";
    public static final String X = "X";
    public static final String INPUT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String USER_MESSAGE_LOG = "User {} with IP: {} Executed {} request on endpoint: {}";
    public static final String TEXT_CONTENT_TYPE = "text/html";
    public static final String XML_CONTENT_TYPE = "application/xml";
    public static final String F1102_RESULT_NAME = "f1102.xml";
    public static final String F1115_RESULT_NAME = "f1115.xml";
    public static final String F1125_RESULT_NAME = "f1125.xml";
    public static final String F1127_RESULT_NAME = "f1127.xml";

    private Utils() {
    }

}
