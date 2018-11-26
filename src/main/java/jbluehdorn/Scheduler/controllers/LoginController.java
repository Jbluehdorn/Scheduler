/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.controllers;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jbluehdorn.Scheduler.util.Logger;
import jbluehdorn.Scheduler.view.StageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


/**
 *
 * @author Jordan
 */
@Component
public class LoginController implements FxmlController {

    //FXML FIELDS BASED ON ID
    @FXML
    private TextField txtUser;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblPassword;
    
    //CONSTANTS
    private final StageManager stageManager;
    
    @Autowired @Lazy
    public LoginController(StageManager stageManager) {
        this.stageManager = stageManager;
    }
    
    public void initialize() {
        Locale locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
        
        this.setText(locale);
    }
    
    @FXML
    public void btnLoginPressed(ActionEvent event) {
        
    }
    
    private void setText(Locale locale) {
        ResourceBundle rb = ResourceBundle.getBundle("login", locale);
        
        lblUser.setText(rb.getString("username") + ":");
        lblPassword.setText(rb.getString("password") + ":");
        btnLogin.setText(rb.getString("login"));
        lblStatus.setText(rb.getString("error"));
        
        Logger.log("Idk");
    }
    
}