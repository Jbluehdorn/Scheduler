/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;
import jbluehdorn.Scheduler.models.Appointment;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.util.DB;

/**
 *
 * @author Jordan
 */
public class AppointmentRepository {
    private static ArrayList<Appointment> allAppointments = new ArrayList<>();
    
    /***
     * Get all appointments
     * 
     * @return Iterable<Appointment>
     * @throws SQLException 
     */
    public static Iterable<Appointment> get() throws SQLException {
        updateIfEmpty();
        
        return allAppointments;
    }
    
    /***
     * Get a specific Appointment
     * 
     * @param id
     * @return Appointment
     * @throws SQLException 
     */
    public static Appointment getById(int id) throws SQLException {
        updateIfEmpty();
        
        //Lamba expression used here as predicate for search
        return allAppointments.stream()
                .filter(app -> app.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /***
     * Create a new Appointment record
     * 
     * @param title
     * @param description
     * @param location
     * @param contact
     * @param url
     * @param customer
     * @param start
     * @param end
     * @return the new Appointment
     * @throws SQLException 
     */
    public static Appointment create(String title, String description, String location, String contact, String url, Customer customer, Date start, Date end) throws SQLException {
        String createdBy = UserRepository.getCurrentUser().getUserName();
        int id = generateUniqueId();
        
        //Get connection
        Connection con = DB.getCon();
        
        //Calendar for Date Objects
        Calendar cal = Calendar.getInstance();
        java.sql.Date now = new java.sql.Date(cal.getTime().getTime());
        
        //Query to run
        String query = "INSERT INTO appointment (appointmentId, customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        //Create statement
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, id);
        stmt.setInt(2, customer.getId());
        stmt.setString(3, title);
        stmt.setString(4, description);
        stmt.setString(5, location);
        stmt.setString(6, contact);
        stmt.setString(7, url);
        stmt.setDate(8, new java.sql.Date(start.getTime()));
        stmt.setDate(9, new java.sql.Date(end.getTime()));
        stmt.setDate(10, now);
        stmt.setString(11, createdBy);
        stmt.setDate(12, now);
        stmt.setString(13, createdBy);
        
        //Execute and return
        if(stmt.executeUpdate() > 0) {
            updateAllAppointments();
            return getById(id);
        }
        
        return null;
    }
    
    /***
     * Update a single record
     * 
     * @param appointment
     * @return success
     * @throws SQLException 
     */
    public static Boolean update(Appointment appointment) throws SQLException {
        String updateBy = UserRepository.getCurrentUser().getUserName();
        
        //Get Connection
        Connection con = DB.getCon();
        
        //Calendar for date Objects
        Calendar cal = Calendar.getInstance();
        java.sql.Date now = new java.sql.Date(cal.getTime().getTime());
        
        //Query to run
        String query = "UPDATE appointment "
                + "SET customerId = ?, title = ?, description = ?, location = ?, contact = ?, url = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ? "
                + "WHERE appointmentId = ?";
        
        //Create statement
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, appointment.getCustomer().getId());
        stmt.setString(2, appointment.getTitle());
        stmt.setString(3, appointment.getDescription());
        stmt.setString(4, appointment.getLocation());
        stmt.setString(5, appointment.getContact());
        stmt.setString(6, appointment.getUrl());
        stmt.setDate(7, new java.sql.Date(appointment.getStartDate().getTime()));
        stmt.setDate(8, new java.sql.Date(appointment.getEndDate().getTime()));
        stmt.setDate(9, now);
        stmt.setString(10, updateBy);
        stmt.setInt(11, appointment.getId());
        
        //Execute and return
        if(stmt.executeUpdate() > 0) {
            updateAllAppointments();
            return true;
        }
        
        return false;
    }
    
    /***
     * Delete a single record
     * 
     * @param id
     * @return success
     * @throws SQLException 
     */
    public static Boolean delete(int id) throws SQLException {
        String query = "DELETE FROM appointment WHERE appointmentId = " + id;
        
        Connection con = DB.getCon();
        
        PreparedStatement stmt = con.prepareStatement(query);
        
        if(stmt.executeUpdate() > 0) {
            updateAllAppointments();
            return true;
        }
        
        return false;
    }
    
    /***
     * Updates the static allAppointments Iterable
     * 
     * !!THIS MUST BE RUN ANY TIME AN UPDATE IS MADE TO APPOINTMENTS!!
     * 
     * @throws SQLException 
     */
    private static void updateAllAppointments() throws SQLException {
        allAppointments.clear();
        
        String query = "SELECT * FROM appointment";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            allAppointments.add(createAppointment(rs));
        }
    }
    
    /***
     * Creates Appointment object from ResultSet
     * 
     * @param rs
     * @return Appointment
     * @throws SQLException 
     */
    private static Appointment createAppointment(ResultSet rs) throws SQLException {
        Customer customer = CustomerRepository.getById(rs.getInt("customerId"));
        
        return new Appointment(
                rs.getInt("appointmentId"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getString("contact"),
                rs.getString("url"),
                customer,
                rs.getTimestamp("start"),
                rs.getTimestamp("end")
        );
    }
    
    /***
     * Update the master list if empty
     * 
     * @throws SQLException 
     */
    private static void updateIfEmpty() throws SQLException {
        if(allAppointments.isEmpty())
            updateAllAppointments();
    }
    
    /***
     * Get all Appointment ids
     * 
     * @return Iterable of ids
     * @throws SQLException 
     */
    private static Iterable<Integer> getIds() throws SQLException {
        updateIfEmpty();
        
        return allAppointments.stream()
                .map(app -> app.getId())
                .collect(Collectors.toList());
    }
    
    /***
     * Generate a unique id
     * 
     * @return new id
     * @throws SQLException 
     */
    private static int generateUniqueId() throws SQLException {
        Random r = new Random(System.currentTimeMillis());
        Boolean exists = false;
        Integer newId = 10000 + r.nextInt(90000);
        Iterable<Integer> ids = getIds();
        
        do {
            for(Integer id : ids) {
                if(newId.equals(id)) {
                    exists = true;
                    newId = 10000 + r.nextInt(90000);
                }
            }
        } while(exists);
        
        return newId;
    }
}
