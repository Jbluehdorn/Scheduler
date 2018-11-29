/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.models;

/**
 *
 * @author Jordan
 */
public class City {
    private String name;
    private String country;
    private int id;
    private int countryId;
    
    public City(String name, String country, int id, int countryId) {
        this.name = name;
        this.country = country;
        this.id = id;
        this.countryId = countryId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getCountryId() {
        return this.countryId;
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s", this.name, this.country);
    }
}
