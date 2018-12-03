package jbluehdorn.Scheduler.controllers.customer;

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
    public void initialize() {
        City city = new City(1, 1);
        try {
            Customer customer = CustomerRepository.create("George", "1928 South Street", "Apartment 2", city, "65742", "(808)291-0218");
            System.out.println(customer);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
