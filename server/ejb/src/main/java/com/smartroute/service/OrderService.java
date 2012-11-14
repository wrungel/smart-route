package com.smartroute.service;

import com.smartroute.model.Customer;

import com.smartroute.model.Order;
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
public class OrderService {
    @Inject
    private Logger log;

    private 
    @Inject EntityManager em;

    @Resource(mappedName="java:/JmsXA")
    private ConnectionFactory connectionFactory;


    @Resource(mappedName="java:/scheduler/Queue")
    private Queue queue;

    /**
     * returns all orders for the given customer
     * @param customer 
     * @return all orders for the given customer
     */
    public List<Order> getCustomerOrders(Customer customer) {
        TypedQuery<Order> query =
                em.createQuery("SELECT o FROM Order o WHERE o.customer = :customer", Order.class);
        query.setParameter("customer", customer);
        return query.getResultList();
    }
    
    /**
     * Persists a new Order and sends JMS message to the queue for subsequent processing by scheduler.
     * The Scheduler will start processing in a new transaction.
     */
    public void create(Order order) {
        log.info("Persisting order");
        em.persist(order);
        log.info("order persisted with id " + order.getId());
        QueueConnection connection = null;
        QueueSession session = null;
        QueueSender messageSender = null;
        try {
            log.info("Creating connection");
            if (connectionFactory == null) {
                log.error("connectionFactory is null!");
                return;
            }
            QueueConnectionFactory qcf = (QueueConnectionFactory) connectionFactory;
            connection = qcf.createQueueConnection();
            
            log.info("Creating session");
            
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            log.info("Creating producer");
            messageSender = session.createSender(queue);
            log.info("Creating text message");
            TextMessage message = session.createTextMessage();
            message.setText(order.getId().toString());
            log.info("sending message");
            messageSender.send(message);
            log.info("message sent");

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

    }
}
