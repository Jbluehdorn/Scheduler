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
public class Customer {
    private int id;
    private String name;
    private Address address;
    
    public Customer(int id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Address getAddress() {
        return this.address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
