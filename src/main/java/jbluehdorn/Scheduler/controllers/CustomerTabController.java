package jbluehdorn.Scheduler.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.xml.bind.ValidationException;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.repositories.CustomerRepository;
import jbluehdorn.Scheduler.util.Logger;
import jbluehdorn.Scheduler.view.FxmlView;
import jbluehdorn.Scheduler.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
public class CustomerTabController implements FxmlController {
    
    //FXML components
    @FXML
    private TableView tblCustomers;
    @FXML
    private TableColumn<Customer, Integer> colId;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colPhone;
    
    //Constants
    private final StageManager stageManager;
    
    @Autowired @Lazy
    public CustomerTabController(StageManager stageManager) {
        this.stageManager = stageManager;
    }
    
    public void initialize() {
        //Map column values
        colId.setCellValueFactory(customer -> new SimpleIntegerProperty(customer.getValue().getId()).asObject());
        colName.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getName()));
        colPhone.setCellValueFactory(customer -> new SimpleStringProperty(customer.getValue().getAddress().getPhone()));
        
        this.populateTable();
    }
    
    @FXML
    public void btnAddPressed() {
        this.stageManager.switchScene(FxmlView.CUSTOMER_FORM);
    }
    
    @FXML
    public void btnModPressed() {
        try {
            Customer customer = this.getSelectedCustomer();

            CustomerFormController.setCustomerToEdit(customer);
            
            this.stageManager.switchScene(FxmlView.CUSTOMER_FORM);
        } catch(ValidationException ex) {
            Logger.error(ex.getMessage());
            this.showError(ex.getMessage());
        }
    }
    
    @FXML
    public void btnDelPressed() {
        try {
            Customer customer = this.getSelectedCustomer();

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Are you sure you want to delete " + customer.getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                CustomerRepository.delete(customer.getId());
                this.populateTable();
            }
        } catch(ValidationException ex) {
            Logger.error(ex.getMessage());
            this.showError(ex.getMessage());
        } catch(SQLException ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
            this.showError("Delete was unsuccessful");
        }
    }
    
    private void showError(String error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(error);
        alert.showAndWait();
    }
    
    private Customer getSelectedCustomer() throws ValidationException {
        Customer customer = (Customer) this.tblCustomers.getSelectionModel().getSelectedItem();
        
        if(customer == null)
            throw new ValidationException("No customer selected");
            
        return customer;
        
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
