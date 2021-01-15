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
public class Course implements SqlEntity {
    
    private final String courseCode;
    private String name;
    boolean isFilled;
    
    public Course(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT COURSENAME FROM COURSE WHERE COURSECODE = ?");
            myStatement.setString(1, courseCode);
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                name = myResultSet.getString(1);
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void add() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getName() {
        if(!isFilled) {
            fillData();
        }
        return name;
    }
    
    
    
}
