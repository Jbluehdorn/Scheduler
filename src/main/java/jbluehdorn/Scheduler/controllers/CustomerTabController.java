package jbluehdorn.Scheduler.controllers;

import jbluehdorn.Scheduler.models.Address;
import jbluehdorn.Scheduler.models.City;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.repositories.AddressRepository;
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
public class CustomerTabController {
    public void initialize() {
        City city = new City("", "", 1, 1);
        
        try {
            Iterable<Customer> customers = CustomerRepository.get();
            System.out.println(customers);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
