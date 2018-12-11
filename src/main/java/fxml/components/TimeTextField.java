package fxml.components;

import java.awt.Toolkit;
import javafx.scene.control.TextField;

public class TimeTextField extends TextField 
{   
    public TimeTextField(String init) {
        super(init) ;
        focusedProperty().addListener((o, oldV, newV) -> changed(newV));
    }

    public TimeTextField() {
        this("12:00 PM");
    }

    private void changed(boolean focus) {
        if (!focus) {
            if (!validate()) { 
                setText("12:00 PM");
                selectAll();
                requestFocus();
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public boolean validate()  {
        return getText()
        .matches("(0?[1-9]|1[0-2]):[0-5][0-9](:[0-5][0-9])? ?[APap][mM]$");
    }    
}