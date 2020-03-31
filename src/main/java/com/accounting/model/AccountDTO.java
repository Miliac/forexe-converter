package com.accounting.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountDTO {

    private int idAccount;
    @NotNull(message = "Username is empty!")
    @Size(min=6, max=30, message = "Username size is not between 6 and 30 characters!")
    private String username;
    @NotNull(message = "Password is empty!")
    @Size(min=8, max=70, message = "Password size is not between 8 and 20 characters!")
    private String password;
    @Size(min=3, max=100, message = "Name size is not between 3 and 100 characters!")
    private String name;
    @Size(min=7, max=7, message = "CUI should have exactly 7 characters!")
    private String cui;
    @NotNull(message = "Status is empty!")
    private String status;
    @NotNull(message = "Role is empty!")
    private String role;
    private String newPassword;
    private String confirmPassword;

    public AccountDTO() {
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
