package jbluehdorn.Scheduler.controllers;

import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.repositories.CustomerRepository;
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
public class CustomerTabController {
    
    //FXML components
    @FXML
    private TableView tblCustomers;
    @FXML
    private TableColumn<Customer, Integer> colId;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colPhone;
    
    public void initialize() {
        //Map column values
        colId.setCellValueFactory(customer -> new SimpleIntegerProperty(customer.getValue().getId()).asObject());
        colName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getName()));
        colPhone.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress().getPhone()));
        
        this.populateTable();
    }
    
    public void btnAddPressed() {
        
    }
    
    private void populateTable() {
        try {
            //Get all customers
            Iterable<Customer> customers = CustomerRepository.get();
            
            //Convert to ObservableList
            ObservableList<Customer> customersObs = FXCollections.observableArrayList((ArrayList<Customer>) customers);
     
            //Set table items
            tblCustomers.setItems(customersObs);
            
        } catch(Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
}
