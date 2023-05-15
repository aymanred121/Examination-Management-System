/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.*;

import java.awt.Font;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * An extended version of GUI.Page that is used to create a view current exams for
 * the logged-in instructor in a specific class of his/her choice.
 *
 * @author Abdel-Aziz Mostafa, Yusuf Nasser, Youssef Nader, Steven Sameh, Ziad Khobeiz, Ayman Hassan
 * @version 1.0
 */

public class ViewExams extends Page {

    private final User user;
    private final Entities.Class userClass;
    private final User.UserType userType;
    private final Font myFont = new java.awt.Font("Tahoma", Font.BOLD, 17),
            titleFont = new java.awt.Font("Tahoma", Font.BOLD, 20);
    private final Vector<Exam> exams;
    private Vector<Exam> runningExams, upcomingExams, finishedExams, unpublishedExams;
    /**
     * The starting y-coordinate for drawing to keep distances between the exams
     */
    private int currentHeight = 0;

    /**
     * It constructs a new ViewExams page for a certain user in a specific class
     *
     * @param user      The user viewing the class exams page
     * @param userClass The current class to display its exams
     */

    public ViewExams(User user, Entities.Class userClass) {
        this.user = user;
        this.userClass = userClass;
        this.exams = userClass.getExams();
        userType = User.getUserType(user.getUsername());

        setSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        // Adding an action listener for the back button in the superclass to go to the UserDashboard
        getBackButton().addActionListener(e -> {
            new UserDashboard(userType, user.getUsername()).setVisible(true);
            dispose();
        });

        showExams();
    }

    /**
     * Classifies the exams of the class to: Running exams, Upcoming exams,
     * Finished exams, Unpublished Exams (ONLY instructors).
     */

    private void classifyExams() {
        runningExams = new Vector<>();
        upcomingExams = new Vector<>();
        finishedExams = new Vector<>();
        unpublishedExams = new Vector<>();

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
    }

    /**
     * Displays addNewExam Button after creating, initializing, and setting it
     */

    private void showAddExamButton() {
        JButton addNewExam = new JButton("Add New Exam");
        addNewExam.setBounds(560, 20, 180, 30);
        addNewExam.setFont(myFont);

        addNewExam.addActionListener(e -> {
            new AddExam((Instructor) user, userClass).setVisible(true);
            dispose();
        });

        getPanel().add(addNewExam);
    }

    /**
     * Creates, initializes, sets the 'Enter Exam' button, adds an action listener to it
     * then shows it to the student.
     *
     * @param exam the exam to link with the button via the action listener
     */

    private void showEnterExamButton(Exam exam) {
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(myFont);
        enterButton.setBounds(380 + 87, 85 + currentHeight, 150, 30);
        enterButton.addActionListener(e -> {
            int studentModelIndex = exam.getStudentModelIndex(user.getUsername());
            new TakeExam((Student) user, exam.getModels().elementAt(studentModelIndex)).setVisible(true);
            dispose();
        });
        getPanel().add(enterButton);
    }

    /**
     * Shows that an exam portion doesn't contain any exams at the moment
     *
     * @param examStatus the status of the exams portion
     */

    private void showNoExamsLabel(Exam.Status examStatus) {
        JLabel noExams = new JLabel("No " + examStatus.name().toLowerCase() + " exams.");
        noExams.setBounds(40, 60 + currentHeight, 300, 80);
        noExams.setFont(myFont);
        getPanel().add(noExams);
    }

    /**
     * Creates, initializes, sets the 'Report' button, adds an action listener to it
     * then shows it to the instructor.
     *
     * @param exam the exam to link with the button via the action listener
     */

    private void showReportButton(Exam exam) {
        JButton reportButton = new JButton("Report");
        reportButton.setFont(myFont);
        reportButton.setBounds(570, 85 + currentHeight, 130, 30);
        reportButton.addActionListener(e -> {
            new ViewReport((Instructor) user, exam).setVisible(true);
            dispose();
        });
        getPanel().add(reportButton);
    }

    /**
     * Creates, initializes, sets the 'Questions Ranked' button, adds an action listener to it
     * then shows it to the instructor.
     *
     * @param exam the exam to link with the button via the action listener
     */

    private void showQuestionsRankedButton(Exam exam) {
        JButton showQuestionRank = new JButton("Questions Ranked");
        showQuestionRank.setFont(myFont);
        showQuestionRank.setBounds(380 - 190, 85 + currentHeight, 196, 30);
        showQuestionRank.addActionListener(e -> {
            new ViewModels((Instructor) user, exam.getModels().elementAt(0), true).setVisible(true);
            dispose();
        });
        getPanel().add(showQuestionRank);
    }

    /**
     * Creates, initializes, sets the 'Models' button, adds an action listener to it
     * then returns it.
     *
     * @param exam the exam to link with the button via the action listener
     * @return an initialized and set JButton instance representing the 'Models' button.
     */

    private JButton getModelsButton(Exam exam) {
        JButton modelsButton = new JButton("Models");
        modelsButton.setFont(myFont);
        modelsButton.setBounds(560, 85 + currentHeight, 120, 30);
        modelsButton.addActionListener(e -> {
            new ViewModels((Instructor) user, exam.getModels().elementAt(0)).setVisible(true);
            dispose();
        });
        return modelsButton;
    }

    /**
     * Builds multiple JLabels representing the student mark then
     * shows it to him or her.
     *
     * @param exam the exam from which the student is queried
     */

    private void showStudentMark(Exam exam) {
        // Showing the student his marks in finished Exams
        JLabel markLabel = new JLabel("Mark: ");
        markLabel.setBounds(380 + 87, 85 + currentHeight, 150, 30);
        markLabel.setFont(myFont);

        // retrieving and rendering the student mark in the exam to a JLabel
        JLabel studentMarkLabel = new JLabel(String.valueOf(exam.getStudentMark(user.getUsername())));
        studentMarkLabel.setBounds(380 + 150, 85 + currentHeight, 150, 30);
        studentMarkLabel.setFont(myFont);

        // retrieving and rendering the total mark of the exam to a JLabel
        int examTotalMark = exam.getTotalMark();
        JLabel examTotalMarkLabel = new JLabel(" / " + examTotalMark);
        examTotalMarkLabel.setBounds(380 + 175, 85 + currentHeight, 150, 30);
        examTotalMarkLabel.setFont(myFont);

        // draw into the panel
        getPanel().add(markLabel);
        getPanel().add(studentMarkLabel);
        getPanel().add(examTotalMarkLabel);
    }

    /**
     * Builds a JLabel representing the exam date and time then shows it to the user
     *
     * @param exam the exam whose time and date shall be shown
     */

    private void showExamDateTime(Exam exam) {
        String DateTime = "Date & Time: ";
        DateTime += (exam.getStartTime().toLocalDate().toString() + "  ");
        DateTime += exam.getStartTime().toLocalTime().toString();

        JLabel examDateTime = new JLabel(DateTime);
        examDateTime.setFont(myFont);

        if (userType == User.UserType.INSTRUCTOR) {
            examDateTime.setBounds(180, 60 + currentHeight, 400, 80);
        } else {
            examDateTime.setBounds(350, 60 + currentHeight, 400, 80);
        }

        getPanel().add(examDateTime);
    }

    /**
     * Shows a JLabel representing the exam name
     *
     * @param exam the exam whose name will be shown
     */

    private void showExamName(Exam exam) {
        JLabel examNameLabel = new JLabel(exam.getName());
        examNameLabel.setBounds(40, 60 + currentHeight, 300, 80);
        examNameLabel.setFont(myFont);
        getPanel().add(examNameLabel);
    }

    /**
     * Shows the name of an exams portion, i.e. Running Exams
     *
     * @param examStatus the status of the exams portion
     */

    private void showExamsPortionName(Exam.Status examStatus) {
        String examName = examStatus.toString().toLowerCase();
        examName = examName.substring(0, 1).toUpperCase() + examName.substring(1);

        JLabel ExamsLabel = new JLabel(examName + " exams:");
        ExamsLabel.setBounds(20, currentHeight + 10, 300, 80);
        ExamsLabel.setFont(titleFont);
        getPanel().add(ExamsLabel);
    }

    /**
     * It displays the exams of a certain type (e.g. Running exams) with the corresponding buttons
     * <p>"Reports" button in case of finished exams of the instructor
     * and "Show Models" button in all the exams of the instructor
     * and "Enter" button in case of running exams for the student.</p>
     *
     * @param portionExams the java.util.Vector containing all the considered exams
     * @param examStatus   The status of the exam
     */

    private void showExamsPortion(Vector<Exam> portionExams, Exam.Status examStatus) {

        showExamsPortionName(examStatus);

        for (Exam exam : portionExams) {

            showExamName(exam);

            // Showing the exam date and time to the user
            if (exam.getStatus() == Exam.Status.UPCOMING || exam.getStatus() == Exam.Status.UNPUBLISHED) {
                showExamDateTime(exam);
            }

            if (userType == User.UserType.INSTRUCTOR) {

                JButton modelsButton = getModelsButton(exam);
                getPanel().add(modelsButton);

                if (examStatus == Exam.Status.FINISHED) {
                    showQuestionsRankedButton(exam);
                    showReportButton(exam);

                    // Setting models button bounds to fit with the new two buttons
                    modelsButton.setBounds(420, 85 + currentHeight, 120, 30);
                }

            } else if (userType == User.UserType.STUDENT) {

                if (exam.getStudentStatus(user.getUsername()) == Exam.Status.RUNNING) {
                    showEnterExamButton(exam);
                } else if (exam.getStudentStatus(user.getUsername()) == Exam.Status.FINISHED) {
                    showStudentMark(exam);
                }

            }

            currentHeight += 50;
        }

        // When there is no exams of the current status, it displays a message to inform the user
        if (portionExams.size() == 0) {
            showNoExamsLabel(examStatus);
            currentHeight += 50;
        }

        currentHeight += 50;
    }

    /**
     * It displays all the exams of the current class ordered according to the
     * following order: Running exams, Upcoming exams, Finished exams and
     * Unpublished Exams (for instructors).
     */

    private void showExams() {
        getTitleLabel().setText(userClass.getCourse().getName() + " exams:");
        classifyExams();

        showExamsPortion(runningExams, Exam.Status.RUNNING);
        showExamsPortion(upcomingExams, Exam.Status.UPCOMING);
        showExamsPortion(finishedExams, Exam.Status.FINISHED);

        if (userType == User.UserType.INSTRUCTOR) {
            showExamsPortion(unpublishedExams, Exam.Status.UNPUBLISHED);
            showAddExamButton();
        }

    }

}