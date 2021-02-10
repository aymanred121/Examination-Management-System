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
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
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
        getTitleLabel().setText("Model " + model.getModelNumber());
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
            System.out.println(question.getStatement());
            JTextField questionStatement = new JTextField(question.getStatement());
            questionStatement.setEditable(false);
            questionStatement.setBounds(50, 25+delta, 420, 30);
            questionStatement.setFont(myFont);
            getPanel().add(questionStatement);
            JButton  showQuestionButton = new JButton("Show Question");
            showQuestionButton.setFont(myFont);
            showQuestionButton.setBounds(550, 25+delta , 100, 30);
            showQuestionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //
                    dispose();
                }
            });
            getPanel().add(showQuestionButton);
            currentIndex++;
            delta += 45;
        }
    }
     public static void main(String args[]) {
   
    }
}
