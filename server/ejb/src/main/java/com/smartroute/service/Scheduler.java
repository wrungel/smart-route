package com.smartroute.service;

import com.smartroute.model.Contract;
import org.slf4j.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;


@MessageDriven(activationConfig = { 
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"), 
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),  
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "scheduler/Queue"),  
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable")  })
//@MessageDriven(mappedName="java:/scheduler/Queue")
public class Scheduler implements MessageListener {

    @Inject
    private Logger log;
    
    @Inject EntityManager em;
    
    @Override
    public void onMessage(Message message) {
        log.debug("received message");
        
        // TODO: 1) call the scheduler per jni
        // TODO: 2) push results to the frontend client

        
        long orderId = -1;
        try {
            orderId = Long.parseLong(((TextMessage)message).getText());
            log.info("orderId = " + orderId);
            Contract order = em.find(Contract.class, orderId);
            log.info("processing Order " + order);
        } catch (JMSException e) {            
            e.printStackTrace();
        }
        
        
    }
    
}
