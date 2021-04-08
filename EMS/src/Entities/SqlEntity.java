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
 * Represents all the mutual functionalities that are needed to insert, delete,
 * update and retrieve information in a SQL database.
 *
 * @author Ziad Khobeiz
 */

interface SqlEntity {

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
     * Deletes the object in the database.
     */

    public void delete();

    /**
     * Generates and Returns a unique ID that belongs to a certain sequence
     *
     * @param sequenceName the sequence in which the id to be generated
     * @return the generated id for the desired sequence
     */

    public static int generateID(String sequenceName) {
        int id = 0;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select " + sequenceName + ".NEXTVAL from dual");
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
