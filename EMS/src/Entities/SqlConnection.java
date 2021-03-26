/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ZiadK
 */
public class SqlConnection {
    
    public static Connection getConnection() {
        
        try {
            java.lang.Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl", "hr", "hr");
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        
    }
    
}
