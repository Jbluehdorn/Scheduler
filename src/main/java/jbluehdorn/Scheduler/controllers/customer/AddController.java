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
            Customer customer = CustomerRepository.create("Carl", "1517 Pine St", "", city, "72136", "(425)555-1519");
            System.out.println(customer);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
