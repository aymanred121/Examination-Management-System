/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.*;

/**
 * An extended version of GUI.Page that is used to host the process of examining
 * the logged-in student in a specific class whose exam is ongoing. It does that
 * by viewing the exam questions one by one through several instances of
 * javax.swing.JTextArea, javax.swing.ButtonGroup and javax.swing.JRadioButton
 *
 * @author Ziad Khobeiz, Yusuf Nasser, Youssef Nader
 * @version 1.0
 */

public class TakeExam extends Page {

    private final Student student;
    private final Model model;
    private final Exam exam;
    private final PageActionListener listener;
    private final Font font = new java.awt.Font("Tahoma", Font.BOLD, 24),
            smallFont = new java.awt.Font("Tahoma", Font.PLAIN, 18);
    private int remainingTime, currentQuestionIndex = 0;
    private String studentSelection;
    private final char[] studentAnswer;
    private JLabel remainingTimeLabel;
    private JButton nextButton, backButton, finishButton;
    private ButtonGroup buttonGroup;
    private Vector<JRadioButton> buttons;

    /**
     * Constructs a new page that allows the logged-in student to
     * take on a certain ongoing exam.
     *
     * @param student The logged-in student instance
     * @param model The model assigned to the student
     */

    public TakeExam(Student student, Model model) {
        // Setting the page size and disable its resizability to maintain the components fixed positions
        setSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        // Setting the member variables
        this.student = student;
        this.model = model;
        this.exam = new Exam(model.getExamID());
        listener = new PageActionListener();
        studentAnswer = new char[exam.getTotalMark()]; // filled with '\u0000'

        // Displaying the GUI components
        displayTopBar();
        displayPanel();
    }

    /**
     * Displays the top bar of the exam page containing the exam name,
     * time remaining to finish, and finish button for submission.
     */

    private void displayTopBar() {
        // Hiding unneeded buttons from the parent — Page class
        getBackButton().setVisible(false);
        getLogoutButton().setVisible(false);

        // displaying top bar components one by one
        displayTimeLabel();
        displayExamName();
        displayFinishButton();
    }

    /**
     * Creates and sets the Finish javax.swing.JButton instance
     * then adds it to the topBar panel
     */

    private void displayFinishButton() {
        finishButton = new JButton("Finish");
        finishButton.setBounds(690, 5, 80, 25);
        finishButton.setVerticalAlignment(SwingConstants.CENTER);
        getTopBar().add(finishButton);
        finishButton.addActionListener(listener);
    }

    /**
     * Sets the title label to the current exam name and its font
     */

    private void displayExamName() {
        getTitleLabel().setText(exam.getExamClass().getCourse().getName() + ": " + exam.getName());
        getTitleLabel().setFont(smallFont);
    }

    /**
     * Initializes the timer, displays the time label and keeps on refreshing the
     * label text to the remaining time by an action listener.
     */

    private void displayTimeLabel() {
        // Initializing the time label and setting its properties
        remainingTimeLabel = new JLabel();
        remainingTimeLabel.setFont(smallFont);
        remainingTimeLabel.setVerticalAlignment(SwingConstants.CENTER);
        remainingTimeLabel.setBounds(355, 8, 200, 20);

        // Initializing and setting a date format to view it to the student
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        // Setting the time zone to Egypt capital — Cairo time zone
        dateFormat.setTimeZone(TimeZone.getTimeZone("Egypt/Cairo"));

        // Setting the remaining time to its initial value — the whole exam time
        remainingTime = (int) Duration.between(LocalDateTime.now(), exam.getEndTime()).toMillis();

        // Putting the remaining time (in Millis) to the chosen date format and displaying it
        remainingTimeLabel.setText(dateFormat.format(remainingTime));

        // Initializing an action listener to be called by the timer
        ActionListener updateTimer = evt -> {
            remainingTime -= 1000;
            if(remainingTime <= 0) {
                // getting the student choice at current question and submitting when he or she runs out of time
                getStudentChoice();
                submitAnswers();
                dispose();
            }

            // Setting the remaining time label to its new value after subtracting the delay
            remainingTimeLabel.setText(dateFormat.format(remainingTime));
        };

        // Creating the timer so it would call the action listener updateTimer every 1000 Millis
        Timer timer = new Timer(1000, updateTimer);

        // Sets it to true so it would call the action listener everytime not just the first time
        timer.setRepeats(true);
        timer.start();

        getTopBar().add(remainingTimeLabel);
    }

    /**
     * Displays the main panel components including the question, back and next button.
     */

    private void displayPanel() {
        displayQuestion(currentQuestionIndex);
        displayBackButton();
        displayNextButton();
    }

    /**
     * Creates and sets the Back javax.swing.JButton instance then
     * adds it to the main panel if it satisfy the visibility condition
     */
    
    private void displayBackButton() {
        backButton = new JButton("Back");
        backButton.setBounds(10, 470, 80, 25);
        backButton.addActionListener(listener);
        backButton.setEnabled(currentQuestionIndex != 0);
        getPanel().add(backButton);
    }

    /**
     * Creates and sets the Next javax.swing.JButton instance then
     * adds it to the main panel if it satisfy the visibility condition
     */

    private void displayNextButton() {
        nextButton = new JButton("Next");
        nextButton.setBounds(670, 470, 80, 25);
        nextButton.addActionListener(listener);
        nextButton.setEnabled(currentQuestionIndex + 1 < model.getQuestions().size());
        getPanel().add(nextButton);
    }

    /**
     * Display the question at the passed index in model questions vector
     * @param questionIndex the index in which the question to display
     */

    private void displayQuestion(int questionIndex) {
        // Retrieving the question from the database
        Question question = model.getQuestions().elementAt(questionIndex);

        // Setting up the question number label and adding it to the panel
        JLabel questionNumber = new JLabel("Q" + (questionIndex + 1) + ": ");
        questionNumber.setFont(font);
        questionNumber.setBounds(10, 10, 100, 50);
        getPanel().add(questionNumber);

        // Setting up the question statement text area and adding it to the panel
        JTextArea questionTextArea = new JTextArea(question.getStatement());
        questionTextArea.setFont(smallFont);
        questionTextArea.setLineWrap(true);
        questionTextArea.setEditable(false);
        questionTextArea.setBounds(50, 60, 700, 100);
        getPanel().add(questionTextArea);

        /*
         * Creating a vector that holds the choices statements for the question
         * and Initializing it with choices retrieved from the database
         */

        Vector<QuestionChoice> questionChoices = question.getChoices();
        buttonGroup = new ButtonGroup();
        buttons = new Vector<>();

        int choiceIndex = 0, offset = 135;
        for(QuestionChoice choice : questionChoices) {
            buttons.add(new JRadioButton(String.valueOf((char) ('a' + choiceIndex)))); choiceIndex++;
            buttons.lastElement().setBounds(670, 46 + offset, 50, 50);
            buttons.lastElement().setBackground(new java.awt.Color(134, 171, 161));

            buttonGroup.add(buttons.lastElement());
            getPanel().add(buttons.lastElement());

            JTextArea choiceTextArea = new JTextArea(choice.getStatement());
            choiceTextArea.setFont(smallFont);
            choiceTextArea.setLineWrap(true);
            choiceTextArea.setEditable(false);
            choiceTextArea.setBounds(50, 60 + offset, 600, 35);
            getPanel().add(choiceTextArea);
            offset += 70;
        }
    }

    /**
     * Refreshes the main panel by removing then validating and finally repainting all the components
     */

    private void refreshPanel() {
        getPanel().removeAll();
        getPanel().revalidate();
        getPanel().repaint();
    }

    /**
     * Sets the current viewed question answer selection if a stored one exists
     */

    private void setQuestionSelection() {
        studentSelection = "";
        studentSelection += Character.toLowerCase(studentAnswer[currentQuestionIndex]);

        switch(studentSelection.charAt(0)) {
            case 'a':
                buttonGroup.setSelected(buttons.elementAt(0).getModel(), true);
                break;
            case 'b':
                buttonGroup.setSelected(buttons.elementAt(1).getModel(), true);
                break;
            case 'c':
                buttonGroup.setSelected(buttons.elementAt(2).getModel(), true);
                break;
            case 'd':
                buttonGroup.setSelected(buttons.elementAt(3).getModel(), true);
                break;
        }
    }

    /**
     * Retrieves the student current viewed question answer selection if exists and stores
     * it to its position in char array studentAnswer before moving on to another question
     */

    private void getStudentChoice() {
        studentSelection = "";
        for (Enumeration<AbstractButton> buttonsEnum = buttonGroup.getElements(); buttonsEnum.hasMoreElements();) {
            AbstractButton button = buttonsEnum.nextElement();
            if (button.isSelected()) {
                studentSelection = button.getText();
            }
        }

        if (!studentSelection.isEmpty()) {
            studentAnswer[currentQuestionIndex] = Character.toLowerCase(studentSelection.charAt(0));
        }
    }

    /**
     * It asks whether the student wants to submit the answers or go back to the exam.
     * It does that via showConfirmDialog() that only take Yes or No as an answer
     *
     * @return true if the student is sure about submitting the answers; false otherwise.
     */

    boolean userIsSure() {
        String message = "Are you sure to submit ?";
        return JOptionPane.showConfirmDialog(null, message, "Submit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /**
     * Submits all the student answers that are already stored at char array studentAnswer
     * by iterating over the exam model questions and inserting every answer to each
     * question in the database by calling StudentChoice.add()
     */

    private void submitAnswers() {
        Vector<Question> questions = model.getQuestions();
        for (int i = 0; i < exam.getTotalMark(); i++) {
            int questionId = questions.elementAt(i).getId();
            StudentSolution studentSolution = new StudentSolution(questionId, studentAnswer[i], student.getUsername());
            studentSolution.add();
        }
    }

    /**
     * An Implementation for the ActionListener class used to override
     * actionPerformed() and acting upon every event occurring while running.
     *
     * @author Yusuf Nasser, Youssef Nader
     */

    private class PageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == backButton) {
                getStudentChoice();
                currentQuestionIndex--;
                refreshPanel();
                displayPanel();
                setQuestionSelection();
            } else if (event.getSource() == nextButton) {
                getStudentChoice();
                currentQuestionIndex++;
                refreshPanel();
                displayPanel();
                setQuestionSelection();
            } else if (event.getSource() == finishButton) {
                getStudentChoice();
                if (userIsSure()) {
                    submitAnswers();
                    new UserDashboard(User.UserType.STUDENT, student.getUsername()).setVisible(true);
                    dispose();
                }
            }
        }
    }
}
