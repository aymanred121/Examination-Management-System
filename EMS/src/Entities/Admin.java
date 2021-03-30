/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *An extended version of User class
 * Use the constructor retrieve the admin and to login into the system
 * Admin can add new classes, instructors and assign instructor to classes.
 * @author Steven, Yusuf Nasser
 */
public class Admin extends User {

    public Admin(String username) {
        super(username);
    }
    
}
