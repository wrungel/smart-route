package com.smartroute.controller;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.Date;
  
@SessionScoped
@Named
public class ButtonBean implements Serializable {  
  
    private Date date;
    
    @Inject Logger logger;
    
    public ButtonBean() {
        date = new Date();
        
    }
    
    private String text = "some text";
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    @PostConstruct
    void postConstruct() {
        logger.info("date: " + date.toString());
    }
    
    public void destroyWorld(ActionEvent actionEvent){  
        logger.info("date: " + date.toString());
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "System Error",  "Please try again later.");  
          
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
}