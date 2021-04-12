/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.*;
import java.time.LocalDate;

/**
 * Represents the main abstract parent entity to child entities &mdash; Admin, Student and Instructor,
 * it contains the mutual fields between the subclasses such as their username and their birthdate.
 *
 * @author Steven Sameh and AbdelAziz Mostafa
 */

public abstract class User {

    private final String username;
    private String mobileNumber, email, firstName, middleName, lastName, password;
    private LocalDate birthdate;
    protected boolean isFilled, isUserFilled;

    /**
     * It initializes the user with the final data.
     *
     * @param username The username of the user
     */

    public User(String username) {
        this.username = username;
    }

    /**
     * Constructs a new user to be added in the database via other methods.
     *
     * @param username     the user's unique username
     * @param mobileNumber the user's registered mobile number
     * @param email        the user's contact email
     * @param firstName    the user's first name
     * @param middleName   the user's middle name
     * @param lastName     the user's last name
     * @param birthdate    the user's birthdate value
     */

    public User(String username, String mobileNumber, String email, String firstName, String middleName, String lastName, LocalDate birthdate, String password) {
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.password = password;
    }

    /**
     * Represents the type of user
     */

    public enum UserType {
        STUDENT,
        ADMIN,
        INSTRUCTOR
    }

    /**
     * It retrieves all the data of the user from the database
     */

    public void fillUserData() {
        isUserFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select firstname, "
                    + "middlename, lastname, birthdate, mobilenumber, email"
                    + " from userdata where username = ?");
            myStatement.setString(1, username);
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                firstName = myResultSet.getString(1);
                middleName = myResultSet.getString(2);
                lastName = myResultSet.getString(3);
                birthdate = myResultSet.getDate(4).toLocalDate();
                mobileNumber = myResultSet.getString(5);
                email = myResultSet.getString(6);
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a new user to the database through a SQL Statement inserting
     * all the new user's data.
     */

    public void addUser() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("insert into UserData values (?,?,?,?,?,?,?,?)");
            myStatement.setString(1, username);
            myStatement.setString(2, firstName);
            myStatement.setString(3, middleName);
            myStatement.setString(4, lastName);
            myStatement.setDate(5, Date.valueOf(birthdate));
            myStatement.setString(6, mobileNumber);
            myStatement.setString(7, email);
            myStatement.setString(8, password);
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * it returns the UserName of the current user
     *
     * @return String defining the username of the user
     */

    public String getUsername() {
        if (!isUserFilled) {
            this.fillUserData();
        }
        return username;
    }

    /**
     * Checks whether the user is in the correct table in the database
     *
     * @param username  The username of the user
     * @param tableName The corresponding table of the UserType needed to check
     * @return Boolean It determines whether the username is found in the table in the database
     * @author Ziad Khobeiz and Abdel-Aziz Mostafa
     */

    private static boolean isCorrectType(String username, String tableName) {
        boolean isExisting = false;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("select count(*) from " + tableName + " where username = ?");
            SQLStatement.setString(1, username);
            ResultSet myResultSet = SQLStatement.executeQuery();
            if (myResultSet.next() && myResultSet.getString(1).equals("1")) {
                isExisting = true;
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return isExisting;
    }

    /**
     * Determines the type of the user
     *
     * @param username The username of the user
     * @return UserType This returns the type of the user existing in the database with the corresponding username
     * @author Ziad Khobeiz and Abdel-Aziz Mostafa
     */

    public static UserType getUserType(String username) {

        if (isCorrectType(username, "STUDENT")) {
            return UserType.STUDENT;
        } else if (isCorrectType(username, "INSTRUCTOR")) {
            return UserType.INSTRUCTOR;
        } else {
            return UserType.ADMIN;
        }

    }

    /**
     * it returns the FirstName of the User
     *
     * @return String defining the FirstName of the user
     */

    public String getFirstName() {
        // it fills the data of the user only not all the data of the object    
        if (!isUserFilled) {
            this.fillUserData();
        }
        return firstName;
    }

    /**
     * Validates that the username with the corresponding password are in the database
     *
     * @param username The username of the user trying to login
     * @param password The password of the user trying to login
     * @return isValid: if the username with the corresponding password exist in the database true; false otherwise.
     * @author Ziad Khobeiz and Abdel-Aziz Mostafa
     */

    public static boolean isValidUser(String username, String password) {
        boolean isValid = false;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("select count(*) from userdata where username = ? and password = ?");
            SQLStatement.setString(1, username);
            SQLStatement.setString(2, password);
            SQLStatement.setString(1, username);
            ResultSet myResultSet = SQLStatement.executeQuery();
            if (myResultSet.next() && myResultSet.getString(1).equals("1")) {
                isValid = true;
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return isValid;
    }

    /**
     * @param username The username of the user trying to add in database
     * @return Boolean This returns whether the username exist in the database
     * @author Steven Sameh and Abdel-Aziz Mostafa
     * Checks that the username exists in the database
     */

    public static boolean isUsernameExisted(String username) {
        boolean isExisted = false;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("select count(*) from userdata where lower(username) = ?");
            SQLStatement.setString(1, username);
            ResultSet myResultSet = SQLStatement.executeQuery();
            if (myResultSet.next() && myResultSet.getInt(1) > 0) {
                isExisted = true;
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return isExisted;
    }
}
