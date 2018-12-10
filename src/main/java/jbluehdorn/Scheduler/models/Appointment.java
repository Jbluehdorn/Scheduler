/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.models;

import java.util.Date;

/**
 *
 * @author Jordan
 */
public class Appointment {
    private int id;
    private Customer customer;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private Date start;
    private Date end;
    
    public Appointment(int id, String title, String description, String location, String contact, String url, Customer customer, Date start, Date end) {
        this.id = id;
        this.customer = customer;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
    }
    
    public int getId() { return this.id; }
    public Customer getCustomer() { return this.customer; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getLocation() { return this.location; }
    public String getContact() { return this.contact; }
    public String getUrl() { return this.url; }
    public Date getStartDate() { return this.start; }
    public Date getEndDate() { return this.end; }
    
    @Override
    public String toString() {
        return String.format("%s for %s on %s", this.title, this.customer.getName(), this.start);
    }
}
