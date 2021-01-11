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
     * @param username The username of the instructor
     */
    public Instructor(String username) {
        super(username);
    }

    /**
     *  It retrieves the classes of the instructor from the database 
     */ 
    @Override
    public void fillData() {
        isFilled = true;
        classes = new Vector<Class>();
        String SQLstatement = "SELECT CLASSID FROM INSTRUCTOROF WHERE USERNAME = \'" + super.getUsername() + '\'';
        try{
            java.lang.Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection myConnection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl","hr","hr");
            Statement myStatement = myConnection.createStatement();
            ResultSet myResultSet = myStatement.executeQuery(SQLstatement);
            while(myResultSet.next()) {
                classes.add(new Class(myResultSet.getInt(1)));
            }    
            myConnection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        super.fillData();
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
    
    
}
