package jbluehdorn.Scheduler.repositories;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import jbluehdorn.Scheduler.models.Address;
import jbluehdorn.Scheduler.models.City;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.util.DB;
import jbluehdorn.Scheduler.repositories.AddressRepository;

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
    
    public static Customer getById(int id) throws SQLException {
        String query = "SELECT * FROM customer WHERE customerId = " + id;
        
        ResultSet rs = DB.ExecQuery(query);
        
        if(rs.next())
            return createCustomer(rs);
        
        return null;
    }
    
    /***
     * Create and save a new Customer
     * 
     * @param name
     * @param address
     * @return new Customer
     * @throws SQLException 
     */
    public static Customer create(String name, Address address) throws SQLException {
        String createdBy = UserRepository.getCurrentUser().getUserName();
        int id = generateUniqueId();
        
        //Get connection
        Connection con = DB.getCon();
        
        //Calendar for date objects
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTime().getTime());
        
        //Query to run
        String query = "INSERT INTO customer (customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?)";
        
        //Create statement
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.setString(2, name);
        stmt.setInt(3, address.getId());
        stmt.setBoolean(4, true);
        stmt.setDate(5, now);
        stmt.setString(6, createdBy);
        stmt.setDate(7, now);
        stmt.setString(8, createdBy);
        
        //Execute and return
        if(stmt.executeUpdate() > 0) {
            updateAllCustomers();
            return getById(id);
        }
        
        return null;
    }
    
    /***
     * Create and save a new customer and address
     * 
     * @param name
     * @param address
     * @param address2
     * @param city
     * @param postalCode
     * @param phone
     * @return new Customer
     * @throws SQLException 
     */
    public static Customer create(String name, String address, String address2, City city, String postalCode, String phone) throws SQLException {
        Address customer_add = AddressRepository.create(address, address2, city, postalCode, phone);
        
        return create(name, customer_add);
    }
    
    public static Boolean update(Customer customer) throws SQLException {
        //Get Connection
        Connection con = DB.getCon();
        
        //Calendar for date objects
        Calendar cal = Calendar.getInstance();
        Timestamp now = new Timestamp(cal.getTime().getTime());
        
        //Query to run
        String query = "UPDATE customer "
                + "SET customerName = ?, addressId = ? "
                + "WHERE customerId = ?";
        
        //Create statement
        PreparedStatement stmt = con.prepareCall(query);
        stmt.setString(1, customer.getName());
        stmt.setInt(2, customer.getAddress().getId());
        stmt.setInt(3, customer.getId());
        
        //Execute and return
        if(stmt.executeUpdate() > 0) {
            updateAllCustomers();
            return true;
        }
        
        return false;
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
     * Get all customerIds
     * 
     * @return Iterable<Integer>
     * @throws SQLException 
     */
    private static Iterable<Integer> getIds() throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        
        String query = "SELECT customerId FROM customer";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            ids.add(rs.getInt("customerId"));
        }
        
        return ids;
    }
    
    /***
     * Generate a unique identifier for each customer
     * 
     * @return ID
     * @throws SQLException 
     */
    private static int generateUniqueId() throws SQLException {
        Random r = new Random(System.currentTimeMillis());
        Boolean exists = false;
        Integer newId = 10000 + r.nextInt(90000);
        Iterable<Integer> ids = getIds();
        
        do {
            for(Integer id: ids) {
                if(newId.equals(id)) {
                    exists = true;
                    newId = 10000 + r.nextInt(90000);
                }
            }
        } while(exists);
        
        return newId;
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
