package jbluehdorn.Scheduler.controllers.customer;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import jbluehdorn.Scheduler.controllers.partials.AddressController;
import jbluehdorn.Scheduler.models.City;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.repositories.CustomerRepository;
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
public class AddController {
    //Components
    @FXML
    private AddressController PartialAddressController;
    
    public void initialize() {
        
    }
}
