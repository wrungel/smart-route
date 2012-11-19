package com.smartroute.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.smartroute.model.Customer;
import com.smartroute.service.CustomerRepository;

@SessionScoped
@Named("auth")
public class Authorization implements Serializable {

    private String password;
    private String username;

    private Customer current;
    
    private String from;
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    

    @Inject CustomerRepository customerRepository;

    @Inject Logger logger;

    public Customer getCurrent() {
        return current;
    }    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void login() throws IOException {
        current = customerRepository.findCustomerByLogin(username);
        if (current == null || current != null && current.getAccount().getPasswd().equals(password)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown login or invalid password, try again"));
            username = password = null;
            return;
        } else {
            //Authorization auth = (Authorization) req.getSession().getAttribute("auth");
            FacesContext.getCurrentInstance().getExternalContext().redirect(from);
            //return "/customer/order.xhtml?faces-redirect=true";
        }
    }

    public String logout() {
        current = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return current != null;
    }


}
