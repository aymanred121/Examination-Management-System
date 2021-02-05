/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import Entities.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author Steven, Ziad
 */
public class AddExam extends Page {
  
   private Instructor instructor;
   private Entities.Class userClass;
   
   /**
    * It constructs a new AddExam page for a certain instructor in a specific class
    * @param Instructorr The instructor object
    * @param userClass The current class to set an exam for it
    */
    public AddExam(Instructor instructor, Entities.Class userClass) {
        this.instructor = instructor;
        this.userClass = userClass;
        getTitleLabel().setText("Add New Exam");
        // Adding an action listener for the back button in the superclass to go to the ViewExams page 
        getBackButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewExams((User) instructor, userClass).setVisible(true);
                dispose();
            }
        });
    }
    
}
