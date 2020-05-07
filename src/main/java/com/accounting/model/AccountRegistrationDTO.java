package com.accounting.model;

import com.accounting.validators.FieldMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match!")
public class AccountRegistrationDTO {

    @NotNull(message = "Username is empty!")
    @Size(min=6, max=30, message = "Username size is not between 6 and 30 characters!")
    private String email;

    @Size(min=3, max=100, message = "Name size is not between 3 and 100 characters!")
    private String numeEntitatePublica;

    @Size(min=7, max=7, message = "CUI should have exactly 7 characters!")
    private String cui;

    @NotNull(message = "Password is empty!")
    @Size(min=8, max=70, message = "Password size is not between 8 and 20 characters!")
    private String password;

    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeEntitatePublica() {
        return numeEntitatePublica;
    }

    public void setNumeEntitatePublica(String numeEntitatePublica) {
        this.numeEntitatePublica = numeEntitatePublica;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
