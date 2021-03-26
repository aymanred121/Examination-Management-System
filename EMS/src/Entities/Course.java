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
    private String courseName;
    boolean isFilled;

    public Course(String courseCode, String name) {
        this.courseCode = courseCode;
        this.courseName = name;
    }
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
                courseName = myResultSet.getString(1);
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void add() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("insert into COURSE values(?,?)");
            myStatement.setString(1, courseCode);
            myStatement.setString(2, courseName);
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
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
        return courseName;
    }
    /**
     * @author Steven Sameh and Abdel-Aziz Mostafa
     * Checks that the course exists in the database
     * @param courseCode The courseCode of the course trying to add in database
     * @return Boolean This returns whether the course exist in the database
     */
    public static boolean isCourseCodeExisted( String courseCode){
        boolean isExisted = false;
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement SQLstatement;
            SQLstatement = myConnection.prepareStatement("select count(*) from course where coursecode = ?");
            SQLstatement.setString(1, courseCode);
            ResultSet myResultSet = SQLstatement.executeQuery();
            if(myResultSet.next() && myResultSet.getInt(1) >  0) {
                isExisted = true;
            }
            myConnection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        return isExisted;
    }

    public String getCourseCode() {
        return courseCode;
    }
    
    
}
