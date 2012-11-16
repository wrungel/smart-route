package com.smartroute.controller;

import com.smartroute.model.Contract;
import com.smartroute.service.OrderService;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;

@RequestScoped
public class Orders {
    
    @Inject
    OrderService orderService;
    
    
    @Inject Authorization authorization;
    
    @Produces
    @RequestScoped
    @Named
    public List<Contract> getContracts() {
        return orderService.getCustomerOrders(authorization.getCurrent());
    }

}
