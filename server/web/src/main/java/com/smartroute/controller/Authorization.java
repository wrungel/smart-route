package com.smartroute.controller;

import com.smartroute.model.Customer;
import com.smartroute.service.CustomerLoginService;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.IOException;
import java.io.Serializable;

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

    public void login() throws IOException {
        current = customerLoginService.login(username, password);
        if (current == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown login, try again"));
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
