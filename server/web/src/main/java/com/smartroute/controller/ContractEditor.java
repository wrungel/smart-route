package com.smartroute.controller;


import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.smartroute.model.Contract;
import com.smartroute.model.ContractStation;
import com.smartroute.model.ContractStationKind;
import com.smartroute.service.ContractService;

@SessionScoped
@Named
public class ContractEditor  implements Serializable {
    @Inject private FacesContext facesContext;
    @Inject private ContractService orderService;
    @Inject private Authorization authorization;
    private Contract contract;

    @PostConstruct
    void init() {
        contract = new Contract();
        contract.setCustomer(authorization.getCurrent());

        ContractStation contractStation = new ContractStation();
        contractStation.setKind(ContractStationKind.LOAD);
        contractStation.setNumberInSequence(1);
        contractStation.setContract(contract);
        contract.getStations().add(contractStation);

        contractStation = new ContractStation();
        contractStation.setKind(ContractStationKind.UNLOAD);
        contractStation.setNumberInSequence(2);
        contractStation.setContract(contract);
        contract.getStations().add(contractStation);
    }

    private String contractCustomer;
    
    public String getContractCustomer() {
    	return contractCustomer;
    }
    public void setContractCustomer(String contractCustomer) {
		this.contractCustomer = contractCustomer;
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

    public ContractStation getStart() {
    	return contract.getStations().get(0);
    }
    
    public ContractStation getEnd() {
    	return contract.getStations().get(1);
    }

    public void saveContract() {
        try {
            contract = orderService.create(contract);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Created!", "Order successfully created!"));
            init();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, e.getClass().getName(), e.getMessage()));            
        }

    }
}  