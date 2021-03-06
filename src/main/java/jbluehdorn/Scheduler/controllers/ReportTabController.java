package jbluehdorn.Scheduler.controllers;

import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import jbluehdorn.Scheduler.models.Appointment;
import jbluehdorn.Scheduler.models.City;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.repositories.AddressRepository;
import jbluehdorn.Scheduler.repositories.AppointmentRepository;
import jbluehdorn.Scheduler.repositories.CustomerRepository;
import org.springframework.stereotype.Component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jordan
 */
@Component
public class ReportTabController {
    //FXML Components
    @FXML
    private ComboBox cmbType;
    @FXML
    private TextArea txtReport;
    
    public void initialize() {
        this.initializeCmbType();
    }
    
    @FXML
    public void btnGeneratePressed() {
        switch(this.cmbType.getSelectionModel().getSelectedItem().toString()) {
            case "Appointments by Month":
                this.generateAppointmentsByMonthReport();
                break;
            case "Customer Schedules":
                this.generateCustomerSchedulesReport();
                break;
            case "Customers by City":
                System.out.println("Stuff");
                this.generateCustomersByCityReport();
                break;
        }
    }
    
    private void initializeCmbType() {
        this.cmbType.setValue(this.cmbType.getItems().get(0));
    }
    
    private void showError(String error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(error);
        alert.showAndWait();
    }
    
    private void generateAppointmentsByMonthReport() {
        try {
            Iterable<Appointment> apps = AppointmentRepository.get();
            StringBuilder bodyText = new StringBuilder();
            
            bodyText.append("Appoinments by Month\n\n");
            
            Calendar cal = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            
            for(int i = 0; i < 12; i++) {
                int monthlyCount = 0;
                StringBuilder monthlyString = new StringBuilder();
                
                for(Appointment app : apps) {
                    cal.setTime(app.getStartDate());
                    
                    if(cal.get(Calendar.MONTH) == i && cal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                        monthlyString.append(app.toString() + "\n");
                        monthlyCount++;
                    }
                }
                
                if(monthlyCount > 0) {
                    bodyText.append("---" + this.getMonthsForInt(i).toUpperCase() + "---\n");
                    bodyText.append(monthlyString.toString() + "\n");
                }
            }
            
            this.txtReport.setText(bodyText.toString());
            
        } catch(SQLException ex) {
            this.showError(ex.getMessage());
        }
    }
    
    private void generateCustomerSchedulesReport() {
        try {
            StringBuilder bodyText = new StringBuilder();
            bodyText.append("Appointments by Customer\n\n");
            
            Iterable<Appointment> apps = AppointmentRepository.get();
            Iterable<Customer> customers = CustomerRepository.get();
            
            for(Customer cust : customers) {
                int appointmentCount = 0;
                StringBuilder customerString = new StringBuilder();
                
                for(Appointment app : apps) {
                    if(app.getCustomer().getId() == cust.getId()) {
                        customerString.append(app.toString() + "\n");
                        appointmentCount++;
                    }
                }
                
                if(appointmentCount > 0) {
                    bodyText.append("---" + cust.getName().toUpperCase() + "---\n");
                    bodyText.append(customerString.toString() + "\n");
                }
            }
            
            this.txtReport.setText(bodyText.toString());
            
        } catch(SQLException ex) {
            this.showError(ex.getMessage());
        }
    }
    
    private void generateCustomersByCityReport() {
        try {
            StringBuilder bodyText = new StringBuilder();
            bodyText.append("Customers by City\n\n");
            
            Iterable<Customer> customers = CustomerRepository.get();
            Iterable<City> cities = AddressRepository.getCities();
            
            for(City city : cities) {
                int customerCount = 0;
                StringBuilder cityString = new StringBuilder();
                
                for(Customer customer : customers) {
                    if(customer.getAddress().getCity().getId() == city.getId()) {
                        cityString.append(customer.toString() + "\n");
                        customerCount++;
                    }
                }
                
                if(customerCount > 0) {
                    bodyText.append("---" + city.getName().toUpperCase() + "---\n");
                    bodyText.append(cityString + "\n");
                }
            }
            
            this.txtReport.setText(bodyText.toString());
            
        } catch(SQLException ex) {
            this.showError(ex.getMessage());
        }
    }
    
    private String getMonthsForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
}
