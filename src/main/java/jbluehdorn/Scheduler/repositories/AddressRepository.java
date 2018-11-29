package jbluehdorn.Scheduler.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.Random;
import jbluehdorn.Scheduler.models.Address;
import jbluehdorn.Scheduler.models.City;
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
public class AddressRepository {
    private static ArrayList<Address> allAddresses;
    
    /***
     * Retrieve all addresses
     * 
     * @return Iterable<Address>
     * @throws SQLException 
     */
    public static Iterable<Address> get() throws SQLException {
        if(allAddresses == null)
            updateAllAddresses();
        
        return allAddresses;
    }
    
    /***
     * Create and save an address
     * 
     * @param address
     * @param address2
     * @param city
     * @param postalCode
     * @param phone
     * @return
     * @throws SQLException 
     */
    public static Boolean create(String address, String address2, City city, String postalCode, String phone) throws SQLException {
        String createdBy = UserRepository.getCurrentUser().getUserName();
        
        //Get connection
        Connection con = DB.getCon();
        
        //Calendar for date objects
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTime().getTime());
        
        //Query to run
        String query = "INSERT INTO address (addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        //Create statement
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, generateUniqueId());
        stmt.setString(2, address);
        stmt.setString(3, address2);
        stmt.setInt(4, city.getId());
        stmt.setString(5, postalCode);
        stmt.setString(6, phone);
        stmt.setDate(7, now);
        stmt.setString(8, createdBy);
        stmt.setDate(9, now);
        stmt.setString(10, createdBy);
        
        //Execute and return
        int count = stmt.executeUpdate();
        if(count > 0)
            updateAllAddresses();
        
        return count > 0;
    }
    
    /***
     * Get all cities from the database
     * 
     * @return Iterable<City>
     * @throws SQLException 
     */
    public static Iterable<City> getCities() throws SQLException {
        ArrayList<City> cities = new ArrayList();
        
        String query = "select city.city, country.country, city.cityId, country.countryId "
                + "from city "
                + "inner join country "
                + "on city.countryId = country.countryId";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            cities.add(createCity(rs));
        }
        
        return cities;
    }
    
    /***
     * Updates the static allAddresses Iterable
     * 
     * !!THIS MUST BE RUN ANY TIME AN UPDATE IS MADE TO ADDRESSES!!
     * 
     * @throws SQLException 
     */
    private static void updateAllAddresses() throws SQLException {
        allAddresses = new ArrayList<>();
        
        String query = "SELECT a.addressId, a.address, a.address2, a.postalCode, a.phone, city.city, city.cityId, country.country, country.countryId "
                + "FROM address AS a "
                + "INNER JOIN city "
                + "ON a.cityId = city.cityId "
                + "INNER JOIN country "
                + "ON city.countryId = country.countryId";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            allAddresses.add(createAddress(rs));
        }
    }
    
    /***
     * Get all addressIds
     * 
     * @return Iterable<Integer>
     * @throws SQLException 
     */
    private static Iterable<Integer> getIds() throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        
        String query = "SELECT addressId FROM address";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            ids.add(rs.getInt("addressID"));
        }
        
        return ids;
    }
    
    /***
     * Generates a unique identifier for each address
     * 
     * @return ID
     * @throws SQLException 
     */
    private static int generateUniqueId() throws SQLException {
        Random r = new Random(System.currentTimeMillis());
        Boolean exists = false;
        Integer newId = 10000 + r.nextInt(20000);
        Iterable<Integer> ids = getIds();
        
        //Check for existing id and adjust if so
        do {
            for(Integer id : ids) {
                if(newId.equals(id)) {
                    exists = true;
                    newId = 10000 + r.nextInt(20000);
                }
            }
        } while(exists);
        
        return newId;
    }
        
    
    /**
     * Creates city object from ResultSet
     * 
     * @param rs
     * @return models.City
     * @throws SQLException
     */
    private static City createCity(ResultSet rs) throws SQLException {
        return new City(
                rs.getString("city"),
                rs.getString("country"),
                rs.getInt("cityId"),
                rs.getInt("countryId")
        );
    }
    
    /***
     * Creates address object from ResultSet
     * 
     * @param rs
     * @return models.city
     * @throws SQLException 
     */
    private static Address createAddress(ResultSet rs) throws SQLException {
        City city = new City(rs.getString("city"), rs.getString("country"), rs.getInt("cityId"), rs.getInt("countryId"));
        
        return new Address(
                rs.getInt("addressId"),
                rs.getString("address"),
                rs.getString("address2"),
                rs.getString("postalCode"),
                rs.getString("phone"),
                city
        );
    }
}