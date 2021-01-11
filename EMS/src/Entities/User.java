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
public abstract class User implements SqlEntity{
    
    private final String username;
    private String mobileNumber, email, firstName, middleName, lastName, birthdate;
    protected boolean isFilled;
    
    public User(String username, String mobileNumber, String email, String firstName, String middleName, 
            String lastName, String birthdate) {
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthdate = birthdate;
    }
    
    /**
     * It initializes the user with the final data. 
     * @param username The username of the user
     */
    public User(String username) {
        this.username = username;
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
     * It retrieves all the data of the user from the database
     */
    @Override
    public void fillData() {
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement myStatement = myConnection.prepareStatement("select first_name, "
                    + "middle_name, last_name, birthdate, mobile_number, email"
                + " from userdata where username = ?");
            myStatement.setString(1, username);
            ResultSet myResultSet = myStatement.executeQuery();
            while(myResultSet.next()) {
                firstName = myResultSet.getString(1);
                middleName = myResultSet.getString(2);
                lastName = myResultSet.getString(3);
                birthdate = myResultSet.getString(4);
                mobileNumber = myResultSet.getString(5);
                email = myResultSet.getString(6);
            } 
            myConnection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    
    @Override
    public void add() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getUsername() {
        return username;
    }
    
    /**
     * Checks whether the user is in the correct table in the database
     * @author Ziad Khobeiz and Abdel-Aziz Mostafa
     * @param username The username of the user
     * @param tableName The corresponding table of the UserType needed to check
     * @return Boolean It determines whether the username is found in the table in the database
     */
    private static boolean isCorrectType(String username, String tableName) {
        boolean isExisting = false;
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement SQLstatement;
            SQLstatement = myConnection.prepareStatement("select count(*) from " + tableName + " where username = ?");
            SQLstatement.setString(1, username);
            ResultSet myResultSet = SQLstatement.executeQuery();
            if(myResultSet.next() && myResultSet.getString(1).equals("1")) {
                isExisting = true;
            }
            myConnection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        return isExisting;
    }
    
    /**
     * Determines the type of the user
     * @author Ziad Khobeiz and Abdel-Aziz Mostafa
     * @param username The username of the user
     * @return UserType This returns the type of the user existing in the database with the corresponding username
     */
    public static UserType getUserType(String username) {
        
        if(isCorrectType(username, "STUDENT")) {
            return UserType.STUDENT;
        } else if(isCorrectType(username, "INSTRUCTOR")) {
            return UserType.INSTRUCTOR;
        } else {
            return UserType.ADMIN;
        }
        
    }

    public String getFirstName() {
        return firstName;
    }
    
    /**
     * @author Ziad Khobeiz and Abdel-Aziz Mostafa
     * Validates that the username with the corresponding password are in the database
     * @param username The username of the user trying to login
     * @param password The password of the user trying to login
     * @return Boolean This returns whether the username with the corresponding password exist in the database
     */
    public static boolean isValidUser(String username, String password) {
        boolean isValid = false;
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement SQLstatement;
            SQLstatement = myConnection.prepareStatement("select count(*) from userdata where username = ? and password = ?");
            SQLstatement.setString(1, username);
            SQLstatement.setString(2, password);
            ResultSet myResultSet = SQLstatement.executeQuery();
            if(myResultSet.next() && myResultSet.getString(1).equals("1")) {
                isValid = true;
            }
            myConnection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        return isValid;
    }
    
}
