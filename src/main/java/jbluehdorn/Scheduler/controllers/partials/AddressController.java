/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.controllers.partials;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.xml.bind.ValidationException;
import jbluehdorn.Scheduler.models.Address;
import jbluehdorn.Scheduler.models.City;
import jbluehdorn.Scheduler.repositories.AddressRepository;
import jbluehdorn.Scheduler.util.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jordan
 */
@Component
public class AddressController {
    //Components
    @FXML
    ComboBox cmbCities;
    @FXML
    TextField txtAddress;
    @FXML
    TextField txtAddress2;
    @FXML
    TextField txtPostal;
    @FXML
    TextField txtPhone;
    @FXML
    Label lblError;
    
    //Constants
    private static final String EMPTY_STRING = "";
    private static final String ADDRESS_ERROR_MSG = "Address Line 1 is a required field";
    private static final String CITY_ERROR_MSG = "City is a required field";
    private static final String POSTAL_ERROR_MSG = "Zip code is a required field";
    private static final String PHONE_ERROR_MSG = "Phone is a required field";
    
    public void initialize() {
        this.populateCityCombo();
    }
    
    /**
     * Populates the cities ComboBox with all cities
     */
    private void populateCityCombo() {
        try {
            Iterable<City> cities = AddressRepository.getCities();
            
            ObservableList<City> citiesObs = FXCollections.observableArrayList((ArrayList<City>) cities);
            
            this.cmbCities.setItems(citiesObs);
        } catch(Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /***
     * Returns the address from the form
     * 
     * @return Address or null
     */
    public Address getAddress() {
        return this.newAddressFromForm();
    }
    
    /***
     * Validate the form and show the appropriate message
     * 
     * @return success
     */
    private void validateForm() throws ValidationException {
        //Validate address line 1 field
        if(this.txtAddress.getText().equals(EMPTY_STRING))
            throw new ValidationException(ADDRESS_ERROR_MSG);
        
        //Validate city field
        if(this.cmbCities.getValue() == null) 
            throw new ValidationException(CITY_ERROR_MSG);
        
        //Validate postal code field
        if(this.txtPostal.getText().equals(EMPTY_STRING))
            throw new ValidationException(POSTAL_ERROR_MSG);
        
        //Validate the phone field
        if(this.txtPhone.getText().equals(EMPTY_STRING)) 
            throw new ValidationException(PHONE_ERROR_MSG);
        
        this.hideError();
    }
    
    private Address newAddressFromForm() {
        //Validate the form
        try {
            this.validateForm();
        } catch(ValidationException ex) {
            this.showError(ex.getMessage());
            
            return null;
        }
        
        //Return a new address
        return new Address(
                this.txtAddress.getText(),
                this.txtAddress2.getText(),
                this.txtPostal.getText(),
                this.txtPhone.getText(),
                (City) this.cmbCities.getValue()
        );
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
