/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author Steven, Yusuf Nasser
 */
public class Admin extends User implements SqlEntity {

    public Admin(String username, String password, String mobileNumber, String email, String name, String birthdate, int age) {
        super(username, password, mobileNumber, email, name, birthdate, age);
    }

    @Override
    public void fillData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /*
     * Admin methods is written over here
    */
    /**
     * @Note TBC that its access modifier is public
     * @param id this is the student id that is used to add the student to the database
     * @
    */
    public void addStudent(int id){
        // To be implemented
    }
    /**
     * @Note TBC that its access modifier is private
     * @Note TBC the second parameter is Course type or Class type
     * @param id this is the student id that is used to register him in a certain course
     * @param course this parameter to know which course the student is added in
     */
    private void registerCourse(int id, Course course){
        // To be implemented
    }
}
