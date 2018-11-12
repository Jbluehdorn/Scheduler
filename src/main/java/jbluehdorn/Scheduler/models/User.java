package jbluehdorn.Scheduler.models;

import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * User Class
 * 
 * @author Jordan
 */
public class User {
    private int userId;
    private String userName;
    private String password;
    private Boolean active;
    private Date createDate;
    private String createdBy;
    private Date lastUpdate;
    private String lastUpdatedBy;
    
    public User() {};
    
    public User(String name) {
        this.userName = name;
    }
    
    public User( 
            String userName, 
            String password, 
            Boolean active, 
            String createdBy
    ) {
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = new Date();
        this.createdBy = createdBy;
        this.lastUpdate = new Date();
        this.lastUpdatedBy = createdBy;
    }
    
    public User(
            int userId, 
            String userName, 
            String password, 
            Boolean active,
            Date createDate,
            String createdBy,
            Date lastUpdate,
            String lastUpdatedBy
    ) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public void setUserName(String name){
        this.userName = name;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public Boolean getActive() {
        return this.active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public String getCreatedBy() {
        return this.createdBy;
    }
    
    public Date getLastUpdate() {
        return this.lastUpdate;
    }
    
    public void setLastUpdate(Date date) {
        this.lastUpdate = date;
    }
    
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }
    
    public void setLastUpdateBy(String updatedBy) {
        this.lastUpdatedBy = updatedBy;
    }
    
    @Override
    public String toString() {
        String format = "ID: %s\n"
                + "Username: %s\n";
        return String.format(format, 
                this.getUserId(), 
                this.getUserName()
        );
    }
}
