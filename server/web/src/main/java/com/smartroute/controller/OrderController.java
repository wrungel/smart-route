package com.smartroute.controller;

import com.smartroute.model.ConstractStationKind;
import com.smartroute.model.ContractStation;
import com.smartroute.model.Order;
import com.smartroute.service.OrderService;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;



@Model
public class OrderController {
    @Inject
    private FacesContext facesContext;

    @Inject
    private OrderService orderService;

    @Inject
    Logger logger;
    
    private Order newOrder;

    @Produces
    @Named
    public Order getNewOrder() {
        return newOrder;
    }

    
    public void addStation() {
        ContractStation station = new ContractStation();
        station.setContract(newOrder);
        newOrder.getStations().add(station);
        logger.info("order has now " + newOrder.getStations().size() + " stations");
    }
    
    

    public void create() throws Exception {
        logger.info("Creating order ...");
        orderService.create(newOrder);
        logger.info("Order created ...");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Created!", "Order successfully created!"));
        logger.info("Initializing new order ...");
        initNewOrder();
    }

    @PostConstruct
    public void initNewOrder() {
        newOrder = new Order();
        
        ContractStation sourceStation = new ContractStation();
        sourceStation.setContract(newOrder);
        newOrder.getStations().add(sourceStation);
        sourceStation.setKind(ConstractStationKind.load);

        ContractStation destinationStation = new ContractStation();
        destinationStation.setContract(newOrder);
        destinationStation.setKind(ConstractStationKind.unload);
        newOrder.getStations().add(destinationStation);
    }

}
