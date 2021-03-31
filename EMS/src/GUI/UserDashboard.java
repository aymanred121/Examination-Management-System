/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import com.sun.xml.internal.ws.util.StringUtils;
import javax.swing.JButton;
import javax.swing.JLabel;
import Entities.*;

/**
 *
 * @author ZiadK
 */
public class UserDashboard extends Page{
    
   private User user;
   private User.UserType userType;
    
   /**
    * It constructs a new UserDashboard for a certain user given his username and his userType (whether he is an instructor or a student)
    * @param userType The type of the user (Instructor or Student)
    * @param username The username of the user
    */
    public UserDashboard(User.UserType userType, String username) {
        this.userType = userType;
        switch(userType) {
            case STUDENT:
                user = new Student(username);
                break;
            case INSTRUCTOR:
                user = new Instructor(username);
                break;
        }
        greetUser();
        showCourses();
        getBackButton().setVisible(false);
    }
    
    /**
    * It modifies the Title Label to greet the user
    */
    private void greetUser() {
        // Capitalizing first letter of userName
        String userName = user.getFirstName();
        userName = StringUtils.capitalize(userName);
        getTitleLabel().setText("Hello " + userName);
    }
    
    /**
     * It displays the names of all the courses in the panel and the corresponding buttons (Show Exams and Show Topics buttons)
     */
    private void showCourses() {

        Vector<JLabel> courseName = new Vector<JLabel>();
        Vector<JButton> topicsButtons = new Vector<JButton>(), examsButtons = new Vector<JButton>();
        Vector<Entities.Class> classes;
        switch(userType) {
            case STUDENT:
                classes = ((Student) user).getClasses();
                break;
            default:
                classes = ((Instructor) user).getClasses();
                break;
        }
        // It defines the spaces between the names of the courses  
        int delta = 0;
        for (Entities.Class currentClass : classes) {
            // To retrieve the names of the courses that the current user has
            courseName.add(new JLabel());
            courseName.lastElement().setText(currentClass.getCourse().getName());
            courseName.lastElement().setFont(new java.awt.Font("Tahoma", 1, 17));
            courseName.lastElement().setBounds(40, 40 + delta, 300, 80);
            getPanel().add(courseName.lastElement());
            examsButtons.add(new JButton());
            examsButtons.lastElement().setBounds(380, 65 + delta, 150, 30);
            examsButtons.lastElement().setText("Show Exams");
            examsButtons.lastElement().setFont(new java.awt.Font("Tahoma", 1, 12));
            getPanel().add(examsButtons.lastElement());
            examsButtons.lastElement().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new ViewExams(user, currentClass).setVisible(true);
                    dispose();
                }
            });
            // It displays the "Show Topics" button for instructors only
            if(userType == User.UserType.INSTRUCTOR) {
                topicsButtons.add(new JButton());
                topicsButtons.lastElement().setBounds(560, 65 + delta, 150, 30);
                topicsButtons.lastElement().setText("Show Topics");
                topicsButtons.lastElement().setFont(new java.awt.Font("Tahoma", 1, 12));
                topicsButtons.lastElement().addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new Login();
                        dispose();
                    }
                });
                getPanel().add(topicsButtons.lastElement());
            }
            delta += 50;
        }

    }
    
   
    
}
