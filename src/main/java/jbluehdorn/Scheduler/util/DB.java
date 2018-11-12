/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.util;

import java.sql.*;

/**
 *
 * @author Jordan
 */
public class DB {
    private static final String server = "52.206.157.109";
    private static final String dbName = "U054Jk";
    private static final String userName = "U054Jk";
    private static final String password = "53688420127";
    
    public static Connection getCon() throws SQLException {
        String host = "jdbc:mysql://" + server + ":3306/" + dbName;
        
        Connection conn = DriverManager.getConnection(
                host,
                userName,
                password
        );
        
        return conn;
    }
    
    public static ResultSet ExecQuery(String query) throws SQLException {
        //Get the connection
        Connection conn = getCon();
        
        //Create the statement
        Statement stmt = conn.createStatement();
        
        //Execute and return
        return stmt.executeQuery(query);
    }
}
