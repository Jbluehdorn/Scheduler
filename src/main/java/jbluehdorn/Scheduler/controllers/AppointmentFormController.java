/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.controllers;

import fxml.components.TimeTextField;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.xml.bind.ValidationException;
import jbluehdorn.Scheduler.models.Appointment;
import jbluehdorn.Scheduler.models.Customer;
import jbluehdorn.Scheduler.repositories.AppointmentRepository;
import jbluehdorn.Scheduler.repositories.CustomerRepository;
import jbluehdorn.Scheduler.util.Logger;
import jbluehdorn.Scheduler.view.FxmlView;
import jbluehdorn.Scheduler.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jordan
 */
@Component
public class AppointmentFormController {
    //FXML Components
    @FXML
    private Label lblError;
    @FXML
    private TextField txtTitle;
    @FXML
    private ComboBox cmbCustomer;
    @FXML
    private TextArea txtDesc;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtContact;
    @FXML
    private DatePicker pickerDate;
    @FXML
    private TimeTextField txtStart;
    @FXML
    private TimeTextField txtEnd;
    
    //Data
    private static Appointment appointmentToEdit;
    
    //Constants
    private final StageManager stageManager;
    private final String EMPTY_STRING = "";
    private final String TITLE_EMPTY_ERR = "Title is required";
    private final String CUSTOMER_EMPTY_ERR = "Customer is required";
    private final String LOCATION_EMPTY_ERR = "Location is required";
    private final String DATE_EMPTY_ERR = "Date is required";
    private final String START_EMPTY_ERR = "Start time is required";
    private final String END_EMPTY_ERR = "End time is required";
    private final String START_END_MISMATCH_ERR = "End time cannot be before start time";
    private final String PARSE_ERR = "Start or end time could not be parsed";
    private final String DB_ERR = "Failed to save properly";
    
    @Autowired @Lazy
    public AppointmentFormController(StageManager stageManager) {
        this.stageManager = stageManager;
    }
    
    public void initialize() {
        this.populateCustomerCombo();
        this.constrainDates();
        
        if(appointmentToEdit != null)
            this.populateEditForm();
    }
    
    public static void setAppointmentToEdit(Appointment appointment) {
        appointmentToEdit = appointment;
    }
    
    @FXML
    public void btnSavePressed() {
        try {
            this.validateForm();
            
            if(appointmentToEdit != null) {
                if(this.updateAppointmentFromForm()) {
                    this.stageManager.switchScene(FxmlView.SCHEDULER);
                }
            } else {
                if(this.newAppointmentFromForm() != null) {
                    this.stageManager.switchScene(FxmlView.SCHEDULER);
                }
            }
            
        } catch(ValidationException ex) {
            Logger.error(ex.getMessage());
            this.showError(ex.getMessage());
        }
    }
    
    @FXML
    public void btnCancelPressed() {
        appointmentToEdit = null;
        
        this.stageManager.switchScene(FxmlView.SCHEDULER);
    }
    
    private void constrainDates() {
        this.pickerDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                
                setDisable(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                        date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                        date.compareTo(today) < 0 ||
                        empty);
            }
        });
    }
    
    private void populateEditForm() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            
        this.txtTitle.setText(appointmentToEdit.getTitle());
        this.cmbCustomer.setValue(appointmentToEdit.getCustomer());
        this.txtDesc.setText(appointmentToEdit.getDescription());
        this.txtLocation.setText(appointmentToEdit.getLocation());
        this.txtContact.setText(appointmentToEdit.getContact());
        this.pickerDate.setValue(appointmentToEdit.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        this.txtStart.setText(formatter.format(appointmentToEdit.getStartDate()));
        this.txtEnd.setText(formatter.format(appointmentToEdit.getEndDate()));
    }
    
    private void populateCustomerCombo() {
        try {
            Iterable<Customer> customers = CustomerRepository.get();
            
            ObservableList<Customer> customersObs = FXCollections.observableArrayList((ArrayList<Customer>) customers);
            
            this.cmbCustomer.setItems(customersObs);
        } catch(Exception ex) {
            Logger.error(ex.getMessage());
            this.showError(ex.getMessage());
        }
    }
    
    private Appointment newAppointmentFromForm() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            Date day = java.sql.Date.valueOf(this.pickerDate.getValue());
            
            //Adjust time for offset
            TimeZone tz = TimeZone.getDefault();
            long offset = tz.getOffset(day.getTime());
            
            Date start = new Date(day.getTime() + formatter.parse(this.txtStart.getText()).getTime() + offset);
            Date end = new Date(day.getTime() + formatter.parse(this.txtEnd.getText()).getTime() + offset);
            
            return AppointmentRepository.create(
                    this.txtTitle.getText(),
                    this.txtDesc.getText(),
                    this.txtLocation.getText(),
                    this.txtContact.getText(),
                    EMPTY_STRING,
                    (Customer) this.cmbCustomer.getSelectionModel().getSelectedItem(),
                    start,
                    end
            );
        } catch(SQLException ex) {
            Logger.error(ex.getMessage());
            this.showError(DB_ERR);
            return null;
        } catch(ParseException ex) {
            this.showError(PARSE_ERR);
            return null;
        }
    }
    
    private Boolean updateAppointmentFromForm() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
            Date day = java.sql.Date.valueOf(this.pickerDate.getValue());
            
            //Adjust time for offset
            TimeZone tz = TimeZone.getDefault();
            long offset = tz.getOffset(day.getTime());
            
            Date start = new Date(day.getTime() + formatter.parse(this.txtStart.getText()).getTime() + offset);
            Date end = new Date(day.getTime() + formatter.parse(this.txtEnd.getText()).getTime() + offset);
            
            appointmentToEdit.setTitle(this.txtTitle.getText());
            appointmentToEdit.setCustomer((Customer) this.cmbCustomer.getValue());
            appointmentToEdit.setDescription(this.txtDesc.getText());
            appointmentToEdit.setLocation(this.txtLocation.getText());
            appointmentToEdit.setContact(this.txtContact.getText());
            appointmentToEdit.setStartDate(start);
            appointmentToEdit.setEndDate(end);
            
            return AppointmentRepository.update(appointmentToEdit);
        } catch(SQLException ex) {
            Logger.error(ex.getMessage());
            this.showError(DB_ERR);
            return false;
        } catch(ParseException ex) {
            this.showError(PARSE_ERR);
            return false;
        }
    }
    
    private void validateForm() throws ValidationException {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        
        if(this.txtTitle.getText().equals(EMPTY_STRING))
            throw new ValidationException(TITLE_EMPTY_ERR);
        
        if(this.cmbCustomer.getValue() == null) 
            throw new ValidationException(CUSTOMER_EMPTY_ERR);
        
        if(this.txtLocation.getText().equals(EMPTY_STRING))
            throw new ValidationException(LOCATION_EMPTY_ERR);
        
        if(this.pickerDate.getValue() == null)
            throw new ValidationException(DATE_EMPTY_ERR);
        
        if(this.txtStart.getText().equals(EMPTY_STRING))
            throw new ValidationException(START_EMPTY_ERR);
        
        if(this.txtEnd.getText().equals(EMPTY_STRING))
            throw new ValidationException(END_EMPTY_ERR);
        
        try {
            Date start = formatter.parse(this.txtStart.getText());
            Date end = formatter.parse(this.txtEnd.getText());
            
            if(start.after(end) || start.equals(end))
                throw new ValidationException(START_END_MISMATCH_ERR);
        } catch(ParseException ex) {
            throw new ValidationException(PARSE_ERR);
        }
        
        this.hideError();
    }
    
    private void showError(String message) {
        this.lblError.setText(message);
        this.lblError.setVisible(true);
        this.lblError.setManaged(true);
    }
    
    private void hideError() {
        this.lblError.setText(EMPTY_STRING);
        this.lblError.setVisible(false);
        this.lblError.setManaged(false);
    }
}
