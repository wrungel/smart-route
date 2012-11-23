package com.smartroute.service;

import com.smartroute.util.SchedulerApi;

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
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/scheduler"),  
        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable")  })
public class ContractDispatcher implements MessageListener {

    @Inject
    Logger log;
    
    @Inject EntityManager em;
    
    @Override
    public void onMessage(Message message) {
        log.debug("received message");
        
        
        
        // TODO: 1) call the scheduler per jni
        // TODO: 2) push results to the frontend client os dispatcher

        
        long orderId = -1;
        try {
            orderId = Long.parseLong(((TextMessage)message).getText());
            log.info("orderId = " + orderId);
            Contract order = em.find(Contract.class, orderId);
            log.info("processing Order " + order);
            
            String bestAvailable = new SchedulerApi().fnBestAvailable(new int[]{-1}, -1);
            log.info("bestAvailable: " + bestAvailable);
            
        } catch (JMSException e) {            
            e.printStackTrace();
        }
        
        
    }
    
}
