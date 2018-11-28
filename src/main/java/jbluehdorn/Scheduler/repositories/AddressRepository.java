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
    
    /**
     * Creates city object from ResultSet
     * 
     * @param rs
     * @return models.City
     */
    private static City createCity(ResultSet rs) throws SQLException {
        return new City(
                rs.getString("city"),
                rs.getString("country")
        );
    }
}
