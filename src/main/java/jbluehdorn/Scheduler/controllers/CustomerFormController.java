package jbluehdorn.Scheduler.controllers;

import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javax.xml.bind.ValidationException;
import jbluehdorn.Scheduler.controllers.partials.AddressController;
import jbluehdorn.Scheduler.models.Address;
import jbluehdorn.Scheduler.models.City;
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
public class CustomerFormController {
    //Components
    @FXML
    Label lblError;
    @FXML
    TextField txtName;
    @FXML
    private AddressController partialAddressController;
    
    //Data
    private Address add;
    private static Customer customerToEdit;
    
    //Constants
    private final StageManager stageManager;
    private static final String EMPTY_STRING = "";
    private static final String NAME_ERR_MSG = "Name is required";
    private static final String ADDRESS_ERR_MSG = "Address is invalid";
    private static final String DB_ERR_MSG = "Failed to save properly";
    
    @Autowired @Lazy
    public CustomerFormController(StageManager stageManager) {
        this.stageManager = stageManager;
    }
    
    public void initialize() {
        if(customerToEdit != null)
            this.populateEditForm();
    }
    
    public static void setCustomerToEdit(Customer customer) {
        customerToEdit = customer;
    }
    
    @FXML
    public void btnSavePressed() {
        try {
            this.validateForm();
            
            if(customerToEdit != null) {
                if(this.updateCustomerFromForm()) {
                    this.clearEditForm();
                    this.stageManager.switchScene(FxmlView.SCHEDULER);
                }
            } else {
                if(this.newCustomerFromForm() != null) {
                    this.stageManager.switchScene(FxmlView.SCHEDULER);
                }
            }
        } catch(ValidationException ex) {
            this.showError(ex.getMessage());
        }
    }
    
    @FXML
    public void btnCancelPressed() {
        this.clearEditForm();
        
        this.stageManager.switchScene(FxmlView.SCHEDULER);
    }
    
    public void clearEditForm() {
        customerToEdit = null;

        this.partialAddressController.clearEditForm();
        this.txtName.setText(EMPTY_STRING);
    }
    
    /***
     * Validate the form and then clear the error label if so
     * 
     * @throws ValidationException 
     */
    private void validateForm() throws ValidationException {
        if(this.txtName.getText().equals(EMPTY_STRING))
            throw new ValidationException(NAME_ERR_MSG);
        
        this.setAddress();
        
        this.hideError();
    }
    
    private void setAddress() throws ValidationException {
        this.add = this.partialAddressController.getAddress();
        
        if(this.add == null)
            throw new ValidationException(ADDRESS_ERR_MSG);
    }
    
    private void populateEditForm() {
        this.txtName.setText(customerToEdit.getName());
        
        this.partialAddressController.populateEditForm(customerToEdit.getAddress());
    }
    
    private Customer newCustomerFromForm() {
        try {
            return CustomerRepository.create(
                    this.txtName.getText(),
                    this.add
            );
        } catch(SQLException ex) {
            Logger.error(ex.getMessage());
            this.showError(DB_ERR_MSG);
            return null;
        }
    }
    
    private Boolean updateCustomerFromForm() {
        try {
            customerToEdit.setName(this.txtName.getText());
            customerToEdit.setAddress(this.partialAddressController.getAddress());
            
            return CustomerRepository.update(customerToEdit);
        } catch(SQLException ex) {
            Logger.error(ex.getMessage());
            this.showError(DB_ERR_MSG);
            return false;
        }
    }
    
    private void showError(String error) {
        this.lblError.setText(error);
        this.lblError.setVisible(true);
    }
    
    private void hideError() {
        this.lblError.setText(EMPTY_STRING);
        this.lblError.setVisible(false);
    }
}
