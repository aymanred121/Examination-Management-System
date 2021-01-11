/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author bizarre
 */
public class Instructor extends User {

    Vector<Class> classes;

    public Instructor(String username, String mobileNumber, String email, String firstName, String middleName,
            String lastName, String birthdate) {
        super(username, mobileNumber, email, firstName, middleName, lastName, birthdate);
    }

    /**
     * It initializes the instructor with the final data.
     *
     * @param username The username of the instructor
     */
    public Instructor(String username) {
        super(username);
    }

    /**
     * It retrieves the classes of the instructor from the database
     */
    @Override
    public void fillData() {
        isFilled = true;
        super.fillData();
        classes = new Vector<Class>();
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT CLASSID FROM INSTRUCTOROF WHERE USERNAME = ?");
            myStatement.setString(1, super.getUsername());
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                classes.add(new Class(myResultSet.getInt(1)));
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

    /**
     * this method query the database to retrieve courses' info for an instructor
     * @author Youssef Nader , Ayman Hassan writing and Yusuf Nasser meeting via Zoom
     * at 9:53 AM 11-1-2021 
     * @param instructorName: is a string that holds the instructor's Name
     * @return coursesResultSet: is a ResultSet which holds the courses info
     * retrieved from table 
     */
    public ResultSet queryCourses(String instructorName) {
        String SQLstatement = "SELECT course.*\n"
                + "from instructorof,class, course\n"
                + "where course.coursecode=class.coursecode\n"
                + "and class.id=instructorof.classid\n"
                + "and instructorof.username='" + instructorName + "'";
        ResultSet coursesResultSet = null;
        try {
            java.lang.Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection myConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl", "hr", "hr");
            Statement myStatement = myConnection.createStatement();
            coursesResultSet = myStatement.executeQuery(SQLstatement);
            //myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return coursesResultSet;
    }

    public Vector<Class> getClasses() {
        if(!isFilled) fillData();
        return classes;
    }

    public static void main(String[] args) {
        Instructor i = new Instructor("ibrahamhassan", "null", "a", "a", "a", "a", "a");
        ResultSet re = i.queryCourses("ibrahamhassan");
        try {
            while (re.next()) {
                System.out.println(re.getRowId(1));
                System.out.println(re.getRowId(2));
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
