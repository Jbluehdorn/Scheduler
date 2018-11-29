package jbluehdorn.Scheduler.controllers;

import jbluehdorn.Scheduler.models.Address;
import jbluehdorn.Scheduler.models.City;
import jbluehdorn.Scheduler.repositories.AddressRepository;
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
            Iterable<Address> addresses = AddressRepository.get();
            for(Address add : addresses) {
                add.setAddress2("Home");
                AddressRepository.update(add);
            }
            
            System.out.println(AddressRepository.get());
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
