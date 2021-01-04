/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.*;

/**
 *
 * @author bizarre
 */
public abstract class User {
    private String username, password, mobileNumber,
           email, name, birthdate;
    private int age;

    public User(String username, String password, String mobileNumber, String email, String name, String birthdate, int age) {
        this.username = username;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.age = age;
    }
    
    /**
     * Represents the type of user
     */
    public enum UserType{
        STUDENT,
        ADMIN,
        INSTRUCTOR
    }
    
    
    /**
     * Validates that the username with the corresponding password are in the database
     * @param username The username of the user trying to login
     * @param password The password of the user trying to login
     * @return Boolean This returns whether the username with the corresponding password exist in the database
     */
    public static Boolean login(String username, String password) {
        // To be implemented
        return true;
    }
    
    /**
     * Determines the type of the user
     * @param username The username of the user
     * @return UserType This returns the type of the user existing in the database with the corresponding username
     */
    public static UserType getUserType(String username) {
        return null;
        // To be implemented
        
    }
    
}
