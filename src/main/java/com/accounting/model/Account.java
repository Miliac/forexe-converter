package com.accounting.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "idaccount", nullable = false)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idAccount;
    @Column(name = "username", length = 45, nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "cui", length = 10)
    private String cui;
    @Column(name = "status", length = 10)
    private String status;
    @Column(name = "role", length = 5, nullable = false)
    private String role;

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
}
