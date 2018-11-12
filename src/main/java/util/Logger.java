/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileWriter;
import java.io.IOException;
/**
 *
 * @author Jordan
 */
public class Logger {
    private static final String location = "logs/log.txt";
    
    public static void log(String message) {
        try {
            FileWriter fw = new FileWriter(location, true);
            
            fw.write("LOG: " + message + "\n");
            
            fw.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void error(String message) {
        try {
            FileWriter fw = new FileWriter(location, true);
            
            fw.write("ERROR: " + message + "\n");
            
            fw.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
