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
    
    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s", this.name, this.country);
    }
}
