package com.smartroute.controller;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@Model
public class StartStationMapBean implements Serializable {

	private MapModel draggableModel;
	
	@Inject ContractEditor contractEditor;

	public StartStationMapBean() {
		draggableModel = new DefaultMapModel();
		
		//Shared coordinates
		LatLng coord1 = new LatLng(50.784361, 6.053046);
		LatLng coord2 = new LatLng(51.214619, 6.792017);
		
		//Draggable
		draggableModel.addOverlay(new Marker(coord1, "Aachen, Kastanienweg 13"));
		draggableModel.addOverlay(new Marker(coord2, "Düsseldorf"));
		
		for(Marker marker : draggableModel.getMarkers()) {
			marker.setDraggable(true);
		}
	}
	
	public void onPointSelect(PointSelectEvent event) {  
        LatLng latlng = event.getLatLng();  
          
        contractEditor.getStart().setLatitude(new BigDecimal(latlng.getLat()));
        contractEditor.getStart().setLongitude(new BigDecimal(latlng.getLng()));
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));  
    }  
	
	public MapModel getDraggableModel() {
		return draggableModel;
	}
	
	public void onMarkerDrag(MarkerDragEvent event) {
		Marker marker = event.getMarker();
		
		addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Dragged", "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));
	}
	
	public void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
					