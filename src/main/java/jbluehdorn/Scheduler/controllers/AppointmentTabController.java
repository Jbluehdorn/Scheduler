package jbluehdorn.Scheduler.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jbluehdorn.Scheduler.models.Appointment;
import jbluehdorn.Scheduler.repositories.AppointmentRepository;
import jbluehdorn.Scheduler.util.Logger;
import org.springframework.stereotype.Component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jordan
 */
@Component
public class AppointmentTabController {
    //FXML Components
    @FXML
    private TableView tblAppointments;
    @FXML
    private TableColumn<Appointment, String> colCust;
    @FXML
    private TableColumn<Appointment, String> colTitle;
    @FXML
    private TableColumn<Appointment, String> colLocation;
    @FXML
    private TableColumn<Appointment, String> colStart;
    @FXML
    private TableColumn<Appointment, String> colEnd;
    
    public void initialize() {
        this.mapColumns();
        this.populateTable();
    }
    
    @FXML
    public void btnAddPressed() {
        
    }
    
    @FXML
    public void btnModPressed() {
        
    }
    
    @FXML
    public void btnDelPressed() {
        
    }
    
    private void mapColumns() {
        DateFormat format = new SimpleDateFormat("M/dd h:mm a");
        
        colCust.setCellValueFactory(app -> new SimpleStringProperty(app.getValue().getCustomer().getName()));
        colTitle.setCellValueFactory(app -> new SimpleStringProperty(app.getValue().getTitle()));
        colLocation.setCellValueFactory(app -> new SimpleStringProperty(app.getValue().getLocation()));
        colStart.setCellValueFactory(app -> new SimpleStringProperty(format.format(app.getValue().getStartDate())));
        colEnd.setCellValueFactory(app -> new SimpleStringProperty(format.format(app.getValue().getEndDate())));
    }
    
    private void populateTable() {
        try {
            Iterable<Appointment> apps = AppointmentRepository.get();
            
            ObservableList<Appointment> appsObs = FXCollections.observableArrayList((ArrayList) apps);
            
            tblAppointments.setItems(appsObs);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
