package jbluehdorn.Scheduler.controllers;

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
            System.out.println(AddressRepository.create("13289 NE 182nd St", "", city, "98072", "555-1234"));
            System.out.println(AddressRepository.get());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
