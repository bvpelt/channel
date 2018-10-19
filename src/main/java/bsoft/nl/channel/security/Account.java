package bsoft.nl.channel.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Account extends ResourceSupport implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountid")
    private Integer accountId;

    @org.springframework.data.annotation.Id
    private String key;
    private String username;
    private String password;

    public Account() {
    }

    public Account(String username, String password) {
/*
        UserDetails user = User.withDefaultPasswordEncoder()
                .username(username)
                .password(password)
                .roles("user")
                .build();

        logger.info("Account password length: {}, value: {}", user.getPassword().length(), user.getPassword());
*/
        this.key = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
