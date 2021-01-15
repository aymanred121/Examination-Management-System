/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author bizarre
 */
public class Student extends User implements SqlEntity{
    
    private int id;

    public Student(int id, String username, String mobileNumber, String email, String firstName, String middleName, 
            String lastName, String birthdate) {
        super(username, mobileNumber, email, firstName, middleName, lastName, birthdate);
        this.id = id;
    }
    
    public Student(String username) {
        super(username);
    }

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement myStatement = myConnection.prepareStatement("select id where username = ?");
            myStatement.setString(1, getUsername());
            ResultSet myResultSet = myStatement.executeQuery();
            if(myResultSet.next()) {
               id = myResultSet.getInt(1);
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

    public int getId() {
        if(!isFilled) {
            fillData();
        }
        return id;
    }
    
    
    
}
