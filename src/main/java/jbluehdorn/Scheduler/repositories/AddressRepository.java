package jbluehdorn.Scheduler.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    
    public static Iterable<Address> get() throws SQLException {
        if(allAddresses == null)
            updateAllAddresses();
        
        return allAddresses;
    }
    
    /***
     * Get all cities from the database
     * 
     * @return Iterable<City>
     * @throws SQLException 
     */
    public static Iterable<City> getCities() throws SQLException {
        ArrayList<City> cities = new ArrayList();
        
        String query = "select city.city, country.country "
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
        
        String query = "SELECT a.addressId, a.address, a.address2, a.postalCode, a.phone, city.city, country.country "
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
                rs.getString("country")
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
        City city = new City(rs.getString("city"), rs.getString("country"));
        
        return new Address(
                rs.getInt("addressId"),
                rs.getString("address"),
                rs.getString("address2"),
                rs.getInt("postalCode"),
                rs.getString("phone"),
                city
        );
    }
}
