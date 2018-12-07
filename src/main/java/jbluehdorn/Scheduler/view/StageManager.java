/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.view;

import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jbluehdorn.Scheduler.spring.config.SpringFXMLLoader;
import jbluehdorn.Scheduler.util.Logger;
/**
 *
 * @author Jordan
 */
public class StageManager {
    private final Stage primaryStage;
    private final SpringFXMLLoader springFXMLLoader;
    
    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }
    
    public void switchScene(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }
    
    public void newWindow(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        showNewWindow(viewRootNodeHierarchy, view.getTitle());
    }
    
    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);
        
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        
        try {
            primaryStage.show();
        } catch(Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void showNewWindow(final Parent rootnode, String title) {
        Scene scene = new Scene(rootnode);
        
        Stage newWindow = new Stage();
        newWindow.setTitle(title);
        newWindow.setScene(scene);
        newWindow.centerOnScreen();
        
        try {
            newWindow.show();
        } catch(Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private Scene prepareScene(Parent rootnode) {
        Scene scene = primaryStage.getScene();
        
        if(scene == null) {
            scene = new Scene(rootnode);
        }
        
        scene.setRoot(rootnode);
        
        return scene;
    }
    
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML must not be null");
        }catch(Exception ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        
        return rootNode;
    }
}
