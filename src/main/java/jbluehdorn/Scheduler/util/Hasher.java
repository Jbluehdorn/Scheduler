/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jbluehdorn.Scheduler.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Jordan
 */
public class Hasher {
    public static String hash(String str) {
        String hashedStr = null;
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            md.update(str.getBytes());
            
            byte[] bytes = md.digest();
            
            StringBuilder sb = new StringBuilder();
            
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            
            hashedStr = sb.toString();
        } catch(NoSuchAlgorithmException ex) {
            Logger.error(ex.getMessage());
            ex.printStackTrace();
        } 
        
        return hashedStr;
    }
}
