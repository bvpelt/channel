package bsoft.nl.channel;

import bsoft.nl.channel.domain.Channel;
import bsoft.nl.channel.repositories.AccountRepository;
import bsoft.nl.channel.repositories.ChannelRepository;
import bsoft.nl.channel.security.Account;
import bsoft.nl.channel.security.AccountsList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryIntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryIntegrationTest.class);

    @Rule
    public TestName name = new TestName();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    public AccountRepositoryIntegrationTest() {

    }

    @Test
    public void createAccount() {
        logger.info("Start test: {}", name.getMethodName());
        Account account = new Account("testuser", "geheim");
        entityManager.persist(account);

        assertThat(account.getId()).isNotNull();
        assertThat(account.getId().length()).isGreaterThan(0);
        logger.info("account - accountId: {} Id: {} username: {} password: {}", account.getAccountId(), account.getId(), account.getUsername(), account.getPassword());
        logger.info("End   test: {}", name.getMethodName());
    }


    @Test
    public void getAccounts() {
        logger.info("Start test: {}", name.getMethodName());

        int maxAccounts = 10;
        for (int i = 0; i < maxAccounts; i++) {
            Account account = new Account("testuser_" + i, "geheim_" + i);
            entityManager.persist(account);
        }

        // get list of all known channels
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isNotNull();
        assertThat(accounts.size()).isEqualTo(maxAccounts + 1);

        logger.info("End   test: {}", name.getMethodName());
    }


    @Test
    public void findAccountById() {
        logger.info("Start test: {}", name.getMethodName());


        // get list of all known channels
        int accountid = 1; // existing account
        Account account = accountRepository.findByAccountId(accountid);


        logger.info("account - accountId: {} Id: {} username: {} password: {}", account.getAccountId(), account.getId(), account.getUsername(), account.getPassword());

        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void findAccountByName() {
        logger.info("Start test: {}", name.getMethodName());


        // get list of all known channels
        String username = "admin"; // existing account
        Account account = accountRepository.findByUsername(username);


        logger.info("account - accountId: {} Id: {} username: {} password: {}", account.getAccountId(), account.getId(), account.getUsername(), account.getPassword());

        logger.info("End   test: {}", name.getMethodName());
    }

}
