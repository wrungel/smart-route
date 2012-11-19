package com.smartroute.service;

import com.smartroute.model.Customer;

import com.smartroute.model.Contract;
import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;


@Stateless
public class ContractService {
    @Inject
    private Logger log;

    private 
    @Inject EntityManager em;

    @Resource(mappedName="java:/JmsXA")
    private ConnectionFactory connectionFactory;


    @Resource(mappedName="java:/queue/scheduler")
    private Queue queue;

    /**
     * returns all orders for the given customer
     * @param customer 
     * @return all orders for the given customer
     */
    public List<Contract> getCustomerOrders(Customer customer) {
        TypedQuery<Contract> query =
                em.createQuery("SELECT o FROM Contract o WHERE o.customer = :customer", Contract.class);
        query.setParameter("customer", customer);
        return query.getResultList();
    }
    
    /**
     * Persists a new Order and sends JMS message to the queue for subsequent processing by scheduler.
     * The Scheduler will start processing in a new transaction.
     */
    public Contract create(Contract contract) {
        
        contract = em.merge(contract);
        em.persist(contract);
        log.info("order persisted with id " + contract.getId());
        QueueConnection connection = null;
        QueueSession session = null;
        QueueSender messageSender = null;
        try {
            log.info("Creating connection");
            if (connectionFactory == null) {
                log.error("connectionFactory is null!");
                return contract;
            }
            QueueConnectionFactory qcf = (QueueConnectionFactory) connectionFactory;
            connection = qcf.createQueueConnection();
            
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            messageSender = session.createSender(queue);
            TextMessage message = session.createTextMessage();
            message.setText(contract.getId().toString());
            messageSender.send(message);

        } catch (JMSException e) {
            log.error("JMSException", e);
        } finally {
            try {
                if (messageSender != null)
                    messageSender.close();
                if (session != null)
                    session.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                log.error("Exception", e);
            }
        }


        log.info("creating order");
        return contract;
    }
}
