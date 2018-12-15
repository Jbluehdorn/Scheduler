package jbluehdorn.Scheduler.controllers;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.xml.bind.ValidationException;
import jbluehdorn.Scheduler.models.Appointment;
import jbluehdorn.Scheduler.repositories.AppointmentRepository;
import jbluehdorn.Scheduler.util.Logger;
import jbluehdorn.Scheduler.view.FxmlView;
import jbluehdorn.Scheduler.view.StageManager;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

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
public class AppointmentTabController {
    //FXML Components
    @FXML
    private TableView tblAppointments;
    @FXML
    private TableColumn<Appointment, String> colCust;
    @FXML
    private TableColumn<Appointment, String> colTitle;
    @FXML
    private TableColumn<Appointment, String> colLocation;
    @FXML
    private TableColumn<Appointment, String> colStart;
    @FXML
    private TableColumn<Appointment, String> colEnd;
    @FXML
    private ComboBox cmbGranularity;
    
    //Constants
    private final StageManager stageManager;
    
    @Autowired @Lazy
    public AppointmentTabController(StageManager stageManager) {
        this.stageManager = stageManager;
    }
    
    public void initialize() {
        this.mapColumns();
        this.cmbGranularity.setValue("All");
        this.populateTable(this.getGranularity());
    }
    
    @FXML
    public void cmbGranularityChanged() {
        this.populateTable(this.getGranularity());
    }
    
    @FXML
    public void btnAddPressed() {
        this.stageManager.switchScene(FxmlView.APPOINTMENT_FORM);
    }
    
    @FXML
    public void btnModPressed() {
        try {
            Appointment app = this.getSelectedAppointment();
            
            AppointmentFormController.setAppointmentToEdit(app);
            
            this.stageManager.switchScene(FxmlView.APPOINTMENT_FORM);
        } catch(ValidationException ex) {
            this.showError(ex.getMessage());
        }
    }
    
    @FXML
    public void btnDelPressed() {
        try {
            Appointment app = this.getSelectedAppointment();
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Are you sure you want to delete " + app.getTitle() + "?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK) {
                AppointmentRepository.delete(app.getId());
                this.populateTable(this.getGranularity());
            }
        } catch(ValidationException ex) {
            this.showError(ex.getMessage());
        } catch(SQLException ex) {
            Logger.error(ex.getMessage());
            this.showError("Delete was unsuccessful");
        }
    }
    
    private AppointmentRepository.Granularity getGranularity() {
        switch(this.cmbGranularity.getSelectionModel().getSelectedItem().toString()) {
            case "Today":
                return AppointmentRepository.Granularity.TODAY;
            case "This Week":
                return AppointmentRepository.Granularity.THIS_WEEK;
            case "This Month":
                return AppointmentRepository.Granularity.THIS_MONTH;
            case "All":
                return AppointmentRepository.Granularity.ALL;
            default:
                return AppointmentRepository.Granularity.ALL;
        }
    }
    
    /***
     * Map the table columns to the appropriate Appointment fields
     */
    private void mapColumns() {
        DateFormat format = new SimpleDateFormat("M/dd h:mm a");
        
        colCust.setCellValueFactory(app -> new SimpleStringProperty(app.getValue().getCustomer().getName()));
        colTitle.setCellValueFactory(app -> new SimpleStringProperty(app.getValue().getTitle()));
        colLocation.setCellValueFactory(app -> new SimpleStringProperty(app.getValue().getLocation()));
        colStart.setCellValueFactory(app -> new SimpleStringProperty(format.format(app.getValue().getStartDate())));
        colEnd.setCellValueFactory(app -> new SimpleStringProperty(format.format(app.getValue().getEndDate())));
    }
    
    /***
     * Get appointments and throw them in the table
     */
    private void populateTable(AppointmentRepository.Granularity granularity) {
        try {
            Iterable<Appointment> apps = AppointmentRepository.get(granularity);
            
            ObservableList<Appointment> appsObs = FXCollections.observableArrayList((ArrayList) apps);
            
            tblAppointments.setItems(appsObs);
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private Appointment getSelectedAppointment() throws ValidationException {
        Appointment app = (Appointment) this.tblAppointments.getSelectionModel().getSelectedItem();
        
        if(app == null)
            throw new ValidationException("No appointment selected");
        
        return app;
    }
    
    private void showError(String error) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setHeaderText(error);
        alert.showAndWait();
    }
}
