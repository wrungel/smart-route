package com.smartroute.controller;


import com.smartroute.model.ContractStation;
import com.smartroute.model.Contract;
import com.smartroute.service.OrderService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;

@SessionScoped
@Named
public class ContractEditor  implements Serializable {
    @Inject private FacesContext facesContext;
    @Inject private OrderService orderService;
    @Inject private Authorization authorization;
    private Contract contract;

    @PostConstruct
    void init() {
        contract = new Contract();
        contract.setCustomer(authorization.getCurrent());

        ContractStation contractStation = new ContractStation();
        contractStation.setAddress("Moscow");
        contractStation.setWeightKg(new BigDecimal(31));
        contractStation.setNumberInSequence(1);
        contractStation.setContract(contract);
        contract.getStations().add(contractStation);

        contractStation = new ContractStation();
        contractStation.setAddress("SPB");
        contractStation.setWeightKg(new BigDecimal(11));
        contractStation.setNumberInSequence(2);
        contractStation.setContract(contract);
        contract.getStations().add(contractStation);

    }


    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void addStation() {
        ContractStation contractStation = new ContractStation();
        contractStation.setContract(contract);
        contract.getStations().add(contractStation); 
    }



    public void saveContract() {
        try {
            contract = orderService.create(contract);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Created!", "Order successfully created!"));
            init();

        } catch (Exception e) {
            Throwable t = e;
            while (e.getCause() != null && t != e.getCause()) {
                t = e.getCause();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, t.getClass().getName(), t.getMessage()));            
        }

    }
}  