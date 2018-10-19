package bsoft.nl.channel.repositories;

import bsoft.nl.channel.security.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    public final static String FIND_ACCOUNT_BY_ACCOUNTID_QUERY = "SELECT a " +
            "FROM Account a " +
            "WHERE a.accountId = :accountid";

    public final static String FIND_ACCOUNT_BY_USERNAME_QUERY = "SELECT a " +
            "FROM Account a " +
            "WHERE a.username = :username";

    public final static String DELETE_ACCOUNT_BY_NAME_QUERY = "DELETE " +
            "FROM Account a " +
            "WHERE a.username = :username";

    @Query(FIND_ACCOUNT_BY_ACCOUNTID_QUERY)
    public Account findByAccountId(@Param("accountid") Integer accountid);

    @Query(FIND_ACCOUNT_BY_USERNAME_QUERY)
    public Account findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(DELETE_ACCOUNT_BY_NAME_QUERY)
    public void deleteAccountByName(@Param("username") String username);

}
