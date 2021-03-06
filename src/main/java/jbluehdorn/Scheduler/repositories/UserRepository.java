/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import jbluehdorn.Scheduler.models.User;
import jbluehdorn.Scheduler.util.DB;
import jbluehdorn.Scheduler.util.Hasher;

/**
 * Repository for User Model
 * 
 * @author Jordan
 */
public class UserRepository {
    private static User loggedInUser;
    
    /**
     * Get a single User
     * 
     * @param userID
     * @return models.User
     * @throws SQLException 
     */
    public static User getById(int userID) throws SQLException {
        String query = "select * from user where userId = " + userID;
        
        ResultSet rs = DB.ExecQuery(query);
            
        if(rs.next())
            return createUser(rs);
        else
            return null;
    }
    
    /**
     * Get all Users
     * 
     * @return ArrayList<models.User>
     * @throws SQLException 
     */
    public static Iterable<User> get() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        
        String query = "select * from user";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            users.add(createUser(rs));
        }
        
        return users;
    }
    
    /**
     * Save a new User
     * 
     * @param userName
     * @param password
     * @param createdBy
     * @return Boolean
     * @throws SQLException 
     */
    public static Boolean create(String userName, String password) throws SQLException {
        String createdBy = getCurrentUser().getUserName();
        
        //Get connection
        Connection con = DB.getCon();
        
        //Calendar for date objects
        Calendar cal = Calendar.getInstance();
        Timestamp now = new Timestamp(cal.getTime().getTime());
        
        //Query to run
        String query = "insert into user (userId, userName, password, active, createBy, createDate, lastUpdate, lastUpdatedBy)"
                + "values(?, ?, ?, ?, ?, ?, ?, ?)";
        
        //Create statement
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, generateUniqueId());
        stmt.setString(2, userName);
        stmt.setString(3, Hasher.hash(password));
        stmt.setBoolean(4, true);
        stmt.setString(5, createdBy);
        stmt.setTimestamp(6, now);
        stmt.setTimestamp(7, now);
        stmt.setString(8, createdBy);
        
        //Execute and return
        return stmt.execute();
    }
    
    /**
     * Get all userIds
     * 
     * @return Iterable<Integer>
     * @throws SQLException 
     */
    private static Iterable<Integer> getIds() throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        
        String query = "select userId from user";
        
        ResultSet rs = DB.ExecQuery(query);
        
        while(rs.next()) {
            ids.add(rs.getInt("userId"));
        }
        
        return ids;
    }
    
    /**
     * Check if a username and password match
     * 
     * @param username
     * @param password
     * @return Boolean for success
     * @throws SQLException 
     */
    public static Boolean validateCredentials(String username, String password) throws SQLException {
        String query = "select * from user where userName = '" + username + "'";
        
        ResultSet rs = DB.ExecQuery(query);
        
        if(rs.next()) {
            loggedInUser = createUser(rs);
            
            return Hasher.hash(password).equals(rs.getString("password"));
        }
        
        return false;
    }
    
    public static User getCurrentUser() {
        return loggedInUser;
    }
    
    /***
     * Generate a unique 5 digit id
     * 
     * @return Integer
     * @throws SQLException 
     */
    private static Integer generateUniqueId() throws SQLException {
        Random r = new Random(System.currentTimeMillis());
        Boolean exists = false;
        Integer newID = 10000 + r.nextInt(90000);
        Iterable<Integer> ids = getIds();
        
        //check for existing id and adjust accordingly
        do {
            for(Integer id : ids) {
                if(newID.equals(id)) {
                    exists = true;
                    newID = 10000 + r.nextInt(90000);
                }
            }
        } while(exists);
        
        return newID;
    }
    
    /**
     * Create a local User object from a java.SQL.ResultSet
     * 
     * @param rs
     * @return models.User
     * @throws SQLException 
     */
    private static User createUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("userId"),
                rs.getString("userName"),
                rs.getString("password"),
                rs.getBoolean("active"),
                rs.getDate("createDate"),
                rs.getString("createBy"),
                rs.getDate("lastUpdate"),
                rs.getString("lastUpdatedBy")
        );
    }
}
