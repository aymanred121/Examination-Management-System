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
 * @author Ziad Khobeiz, Yusuf Nasser
 * @version 1.0
 */

public class TakeExam extends Page {

    private Student student;
    private Model model;
    private Exam exam;
    private String studentSelection;
    char[] studentAnswer;
    private int remainingTime, currentQuestionIndex = 0;
    private final Font font = new java.awt.Font("Tahoma", Font.BOLD, 24),
            smallFont = new java.awt.Font("Tahoma", Font.PLAIN, 18);
    private JLabel remainingTimeLabel;
    private JButton nextButton, backButton, finishButton;
    private PageActionListener listener;
    ButtonGroup buttonGroup;
    Vector<JRadioButton> buttons;

    public TakeExam(Student student, Model model) {
        this.student = student;
        this.model = model;
        this.exam = new Exam(model.getExamID());
        listener = new PageActionListener();
        studentAnswer = new char[exam.getTotalMark()];

        displayTopBar();
        displayPanel();
    }

    private void displayTopBar() {
        setSize(new java.awt.Dimension(800, 600));
        getBackButton().setVisible(false);
        getLogoutButton().setVisible(false);
        displayTimeLabel();
        displayExamName();
        displayFinishButton();
    }

    private void displayFinishButton() {
        finishButton = new JButton("Finish");
        finishButton.setBounds(690, 11, 80, 25);
        finishButton.setVerticalAlignment(SwingConstants.CENTER);
        getTopBar().add(finishButton);
        finishButton.addActionListener(listener);
    }

    private void displayExamName() {
        getTitleLabel().setText(exam.getExamClass().getCourse().getName() + ": " + exam.getName());
        getTitleLabel().setFont(smallFont);
    }

    private void displayTimeLabel() {
        remainingTimeLabel = new JLabel();
        remainingTimeLabel.setFont(smallFont);
        remainingTimeLabel.setVerticalAlignment(SwingConstants.CENTER);
        remainingTimeLabel.setBounds(355, 15, 200, 20);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        remainingTime = (int) Duration.between(LocalDateTime.now(), exam.getEndTime()).toMillis();
        remainingTimeLabel.setText(dateFormat.format(remainingTime));

        ActionListener updateTimer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                remainingTime -= 1000;
                if(remainingTime <= 0) {
                    dispose();
                }
                remainingTimeLabel.setText(dateFormat.format(remainingTime));
            }
        };

        Timer timer = new Timer(1000, updateTimer);
        timer.setRepeats(true);
        timer.start();

        getTopBar().add(remainingTimeLabel);
    }

    private void displayPanel() {
        displayQuestion(currentQuestionIndex);
        displayBackButton();
        displayNextButton();
    }
    
    private void displayBackButton() {
        backButton = new JButton("Back");
        backButton.setBounds(10, 470, 80, 25);
        backButton.addActionListener(listener);
        backButton.setVisible(currentQuestionIndex != 0);
        getPanel().add(backButton);
    }

    private void displayNextButton() {
        nextButton = new JButton("Next");
        nextButton.setBounds(670, 470, 80, 25);
        nextButton.addActionListener(listener);
        nextButton.setVisible(currentQuestionIndex + 1 < model.getQuestions().size());
        getPanel().add(nextButton);
    }

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

    private void refreshPanel() {
        getPanel().removeAll();
        getPanel().revalidate();
        getPanel().repaint();
    }

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
     * It asks whether the student wants to submit the answers or edit them.
     * It does that via showConfirmDialog() that only take Yes or No as an answer
     *
     * @return true if the student is sure about the answer; false otherwise.
     */

    boolean userIsSure() {
        String message = "Are you sure to submit ?";
        return JOptionPane.showConfirmDialog(null, message, "Submit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void submitAnswers() {
        Vector<Question> questions = model.getQuestions();
        for (int i = 0; i < exam.getTotalMark(); i++) {
            int questionId = questions.elementAt(i).getId();
            StudentChoice studentChoice = new StudentChoice(questionId, studentAnswer[i], student.getUsername());
            studentChoice.add();
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
            } else if (event.getSource() == finishButton && userIsSure()) {
                submitAnswers();
                dispose();
            }
        }
    }

    public static void main(String[] args) {
        new TakeExam(new Student("georgesamy"), new Model(1, 1)).setVisible(true);
    }

}
