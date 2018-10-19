package bsoft.nl.channel.services;

import bsoft.nl.channel.repositories.AccountRepository;
import bsoft.nl.channel.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Transactional
@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private AccountRepository accountRepository = null;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Iterable<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getAccount(final String username) {
        return accountRepository.findByUsername(username);
    }

    public Account create(final Account account) {
        Account savedAccount = null;

        Account existingAccount = null;

        existingAccount = accountRepository.findByUsername(account.getUsername());

        if ((existingAccount != null)) {
            logger.error("Account already exists, id: {} name:{}", account.getAccountId(), account.getUsername());
        } else {
            UserDetails user = User.withDefaultPasswordEncoder()
                    .username(account.getUsername())
                    .password(account.getPassword())
                    .roles("user")
                    .build();

            if ((account.getKey() == null) || (account.getKey().length() == 0)) {
                account.setKey(UUID.randomUUID().toString());
            }
            account.setPassword(user.getPassword());

            logger.info("Account password length: {}, value: {}", user.getPassword().length(), user.getPassword());


            savedAccount = accountRepository.save(account);
            logger.info("Persisted new channel: {}", account.getAccountId());
        }
        return savedAccount;
    }

    public boolean delete(final String username) {
        boolean deleted = false;

        Account existingAccount = null;

        existingAccount = accountRepository.findByUsername(username);

        if ((existingAccount != null)) {
            accountRepository.delete(existingAccount);
            deleted = true;
            logger.error("Account deleted for username:{}", username);
        } else {
            logger.error("Account not found");
        }
        return deleted;
    }

}
