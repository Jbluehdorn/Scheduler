/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.controllers;

import fxml.components.TimeTextField;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.repositories.CustomerRepository;
import jbluehdorn.Scheduler.util.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jordan
 */
@Component
public class AppointmentFormController {
    //FXML Components
    @FXML
    private Label lblError;
    @FXML
    private TextField txtTitle;
    @FXML
    private ComboBox cmbCustomer;
    @FXML
    private TextArea txtDesc;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtContact;
    @FXML
    private DatePicker pickerDate;
    @FXML
    private TimeTextField txtStart;
    @FXML
    private TimeTextField txtEnd;
    
    //Constants
    private final String EMPTY_STRING = "";
    
    public void initialize() {
        this.populateCustomerCombo();
    }
    
    @FXML
    public void btnSavePressed() {
        
    }
    
    private void populateCustomerCombo() {
        try {
            Iterable<Customer> customers = CustomerRepository.get();
            
            ObservableList<Customer> customersObs = FXCollections.observableArrayList((ArrayList<Customer>) customers);
            
            this.cmbCustomer.setItems(customersObs);
        } catch(Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void showError(String message) {
        this.lblError.setText(message);
        this.lblError.setVisible(true);
        this.lblError.setManaged(true);
    }
    
    private void hideError() {
        this.lblError.setText(EMPTY_STRING);
        this.lblError.setVisible(false);
        this.lblError.setManaged(false);
    }
}
