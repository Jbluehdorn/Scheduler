/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        if(allAppointments.isEmpty())
            updateAllAppointments();
        
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
        if(allAppointments.isEmpty())
            updateAllAppointments();
        
        //Lamba expression used here as predicate for search
        return allAppointments.stream()
                .filter(app -> app.getId() == id)
                .findFirst()
                .orElse(null);
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
                rs.getDate("start"),
                rs.getDate("end")
        );
    }
}
