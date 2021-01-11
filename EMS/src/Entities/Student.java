/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author bizarre
 */
public class Student extends User implements SqlEntity{
    
    private final int id;

    public Student(int id, String username, String mobileNumber, String email, String firstName, String middleName, 
            String lastName, String birthdate) {
        super(username, mobileNumber, email, firstName, middleName, lastName, birthdate);
        this.id = id;
    }

    @Override
    public void fillData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
