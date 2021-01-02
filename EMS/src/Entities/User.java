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
    
    public static void main(String[] args) {
        
        try{  
            //step1 load the driver class  
            Class.forName("oracle.jdbc.driver.OracleDriver");  
  
            //step2 create  the connection object  
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl" ,"hr","hr");  
  
            //step3 create the statement object  
            Statement stmt=con.createStatement();  
  
            //step4 execute query  
            ResultSet rs=stmt.executeQuery("select * from userData");  
            while(rs.next())  
            System.out.println(rs.getString("USERNAME")+"  "+rs.getString("FIRST_NAME"));  
  
            //step5 close the connection object  
            stmt.close();
            con.close();  
  
            }
        catch(Exception e){ System.out.println(e);}  
  
     }  
}
