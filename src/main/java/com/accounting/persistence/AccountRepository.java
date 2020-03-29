package com.accounting.persistence;

import com.accounting.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByUsername(String username);

    Account findAccountByUsername(String username);
}
