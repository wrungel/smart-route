package com.smartroute.controller;

import com.smartroute.model.Customer;

import com.smartroute.service.CustomerLoginService;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;

@SessionScoped
@Named("auth")
public class Authorization implements Serializable {

    private String password;
    private String username;

    private Customer current;

    @Inject CustomerLoginService customerLoginService;

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

    public String login() {
        current = customerLoginService.login(username, password);
        if (current == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown login, try again"));
            return (username = password = null);
        } else {
            //Authorization auth = (Authorization) req.getSession().getAttribute("auth");

            return "/customer/order.xhtml?faces-redirect=true";
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
