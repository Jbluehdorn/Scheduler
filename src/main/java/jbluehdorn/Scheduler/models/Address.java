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
    private City city;
    
    public Address(int id, String address, String address2, String postalCode, String phone, City city) {
        this.id = id;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getAddress2() {
        return this.address2;
    }
    
    public void setAddress2(String address) {
        this.address2 = address;
    }
    
    public String getPostalCode() {
        return this.postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public City getCity() {
        return this.city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
    
    public String getCityName() {
        return this.city.getName();
    }
    
    public String getCountryName() {
        return this.city.getCountry();
    }
    
    @Override
    public String toString() {
        return String.format("%s %s, %s, %s, %s", this.address, this.address2, this.getCityName(), this.postalCode, this.getCountryName());
    }
}
