package bsoft.nl.channel.controllers;

import bsoft.nl.channel.security.Account;
import bsoft.nl.channel.security.AccountsList;
import bsoft.nl.channel.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*",
        allowCredentials = "true",
        allowedHeaders = {"*"},
        methods = {RequestMethod.GET,
                RequestMethod.DELETE,
                RequestMethod.POST,
                RequestMethod.OPTIONS
        })
@RestController
public class AccountController extends ResourceSupport {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService = null;

    @Autowired
    public AccountController(final AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<AccountsList> getAccounts() {
        logger.info("Get accounts list");

        Iterable<Account> accountList = accountService.getAll();

        AccountsList accountResult = new AccountsList();

        for (Account account : accountList) {
            if (account.getLinks().size() == 0) {
                Link link = ControllerLinkBuilder
                        .linkTo(ControllerLinkBuilder
                                .methodOn(AccountController.class).getAccounts())
                        .slash(account.getAccountId())
                        .withSelfRel();

                account.add(link);
            }
            accountResult.getAccount().add(account);
        }

        //Adding self link channels collection resource
        Link selfLink = ControllerLinkBuilder
                .linkTo(ControllerLinkBuilder
                        .methodOn(AccountController.class).getAccounts())
                .withSelfRel();

        accountResult.add(selfLink);

        logger.info("Result: {}", accountResult.toString());

        return new ResponseEntity<AccountsList>(accountResult, HttpStatus.OK);
    }

    @GetMapping("/accounts/{userName}")
    public ResponseEntity<Account> getUser(@PathVariable String userName) {

        Account savedAccount = null;

        savedAccount = accountService.getAccount(userName);
        HttpStatus hs = HttpStatus.NOT_FOUND;

        if (savedAccount != null) {
            if (savedAccount.getLinks().size() == 0) {
                Link link = ControllerLinkBuilder
                        .linkTo(ControllerLinkBuilder
                                .methodOn(AccountController.class).getAccounts())
                        .slash(savedAccount.getAccountId())
                        .withSelfRel();

                savedAccount.add(link);
            }
            hs = HttpStatus.OK;
        }

        return new ResponseEntity<Account>(savedAccount, hs);
    }

    /*
    POST - Create new channel
     */
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody final Account account) {
        logger.info("Create Account for: {}", account.getUsername());

        Account savedAccount = accountService.create(account);

        if (null == savedAccount) {
            return new ResponseEntity<Account>(savedAccount, HttpStatus.NOT_MODIFIED);
        } else {

            logger.info("Account id: " + savedAccount.getAccountId());

            return new ResponseEntity<Account>(savedAccount, HttpStatus.CREATED);
        }
    }

    /*
    Delete - Delete an existing channel
    */
    @DeleteMapping("/accounts/{userName}")
    public ResponseEntity<Account> deleteAccount(@PathVariable String userName) {
        logger.info("Delete account for: {}", userName);
        Account savedAccount = null;
        boolean result = accountService.delete(userName);

        if (result) {
            return new ResponseEntity<Account>(savedAccount, HttpStatus.OK);
        } else {
            return new ResponseEntity<Account>(savedAccount, HttpStatus.NOT_MODIFIED);
        }
    }

}