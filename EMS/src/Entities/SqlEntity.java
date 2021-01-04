/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
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
     * Updates the object in the database.
     */
    public void delete();
    
}
