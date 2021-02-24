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
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author Ziad Khobeiz, Yusuf Nasser
 */
public class TakeExam extends Page {
    
    private Student student;
    private Model model;
    private Exam exam;
    private int remainingTime;
    private JLabel remainingTimeLabel;
    private Font font = new java.awt.Font("Tahoma", Font.BOLD, 24);
    private Font smallFont = new java.awt.Font("Tahoma", Font.PLAIN, 18);
    private int currentQuestionIndex;
    private JLabel questionNumber;
    private JTextArea questionTextArea;
    private Vector<QuestionChoice> questionChoices;
    private ButtonGroup buttonGroup;
    private JTextArea choiceTextArea;
    private Vector<JRadioButton> buttons;

    public TakeExam(Student student, Model model) {
        this.student = student;
        this.model = model;
        this.exam = new Exam(model.getExamID());
        displayTopBar();
        displayQuestion();
    }

    private void displayQuestion() {
        Question question = model.getQuestions().elementAt(currentQuestionIndex);
        questionNumber = new JLabel("Q" + (currentQuestionIndex + 1) + ": ");
        questionNumber.setFont(font);
        questionNumber.setBounds(10, 10, 50, 50);
        getPanel().add(questionNumber);
        questionTextArea = new JTextArea(question.getStatement());
        questionTextArea.setFont(smallFont);
        questionTextArea.setLineWrap(true);
        questionTextArea.setEditable(false);
        questionTextArea.setBounds(50, 60, 700, 100);
        getPanel().add(questionTextArea);
        questionChoices = question.getChoices();
        int offset = 150;
        buttonGroup = new ButtonGroup();
        buttons = new Vector<JRadioButton>();
        int choiceIndex = 0;
        for(QuestionChoice choice : questionChoices) {
            buttons.add(new JRadioButton(String.valueOf((char) ('a' + choiceIndex))));
            buttons.lastElement().setBounds(670, 60 + offset, 20, 20);
            buttonGroup.add(buttons.lastElement());
            getPanel().add(buttons.lastElement());
            choiceIndex++;
            choiceTextArea = new JTextArea(choice.getStatement());
            choiceTextArea.setFont(smallFont);
            choiceTextArea.setLineWrap(true);
            choiceTextArea.setEditable(false);
            choiceTextArea.setBounds(50, 60 + offset, 600, 20);
            getPanel().add(choiceTextArea);
            offset += 70;
        }
    }

    private void displayTopBar() {
        getBackButton().setVisible(false);
        getLogoutButton().setVisible(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        remainingTime = (int) Duration.between(LocalDateTime.now(), exam.getEndTime()).toMillis();
        ActionListener updateTimer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                remainingTime -= 1000;
                if(remainingTime <= 0) {
                    dispose();
                }
                remainingTimeLabel.setText(dateFormat.format(remainingTime));
            }
        };
        displayTimeLabel();
        displayExamName();
        displayFinishButton();
        remainingTimeLabel.setText(dateFormat.format(remainingTime));
        Timer timer = new Timer(1000, updateTimer);
        timer.setRepeats(true);
        timer.start();
    }

    private void displayFinishButton() {
        JButton finishButton = new JButton("Finish");
        finishButton.setBounds(690, 30, 80, 30);
        getTopBar().add(finishButton);
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Submit exam and go to the dashboard
                dispose();
            }
        });
    }

    private void displayExamName() {
        getTitleLabel().setText(exam.getExamClass().getCourse().getName() + ": " + exam.getName());
    }

    private void displayTimeLabel() {
        remainingTimeLabel = new JLabel();
        remainingTimeLabel.setFont(font);
        remainingTimeLabel.setBounds(685, 80, 200, 20);
        getTopBar().add(remainingTimeLabel);
    }

    public static void main(String args[]) {
        new TakeExam(new Student("georgesamy"), new Model(1, 1)).setVisible(true);
    }

}
