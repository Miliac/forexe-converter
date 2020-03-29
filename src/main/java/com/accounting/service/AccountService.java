package com.accounting.service;

import com.accounting.model.Account;
import com.accounting.model.AccountDTO;
import com.accounting.persistence.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void saveAccount(AccountDTO account) {
        accountRepository.save(convertFromDTO(account));
    }

    public Optional<AccountDTO> getAccountByName(String username) {
        List<Account> results = accountRepository.findByUsername(username);
        if (!results.isEmpty()) {
            return Optional.of(convertToDTO(results.get(0)));
        }
        return Optional.empty();
    }

    public void deleteAccount(int id) {
        accountRepository.deleteById(id);
    }

    private Account convertFromDTO(AccountDTO accountDTO) {
        Account account = new Account();
        account.setIdAccount(accountDTO.getIdAccount());
        account.setUsername(accountDTO.getUsername());
        account.setPassword(accountDTO.getPassword());
        account.setName(accountDTO.getName());
        account.setCui(accountDTO.getCui());
        account.setStatus(accountDTO.getStatus());
        return account;
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIdAccount(account.getIdAccount());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setName(account.getName());
        accountDTO.setCui(account.getCui());
        accountDTO.setStatus(account.getStatus());
        return accountDTO;
    }
}
