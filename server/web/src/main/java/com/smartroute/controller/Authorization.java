package com.smartroute.controller;

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
    private boolean loggedIn;
    
    @Inject CustomerLoginService customerLoginService;
    
    @Inject Logger logger;
    
    
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
        loggedIn = customerLoginService.login(username, password);
        if (!loggedIn) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unknown login, try again"));
            return (username = password = null);
        } else {
            //Authorization auth = (Authorization) req.getSession().getAttribute("auth");
            
            return "/customer/order.xhtml?faces-redirect=true";
        }
    }

    public String logout() {
        loggedIn = false;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    
}
