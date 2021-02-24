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
 * @author Ziad Khobeiz
 */
public interface SqlEntity {
    
    /**
     * Fills all the attributes of the class implementing the interface by querying from the database.
     */
    public void fillData();

    /**
     * Adds the object to the database.
     */
    public void add();

    /**
     * Updates the object in the database.
     */
    public void update();

    /**
     * Updates the object in the database.
     */
    public void delete();
    /**
     * 
     * @param sequenceName
     * @return 
     */
    public static int  generateID( String sequenceName)
    {
        int id = 0;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select "+sequenceName+".NEXTVAL from dual");
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                id = myResultSet.getInt(1);
           }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return id; 
    }
}
