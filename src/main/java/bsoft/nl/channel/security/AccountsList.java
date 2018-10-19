package bsoft.nl.channel.security;

import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountsList extends ResourceSupport implements Serializable {
    private static final long serialVersionUID = 5L;

    private List<Account> accounts = new ArrayList<Account>();

    public List<Account> getAccount() {
        return accounts;
    }

    public void setAccount(List<Account> accounts) {
        this.accounts = accounts;
    }
}
