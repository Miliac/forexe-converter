package com.accounting.service;

import com.accounting.model.AccountDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

public interface AccountService extends UserDetailsService {
    List<AccountDTO> getAccounts();

    void saveAccount(AccountDTO account);

    Optional<AccountDTO> getAccountByName(String username);

    void deleteAccount(int id);

    BCryptPasswordEncoder getPasswordEncoder();
}
