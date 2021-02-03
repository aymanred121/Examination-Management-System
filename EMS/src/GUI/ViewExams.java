/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Exam;
import Entities.User;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author ZiadK
 */
public class ViewExams extends Page {
    
    User user;
    Entities.Class userClass;
    User.UserType userType;

    /**
     * It constructs a new ViewExams page for a certain user in a specific class
     * @param user The user object
     * @param userClass The current class to display its exams
     */
    public ViewExams(User user, Entities.Class userClass) {
        this.user = user;
        this.userClass = userClass;
        userType = user.getUserType(user.getUsername());
        // Adding an action listener for the back button in the superclass to go to the UserDashboard 
        getBackButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UserDashboard(userType, user.getUsername()).setVisible(true);
                dispose();
            }
        });
        showExams();
    }

    /**
     * It displays the exams of a certain type (e.g. Running exams) with the corresponding buttons
     * ("Show Reports" button in case of finished exams of the instructor
     * and "Show Models" button in all the exams of the instructor
     * and "Enter" button in case of running exams for the student).
     * @param exams the java.util.Vector containing all the considered exams
     * @param examStatus The status of the exam
     * @param delta The starting y-coordinate for drawing to keep distances
     * between the exams
     * @return int This returns the new delta (current y-coordinate to draw)
     */
    private int showExams(Vector<Exam> exams, Exam.Status examStatus, int delta) {

        // Putting the name of the current examStatus (which is an enum) in the string examName (e.g. for the status RUNNING, it puts "Running" in the examName)
        String examName = examStatus.name().toLowerCase();
        examName = examName.substring(0, 1).toUpperCase() + examName.substring(1);
        java.awt.Font titleFont = new java.awt.Font("Tahoma", Font.BOLD, 20);
        java.awt.Font myFont = new java.awt.Font("Tahoma", Font.BOLD, 17);
        JLabel ExamsLabel = new JLabel(examName + " exams:");
        ExamsLabel.setBounds(20, delta, 300, 80);
        ExamsLabel.setFont(titleFont);
        getPanel().add(ExamsLabel);

        for (Exam exam : exams) {

            JLabel examNameLabel = new JLabel();
            examNameLabel.setText(exam.getName());
            examNameLabel.setBounds(40, 40 + delta, 300, 80);
            examNameLabel.setFont(myFont);
            getPanel().add(examNameLabel);
            if (userType == User.UserType.INSTRUCTOR) {
                JButton modelsButton = new JButton();
                modelsButton.setText("Show Models");
                modelsButton.setFont(myFont);
                modelsButton.setBounds(380 + 87, 65 + delta, 150, 30);
                modelsButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // new Login();
                        dispose();
                    }
                });
                getPanel().add(modelsButton);
                if (examStatus == Exam.Status.FINISHED) {
                    modelsButton.setBounds(380, 65 + delta, 150, 30);
                    JButton reportButton = new JButton();
                    reportButton.setText("Show Report");
                    reportButton.setFont(myFont);
                    reportButton.setBounds(560, 65 + delta, 150, 30);
                    reportButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // new Login();
                            dispose();
                        }
                    });
                    getPanel().add(reportButton);
                }
            } else {
                if (exam.getStatus() == Exam.Status.RUNNING) {
                    JButton enterButton = new JButton();
                    enterButton.setText("Enter");
                    enterButton.setFont(myFont);
                    enterButton.setBounds(380 + 87, 65 + delta, 150, 30);
                    enterButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // new Login();
                            dispose();
                        }
                    });
                    getPanel().add(enterButton);
                }
            }
            delta += 50;
        }

        // When there is no exams of the current status, it displays a message to inform the user
        if (exams.size() == 0) {

            JLabel noExams = new JLabel("No " + examStatus.name().toLowerCase() + " exams.");
            noExams.setBounds(40, 40 + delta, 300, 80);
            noExams.setFont(myFont);
            getPanel().add(noExams);
            delta += 50;

        }

        delta += 50;
        return delta;

    }

    /**
     * It displays all the exams of the current class ordered according to the
     * following order:
     * 1- Running exams
     * 2- Upcoming exams
     * 3- Finished exams (for instructors)
     * 4- Unpublished Exams (for instructors).
     */
    private void showExams() {
        getTitleLabel().setText(userClass.getCourse().getName() + " exams:");
        Vector<Exam> exams = userClass.getExams();
        Vector<Exam> runningExams = new Vector<Exam>();
        Vector<Exam> upcomingExams = new Vector<Exam>();
        Vector<Exam> finishedExams = new Vector<Exam>();
        Vector<Exam> unpublishedExams = new Vector<Exam>();

        for (Entities.Exam exam : exams) {

            switch (exam.getStatus()) {
                case UNPUBLISHED:
                    unpublishedExams.add(exam);
                    break;
                case RUNNING:
                    runningExams.add(exam);
                    break;
                case FINISHED:
                    finishedExams.add(exam);
                    break;
                case UPCOMING:
                    upcomingExams.add(exam);
                    break;
            }
        }

        Collections.reverse(finishedExams);

        int delta = 0;
        delta = showExams(runningExams, Exam.Status.RUNNING, delta);
        delta = showExams(upcomingExams, Exam.Status.UPCOMING, delta);
        if (userType == User.UserType.INSTRUCTOR) {
            delta = showExams(finishedExams, Exam.Status.FINISHED, delta);
            showExams(unpublishedExams, Exam.Status.UNPUBLISHED, delta);
        }

    }

}
