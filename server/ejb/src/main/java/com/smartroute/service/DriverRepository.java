package com.smartroute.service;

import com.smartroute.model.Driver;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class DriverRepository {
    
    private final EntityManager em;
    
    
    @Inject
    public DriverRepository(EntityManager em) {
        this.em = em;
    }
    
    public void persist(Driver driver) {        
        em.persist(driver);
    }
    
    
    public Driver findById(Long id) {
        return em.find(Driver.class, id);
    }
    
    
}
