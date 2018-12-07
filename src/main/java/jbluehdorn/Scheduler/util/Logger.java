/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.util;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
/**
 *
 * @author Jordan
 */
public class Logger {
    private static final String location = "logs/";
    private static final String logFileName = "logs.txt";
    private static final String loginFileName = "logins.txt";
    
    public static void log(String message) {
        try {
            FileWriter fw = new FileWriter(location + logFileName, true);
            
            fw.write("LOG: " + message + "\n");
            
            fw.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void error(String message) {
        try {
            FileWriter fw = new FileWriter(location + logFileName, true);
            
            fw.write("ERROR: " + message + "\n");
            
            fw.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void login(String username) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        try {
            FileWriter fw = new FileWriter(location + loginFileName, true);
            
            fw.write(username + ": " + now.toString());
            
            fw.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
