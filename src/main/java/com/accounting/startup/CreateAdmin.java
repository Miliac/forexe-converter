package com.accounting.startup;

import com.accounting.model.AccountDTO;
import com.accounting.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CreateAdmin implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LogManager.getLogger(CreateAdmin.class);
    private static final String ADMIN_ACCOUNT_NAME = "administrator";

    private final AccountService accountService;

    @Autowired
    public CreateAdmin(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

        if (accountService.getAccountByName(ADMIN_ACCOUNT_NAME)
                .isEmpty()) {

            AccountDTO account = new AccountDTO();
            account.setUsername(ADMIN_ACCOUNT_NAME);
            account.setRole("ADMIN");
            account.setStatus("active");
            account.setName(ADMIN_ACCOUNT_NAME);
            account.setPassword(accountService.getEncryptedPassword("P9nEqhtk!JUR2KYku"));

            accountService.saveAccount(account);

            logger.info("Admin account added with success!");
        } else {
            logger.info("Admin account already exists!");
        }
    }
}
