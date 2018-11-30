package jbluehdorn.Scheduler.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jbluehdorn.Scheduler.models.Address;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.util.DB;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jordan
 */
public class CustomerRepository {
    private static ArrayList<Customer> allCustomers = new ArrayList<>();
    
    /***
     * Get all customers
     * 
     * @return Iterable<Customer>
     * @throws SQLException 
     */
    public static Iterable<Customer> get() throws SQLException {
        if(allCustomers.isEmpty())
            updateAllCustomers();
        
        return allCustomers;
    }
    
    /***
     * Update the allCustomers object
     * 
     * !!THIS MUST BE RUN EVERY TIME AN UPDATE IS MADE!!
     * 
     * @throws SQLException 
     */
    private static void updateAllCustomers() throws SQLException {
        allCustomers.clear();
        
        //Only get records that aren't soft deleted
        String query = "SELECT * FROM customer WHERE active = true";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            allCustomers.add(createCustomer(rs));
        }
    }
    
    /***
     * Create a single customer from a ResultSet
     * 
     * @param rs
     * @return Customer
     * @throws SQLException 
     */
    private static Customer createCustomer(ResultSet rs) throws SQLException {
        Address address = AddressRepository.getById(rs.getInt("addressId"));
        
        return new Customer(
                rs.getInt("customerId"),
                rs.getString("customerName"),
                address
        );
    }
}
