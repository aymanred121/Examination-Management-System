/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Represents a connection with Oracle HR database through jdbc6 driver
 *
 * @author Ziad Khobeiz
 */
public class SqlConnection {

    /**
     * Creates and Returns a connection with the project database to facilitate the
     * SQL processes.
     *
     * @return a connection with hr database if no exception occurred; null otherwise.
     */

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
