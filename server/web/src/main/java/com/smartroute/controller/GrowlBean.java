package com.smartroute.controller;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@Model
public class GrowlBean {

    private String text;
    
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public void save(ActionEvent actionEvent) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        context.addMessage(null, new FacesMessage("Successful", "Hello " + text));
        context.addMessage(null, new FacesMessage("Second Message", "Additional Info Here..."));
    }
}

                        