package com.smartroute.service;

import com.smartroute.model.Account;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class AccountRepository {
    private final EntityManager em;


    // public no-arg constructor required for EJBs
    // injection still works fine with the @Inject constructor
    public AccountRepository() {
        this.em = null;
    }

    @Inject
    public AccountRepository(EntityManager em) {
        this.em = em;
    }

    public void persist(Account account) {
        em.persist(account);
    }

}
