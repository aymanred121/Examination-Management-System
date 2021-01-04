
package Entities;

import java.util.*;

/**
 *
 * @author Steven, Yusuf Nasser 19:41 4th, Jan, 2021 
 */
public class Class implements SqlEntity{

    /*
     * All the attributes of the class
    */
    private int id;
    private String courseCode;
    private Vector<Student> assignedStudents, assignedInstructor, topics, exams = new Vector<Student>();
    /*
     * Implemeting all the functions of the interface   
    */
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
     * Implementing all the functions of the class
    */
    private void displayExams(){
        // To be implemented
    }
    private Vector<Exam> getExams(){
        // To be implemented
        return null;
    }
    
}
