package com.accounting.model;

import com.accounting.validators.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "confirmPassword", message = "Parolele nu se potrivesc!")
public class AccountRegistrationDTO {

    @Email(message = "Adresă de email invalidă")
    @Size(min=6, max=100, message = "Introduceți o adresă de email!")
    private String email;

    @Size(min=3, max=50, message = "Numele entității publice trebuie să fie între 3 si 50 de caractere!")
    private String numeEntitatePublica;

    @Size(min=7, max=7, message = "Codul Unic de Identificare trebuie să fie de exact 7 caractere!")
    private String cui;

    @NotNull(message = "Introduceți o parolă!")
    @Size(min=8, max=24, message = "Parola trebuie să fie între 8 si 24 de caractere!")
    private String password;

    @NotNull(message = "Confirmați parola!")
    @Size(min=8, max=24, message = "Parola trebuie să fie între 8 si 24 de caractere!")
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

    @Override
    public String toString() {
        return "AccountRegistrationDTO{" +
                "email='" + email + '\'' +
                ", numeEntitatePublica='" + numeEntitatePublica + '\'' +
                ", cui='" + cui + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
