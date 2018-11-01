package bsoft.nl.channel;

import bsoft.nl.channel.controllers.AccountController;
import bsoft.nl.channel.repositories.AccountRepository;
import bsoft.nl.channel.security.Account;
import bsoft.nl.channel.security.AccountsList;
import bsoft.nl.channel.services.AccountService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.ResponseEntity;
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

        assertThat(account.getKey()).isNotNull();
        assertThat(account.getKey().length()).isGreaterThan(0);
        logger.info("account - accountId: {} Id: {} username: {} password: {}", account.getAccountId(), account.getKey(), account.getUsername(), account.getPassword());
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
        assertThat(accounts.size()).isEqualTo(maxAccounts + 2);

        logger.info("End   test: {}", name.getMethodName());
    }


    @Test
    public void findAccountById() {
        logger.info("Start test: {}", name.getMethodName());


        // get list of all known channels
        int accountid = 1; // existing account
        Account account = accountRepository.findByAccountId(accountid);
        assertThat(account).isNotNull();

        logger.info("account - accountId: {} Key: {} username: {} password: {}", account.getAccountId(), account.getKey(), account.getUsername(), account.getPassword());
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void findAccountByName() {
        logger.info("Start test: {}", name.getMethodName());


        // get list of all known channels
        String username = "admin"; // existing account
        Account account = accountRepository.findByUsername(username);

        assertThat(account).isNotNull();
        logger.info("account - accountId: {} Key: {} username: {} password: {}", account.getAccountId(), account.getKey(), account.getUsername(), account.getPassword());
        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void getAccountsByController() {
        logger.info("Start test: {}", name.getMethodName());

        AccountService as = new AccountService(accountRepository);
        AccountController ac = new AccountController(as);

        ResponseEntity<AccountsList> al = ac.getAccounts();
        logger.info("Account list: {} - {}", al.toString(), "accountid: " + al.getBody().getAccount().get(0).getAccountId() + " username: " + al.getBody().getAccount().get(0).getUsername() + " key: " + al.getBody().getAccount().get(0).getKey());
        assertThat(al).isNotNull();
        assertThat(al.getBody()).isNotNull();
        assertThat(al.getBody().getAccount()).isNotNull();
        assertThat(al.getBody().getAccount().size()).isEqualTo(2);

        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void getUserByController() {
        logger.info("Start test: {}", name.getMethodName());

        AccountService as = new AccountService(accountRepository);
        AccountController ac = new AccountController(as);

        ResponseEntity<Account> al = ac.getUser("admin");
        logger.info("Account list: {} - {}", al.toString(), "accountid: " + al.getBody().getAccountId() + " username: " + al.getBody().getUsername() + " key: " + al.getBody().getKey());
        assertThat(al).isNotNull();
        assertThat(al.getBody()).isNotNull();
        assertThat(al.getBody()).isNotNull();

        logger.info("End   test: {}", name.getMethodName());
    }

    @Test
    public void createUserByController() {
        logger.info("Start test: {}", name.getMethodName());

        AccountService as = new AccountService(accountRepository);
        AccountController ac = new AccountController(as);

        ResponseEntity<Account> account = null;
        int maxSize = 10;

        for (int i = 0; i < maxSize; i++) {
            Account act = new Account();
            act.setUsername("user_" + i);
            act.setPassword("password_" + i);
            account = ac.createAccount(act);

            assertThat(account).isNotNull();
        }

        ResponseEntity<AccountsList> al = ac.getAccounts();
        logger.info("Account list: {} - {}", al.toString(), "accountid: " + al.getBody().getAccount().get(0).getAccountId() + " username: " + al.getBody().getAccount().get(0).getUsername() + " key: " + al.getBody().getAccount().get(0).getKey());
        assertThat(al).isNotNull();
        assertThat(al.getBody()).isNotNull();
        assertThat(al.getBody().getAccount()).isNotNull();
        assertThat(al.getBody().getAccount().size()).isEqualTo(maxSize + 2);
    }

}
