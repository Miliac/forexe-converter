package com.accounting.service;

import com.accounting.model.Account;
import com.accounting.model.AccountDTO;
import com.accounting.persistence.AccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAccount(AccountDTO account) {
        logger.info("Account saved in database: {} ", account);
        accountRepository.save(convertFromDTO(account));
    }

    @Override
    public Optional<AccountDTO> getAccountByName(String username) {
        List<Account> results = accountRepository.findByUsername(username);
        if (!results.isEmpty()) {
            return Optional.of(convertToDTO(results.get(0)));
        }
        return Optional.empty();
    }

    @Override
    public void deleteAccount(int id) {
        logger.info("Account with id {} deleted from database", id);
        accountRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            logger.warn("Invalid username or password");
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new User(account.getUsername(), account.getPassword(), account.getStatus()
                .equals("active"), true, true, true, AuthorityUtils.createAuthorityList(account.getRole()));
    }

    @Override
    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Override
    public void changePasswordAccount(int idAccount, String newPassword) {
        logger.info("Password changed for account with id {}", idAccount);
        Optional<Account> accountOptional = accountRepository.findById(idAccount);
        if(accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setPassword(getEncryptedPassword(newPassword));
            accountRepository.save(account);
        }
    }

    @Override
    public boolean isUsernameChanged(AccountDTO accountDTO) {
        Optional<Account> account = accountRepository.findById(accountDTO.getIdAccount());
        return account.filter(value -> !value.getUsername().equals(accountDTO.getUsername())).isPresent();
    }

    @Override
    public String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private Account convertFromDTO(AccountDTO accountDTO) {
        Account account = new Account();
        account.setIdAccount(accountDTO.getIdAccount());
        account.setUsername(accountDTO.getUsername());
        account.setPassword(accountDTO.getPassword());
        account.setName(accountDTO.getName());
        account.setCui(accountDTO.getCui());
        account.setStatus(accountDTO.getStatus());
        account.setRole(accountDTO.getRole());
        return account;
    }

    private AccountDTO convertToDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setIdAccount(account.getIdAccount());
        accountDTO.setUsername(account.getUsername());
        accountDTO.setName(account.getName());
        accountDTO.setPassword(account.getPassword());
        accountDTO.setCui(account.getCui());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setRole(account.getRole());
        return accountDTO;
    }
}
