/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.controllers;

import fxml.components.TimeTextField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.xml.bind.ValidationException;
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
    private final String TITLE_EMPTY_ERR = "Title is required";
    private final String CUSTOMER_EMPTY_ERR = "Customer is required";
    private final String LOCATION_EMPTY_ERR = "Location is required";
    private final String DATE_EMPTY_ERR = "Date is required";
    private final String START_EMPTY_ERR = "Start time is required";
    private final String END_EMPTY_ERR = "End time is required";
    private final String START_END_MISMATCH_ERR = "End time cannot be before start time";
    private final String PARSE_ERR = "Start or end time could not be parsed";
    
    public void initialize() {
        this.populateCustomerCombo();
    }
    
    @FXML
    public void btnSavePressed() {
        try {
            this.validateForm();
        } catch(ValidationException ex) {
            this.showError(ex.getMessage());
        }
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
    
    private void validateForm() throws ValidationException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        
        if(this.txtTitle.getText().equals(EMPTY_STRING))
            throw new ValidationException(TITLE_EMPTY_ERR);
        
        if(this.cmbCustomer.getValue() == null) 
            throw new ValidationException(CUSTOMER_EMPTY_ERR);
        
        if(this.txtLocation.getText().equals(EMPTY_STRING))
            throw new ValidationException(LOCATION_EMPTY_ERR);
        
        if(this.pickerDate.getValue() == null)
            throw new ValidationException(DATE_EMPTY_ERR);
        
        if(this.txtStart.getText().equals(EMPTY_STRING))
            throw new ValidationException(START_EMPTY_ERR);
        
        if(this.txtEnd.getText().equals(EMPTY_STRING))
            throw new ValidationException(END_EMPTY_ERR);
        
        try {
            Date start = formatter.parse(this.txtStart.getText());
            Date end = formatter.parse(this.txtEnd.getText());
            
            if(start.after(end) || start.equals(end))
                throw new ValidationException(START_END_MISMATCH_ERR);
        } catch(ParseException ex) {
            throw new ValidationException(PARSE_ERR);
        }
        
        this.hideError();
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
