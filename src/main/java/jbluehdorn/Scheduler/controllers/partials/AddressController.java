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
    
    @FXML
    public void btnTestPressed() {
        System.out.println(this.validateForm());
    }
    
    /***
     * Validate the form and show the appropriate message
     * 
     * @return success
     */
    private Boolean validateForm() {
        //Validate address line 1 field
        if(this.txtAddress.getText().equals(EMPTY_STRING)) {
            this.showError(ADDRESS_ERROR_MSG);
            return false;
        }
        
        //Validate city field
        if(this.cmbCities.getValue() == null) {
            this.showError(CITY_ERROR_MSG);
            return false;
        }
        
        //Validate postal code field
        if(this.txtPostal.getText().equals(EMPTY_STRING)) {
            this.showError(POSTAL_ERROR_MSG);
            return false;
        }
        
        //Validate the phone field
        if(this.txtPhone.getText().equals(EMPTY_STRING)) {
            this.showError(PHONE_ERROR_MSG);
            return false;
        }
        
        this.hideError();
        return true;
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
