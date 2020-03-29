package com.accounting.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountDTO {

    private int idAccount;
    @NotNull
    @Size(min=6, max=30)
    private String username;
    @NotNull
    @Size(min=8, max=20)
    private String password;
    @Size(min=3, max=100)
    private String name;
    @Size(min=7, max=7)
    private String cui;
    @NotNull
    private String status;
    @NotNull
    private String role;

    private String modalId;

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

    public String getModalId() {
        return modalId;
    }

    public void setModalId(String modalId) {
        this.modalId = modalId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
