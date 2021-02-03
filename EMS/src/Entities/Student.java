/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author bizarre
 */
public class Student extends User implements SqlEntity{
    
    private int id;
    Vector<Class> myClasses;
    public Student(int id, String username, String mobileNumber, String email, String firstName, String middleName, 
            String lastName, String birthdate) {
        super(username, mobileNumber, email, firstName, middleName, lastName, birthdate);
        this.id = id;
    }
    
    public Student(String username) {
        super(username);
    }

    /**
     * It retrieves the data of the student from the database
     */
    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement myStatement = myConnection.prepareStatement("select id from student where username = ?");
            myStatement.setString(1, getUsername());
            ResultSet myResultSet = myStatement.executeQuery();
            if(myResultSet.next()) {
               id = myResultSet.getInt(1);
            } 
        } catch(Exception e) {
            System.out.println(e);
        }
        myClasses = new Vector<Class>();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT CLASSID FROM STUDENTREGISTER WHERE USERNAME = ?");
            myStatement.setString(1, super.getUsername());
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                myClasses.add(new Class(myResultSet.getInt(1),true));
            }
        } catch (Exception e) {
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
    /** 
     * Abdel-Aziz Mostafa writing, Yusuf Nasser and Yusuf Nader on Zoom 24th Jan 2021, 17:18
     * started working in a new branch "student session"
    */
    /**
     * this method returns the classes of the current student
     * @return Vector<Class> myClasses - the classes of the current student
     */
    public Vector<Class> getClasses() {
       if (!isFilled)
       {
           fillData();
       }
        return myClasses;
    }
    
    
    
}
