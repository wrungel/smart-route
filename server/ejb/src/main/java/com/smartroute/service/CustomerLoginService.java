package com.smartroute.service;

import javax.ejb.Stateless;

@Stateless
public class CustomerLoginService {
    
    
    
    public boolean login(String username, String password) {
        return username.equals("real") && password.equals("pass");
    }
}
