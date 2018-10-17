package bsoft.nl.channel.security;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

@Entity
public class Account {
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountid")
    private Integer accountId;

    @org.springframework.data.annotation.Id
    private String id;
    private String username;
    private String password;

    public Account()
    {}

    public Account(String username, String password) {


        UserDetails user = User.withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .roles("user")
                .build();

        logger.info("Account password length: {}, value: {}", user.getPassword().length(), user.getPassword());

        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = user.getPassword();
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = Id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
