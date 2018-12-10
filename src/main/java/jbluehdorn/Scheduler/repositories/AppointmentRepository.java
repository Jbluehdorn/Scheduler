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
    
    public static Iterable<Appointment> get() throws SQLException {
        if(allAppointments.isEmpty())
            updateAllAppointments();
        
        return allAppointments;
    }
    
    private static void updateAllAppointments() throws SQLException {
        allAppointments.clear();
        
        String query = "SELECT * FROM appointment";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            allAppointments.add(createAppointment(rs));
        }
    }
    
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
