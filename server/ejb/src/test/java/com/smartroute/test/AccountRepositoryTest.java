package com.smartroute.test;

import com.smartroute.model.Account;
import com.smartroute.service.AccountRepository;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class AccountRepositoryTest extends AbstractPersistenceTest {

    Account account;




    
    @Test
    public void create_account() {
        AccountRepository accountRepository = new AccountRepository(getEntityManager());
        
        Account account = new Account();
        account.setEmail("uschi");
        account.setName("eddie");
        accountRepository.persist(account);
        assertThat(account.getId(), notNullValue());
    }

    
}
