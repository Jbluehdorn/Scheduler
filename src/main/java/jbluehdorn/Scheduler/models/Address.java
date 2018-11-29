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
public class Address {
    private int id;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private String city;
    private String country;
    
    public Address(int id, String address, String address2, String postalCode, String phone, City city) {
        this.id = id;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city.getName();
        this.country = city.getCountry();
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public String getAddress2() {
        return this.address2;
    }
    
    public String getPostalCode() {
        return this.postalCode;
    }
    
    public String getPhone() {
        return this.phone;
    }
    
    public String getCity() {
        return this.city;
    }
    
    public String getCountry() {
        return this.country;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s, %s, %s, %s", this.address, this.address2, this.city, this.postalCode, this.country);
    }
}
