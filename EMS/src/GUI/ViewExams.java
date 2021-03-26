/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.*;
import org.jfree.chart.ChartPanel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * An extended version of GUI.Page that is used to create a view current exams for
 * the logged-in instructor in a specific class of his/her choice.
 *
 * @author Yusuf Nasser, Youssef Nader, Steven Sameh, Ziad Khobeiz
 * @version 1.0
 */

public class ViewExams extends Page {
    
    User user;
    Entities.Class userClass;
    User.UserType userType;
    java.awt.Font myFont = new java.awt.Font("Tahoma", Font.BOLD, 17);

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
                        new ViewModels((Instructor)user ,exam.getModels().elementAt(0)).setVisible(true);
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
                            new ViewReport((Instructor) user, exam).setVisible(true);
                            dispose();
                        }
                    });
                    getPanel().add(reportButton);
                }
            } else { // STUDENT CASE
                if (exam.getStudentStatus(user.getUsername()) == Exam.Status.RUNNING) {
                    JButton enterButton = new JButton();
                    enterButton.setText("Enter");
                    enterButton.setFont(myFont);
                    enterButton.setBounds(380 + 87, 65 + delta, 150, 30);
                    enterButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int studentModelIndex = exam.getStudentModelIndex(user.getUsername());
                            new TakeExam((Student) user, exam.getModels().elementAt(studentModelIndex)).setVisible(true);
                            dispose();
                        }
                    });
                    getPanel().add(enterButton);
                }
                else if (exam.getStudentStatus(user.getUsername()) == Exam.Status.FINISHED) {

                    // Showing the student his marks in finished Exams

                    JLabel markLabel = new JLabel("Mark: ");
                    markLabel.setBounds(380 + 87, 65 + delta, 150, 30);
                    markLabel.setFont(myFont);

                    // retrieving and rendering the student mark in the exam to a JLabel
                    int studentModelIndex = exam.getStudentModelIndex(user.getUsername());
                    JLabel studentMarkLabel = new JLabel(String.valueOf(exam.getStudentMark(user.getUsername())));
                    studentMarkLabel.setBounds(380 + 150, 65 + delta, 150, 30);
                    studentMarkLabel.setFont(myFont);

                    // retrieving and rendering the total mark of the exam to a JLabel
                    int examTotalMark = exam.getTotalMark();
                    JLabel examTotalMarkLabel = new JLabel(" / " + String.valueOf(examTotalMark));
                    examTotalMarkLabel.setBounds(380 + 175, 65 + delta, 150, 30);
                    examTotalMarkLabel.setFont(myFont);

                    // draw into the panel
                    getPanel().add(markLabel);
                    getPanel().add(studentMarkLabel);
                    getPanel().add(examTotalMarkLabel);
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
        Vector<Exam> runningExams = new Vector<>();
        Vector<Exam> upcomingExams = new Vector<>();
        Vector<Exam> finishedExams = new Vector<>();
        Vector<Exam> unpublishedExams = new Vector<>();
        for (Entities.Exam exam : exams) {
            Exam.Status currentStatus;

            if (userType == User.UserType.INSTRUCTOR) {
                currentStatus = exam.getStatus();
            } else {
                currentStatus = exam.getStudentStatus(user.getUsername());
            }

            switch (currentStatus) {
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
        delta = showExams(finishedExams, Exam.Status.FINISHED, delta);
        if (userType == User.UserType.INSTRUCTOR) {
            delta = showExams(unpublishedExams, Exam.Status.UNPUBLISHED, delta);
            showAddExamButton(delta);
        }

    }

    /**
     * It displays addNewExam Button 
     * @param delta The starting y-coordinate for drawing to keep distances
     */

    private void showAddExamButton(int delta){
        JButton addNewExam = new JButton("Add New Exam");
        addNewExam.setBounds(600, 20, 180, 30);
        addNewExam.setFont(myFont);
        addNewExam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddExam((Instructor) user, userClass).setVisible(true);
                dispose();
            }
        });
        getPanel().add(addNewExam);
    }
}