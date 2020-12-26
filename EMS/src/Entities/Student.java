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
public class Student extends User{
    private final int id;

    public Student(int id, String username, String password, String mobileNumber, String email, String name, String birthdate, int age) {
        super(username, password, mobileNumber, email, name, birthdate, age);
        this.id = id;
    }
    
    
}
