/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.view;

import java.util.ResourceBundle;

/**
 *
 * @author Jordan
 */
public enum FxmlView {
    SCHEDULER {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("scheduler.title");
        }
        
        @Override
        String getFxmlFile() {
            return "/fxml/Scheduler.fxml";
        }
    }, 
    LOGIN {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/fxml/Login.fxml";
        }
        
    },
    CUSTOMER_ADD {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("customer_add.title");
        }
        
        @Override
        String getFxmlFile() {
            return "/fxml/customer/Add.fxml";
        }
    };
    
    abstract String getTitle();
    abstract String getFxmlFile();

    String getStringFromResourceBundle(String key) {
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
