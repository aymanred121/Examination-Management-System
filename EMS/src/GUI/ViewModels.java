/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import Entities.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import oracle.net.ano.SupervisorService;
/**
 *
 * @author Abdel-Aziz Mostafa
 */
public class ViewModels extends Page{
    
    Instructor instructor;
    Model model;
    Exam exam;
    java.awt.Font myFont = new java.awt.Font("Tahoma", Font.LAYOUT_LEFT_TO_RIGHT, 11);
    public ViewModels(Instructor instructor, Model model) {
        this.instructor = instructor;
        this.model = model;
        exam = new Exam(model.getExamID());
        getBackButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewExams((User) instructor,exam.getExamClass()).setVisible(true);
                dispose();
        }});
        showQuestion();
        showTopBarElements();
    }
    
    private void showTopBarElements() {
        getTitleLabel().setText("Model " + model.getModelNumber() + '/' + exam.getModels().size());
        JButton previousModelButton = new JButton("Previous");
        previousModelButton.setBounds(160 , 70, 90, 30);
        getTopBar().add(previousModelButton);
        if(model.getModelNumber() == 1) {
            previousModelButton.setEnabled(false);
        } else {
            previousModelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewModels(instructor, new Model(exam.getId(), model.getModelNumber() - 1)).setVisible(true);
                dispose();
            }});
        }
        JButton nextModelButton = new JButton("Next");
        nextModelButton.setBounds(260 , 70, 90, 30);
        getTopBar().add(nextModelButton);
        if(model.getModelNumber() == exam.getModels().size()) {
            nextModelButton.setEnabled(false);
        } else {
            nextModelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewModels(instructor, new Model(exam.getId(), model.getModelNumber() + 1)).setVisible(true);
                dispose();
            }});
        }

    }    
    private void showQuestion(){
        Vector<Question> questions = model.getQuestions();
        int delta = 0 , currentIndex = 0; 
        for ( Question question : questions)
        {
            JLabel questionNumber = new JLabel("Q" + (currentIndex+1) + ": ");
            questionNumber.setBounds(20, delta, 300, 80);
            questionNumber.setFont(myFont);
            getPanel().add(questionNumber);
            JTextField questionStatement = new JTextField(question.getStatement());
            questionStatement.setEditable(false);
            questionStatement.setBounds(50, 25+delta, 550, 30);
            questionStatement.setFont(myFont);
            getPanel().add(questionStatement);
            JButton  showQuestionButton = new JButton("Show Question");
            showQuestionButton.setFont(myFont);
            showQuestionButton.setBounds(630, 25+delta , 130, 30);
            showQuestionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new ViewQuestion(instructor, question, model, false).setVisible(true);
                    dispose();
                }
            });
            getPanel().add(showQuestionButton);
            currentIndex++;
            delta += 45;
        }  
        if(exam.getStatus() == Exam.Status.UNPUBLISHED) {
            JButton addQuestionButton = new JButton("Add Question");
            addQuestionButton.setBounds(490 ,50 + delta, 130, 30);
            getPanel().add(addQuestionButton);
            addQuestionButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        new ViewQuestion(instructor, model).setVisible(true);
                        dispose();
                    }
                });
            JButton publishExamButton = new JButton("Publish Exam");
            publishExamButton.setBounds(630 ,50 + delta, 130, 30);
            getPanel().add(publishExamButton);
            publishExamButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (exam.getStartTime().isBefore(LocalDateTime.now())) {
                        JOptionPane.showMessageDialog(null, "The exam start time is Invalid, You will be directed to Edit it to a new valid date.");
                        new AddExam(instructor, exam).setVisible(true);
                        dispose();
                    } else if(exam.isReadyToPublish()) {
                        showAlertMessage("The exam has been published successfully");
                        exam.publish();
                        new ViewExams((User) new Instructor(instructor.getUsername()), new Entities.Class(exam.getExamClass().getId(), false)).setVisible(true);
                        dispose();
                    } else {
                        showAlertMessage("The exam cannot be published. Please make sure that:\n"
                                + "1- All the models have the same number of questions.\n"
                                + "2- There is at least one question in each model.");
                    }
                }
            });
        }
    }
}
