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
    
    
}
