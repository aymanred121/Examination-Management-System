/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import Entities.*;
import java.util.Vector;


/**
 *
 * @author yn653
 */
public class ViewQuestion extends javax.swing.JFrame {

    Model model;
    String correctChoice;
    Instructor instructor;
    Question question;
    boolean isNew, isFromReport;
    
    /**
     * Creates new form CreateQuestion
     */
    public ViewQuestion(Instructor instructor, Model model) {
        isNew = true;
        this.instructor = instructor;
        this.model = model;
        initComponents();
            // Placeholders
        setVisible(true);
    }
    
    public ViewQuestion(Instructor instructor, Question question, Model model, boolean isFromReport) {
        this.instructor = instructor;
        this.question = question;
        this.model = model;
        this.isFromReport = isFromReport;
        initComponents();
        boolean isEditable = new Exam(model.getExamID()).getStatus() == Exam.Status.UNPUBLISHED;
        saveButton.setVisible(isEditable);
        displayQuestion(isEditable);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        createQuestionPanel = new javax.swing.JPanel();
        correctChoiceLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        questionTextArea = new javax.swing.JTextArea();
        enterQuestionLabel = new javax.swing.JLabel();
        choice1Label = new javax.swing.JLabel();
        choice2Label = new javax.swing.JLabel();
        choice2TextField = new javax.swing.JTextField();
        choice1TextField = new javax.swing.JTextField();
        choice4TextField = new javax.swing.JTextField();
        choice4Label = new javax.swing.JLabel();
        choice3Label = new javax.swing.JLabel();
        choice3TextField = new javax.swing.JTextField();
        choice1Rad = new javax.swing.JRadioButton();
        choice2Rad = new javax.swing.JRadioButton();
        choice3Rad = new javax.swing.JRadioButton();
        choice4Rad = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        createQuestionPanel.setBackground(new java.awt.Color(134, 171, 161));

        correctChoiceLabel.setText("Correct choice");

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        questionTextArea.setColumns(20);
        questionTextArea.setRows(5);
        jScrollPane1.setViewportView(questionTextArea);

        enterQuestionLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        enterQuestionLabel.setText("Question statement: ");

        choice1Label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        choice1Label.setText("Choice 1:");

        choice2Label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        choice2Label.setText("Choice 2:");

        choice1TextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choice1TextFieldActionPerformed(evt);
            }
        });

        choice4Label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        choice4Label.setText("Choice 4:");

        choice3Label.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        choice3Label.setText("Choice 3:");

        choice1Rad.setBackground(new java.awt.Color(134, 171, 161));
        buttonGroup.add(choice1Rad);
        choice1Rad.setText("A");

        choice2Rad.setBackground(new java.awt.Color(134, 171, 161));
        buttonGroup.add(choice2Rad);
        choice2Rad.setText("B");
        choice2Rad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choice2RadActionPerformed(evt);
            }
        });

        choice3Rad.setBackground(new java.awt.Color(134, 171, 161));
        buttonGroup.add(choice3Rad);
        choice3Rad.setText("C");

        choice4Rad.setBackground(new java.awt.Color(134, 171, 161));
        buttonGroup.add(choice4Rad);
        choice4Rad.setText("D");

        javax.swing.GroupLayout createQuestionPanelLayout = new javax.swing.GroupLayout(createQuestionPanel);
        createQuestionPanel.setLayout(createQuestionPanelLayout);
        createQuestionPanelLayout.setHorizontalGroup(
            createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createQuestionPanelLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutButton)
                .addGap(20, 20, 20))
            .addGroup(createQuestionPanelLayout.createSequentialGroup()
                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(createQuestionPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(enterQuestionLabel)
                            .addGroup(createQuestionPanelLayout.createSequentialGroup()
                                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(createQuestionPanelLayout.createSequentialGroup()
                                            .addComponent(choice4Label)
                                            .addGap(18, 18, 18)
                                            .addComponent(choice4TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE))
                                        .addGroup(createQuestionPanelLayout.createSequentialGroup()
                                            .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, createQuestionPanelLayout.createSequentialGroup()
                                                        .addComponent(choice1Label)
                                                        .addGap(18, 18, 18))
                                                    .addGroup(createQuestionPanelLayout.createSequentialGroup()
                                                        .addComponent(choice2Label)
                                                        .addGap(18, 18, 18)))
                                                .addGroup(createQuestionPanelLayout.createSequentialGroup()
                                                    .addComponent(choice3Label)
                                                    .addGap(18, 18, 18)))
                                            .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(choice3TextField, javax.swing.GroupLayout.DEFAULT_SIZE, 415, Short.MAX_VALUE)
                                                .addComponent(choice2TextField)
                                                .addComponent(choice1TextField))))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(createQuestionPanelLayout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(choice1Rad)
                                            .addComponent(choice2Rad)
                                            .addComponent(choice3Rad)
                                            .addComponent(choice4Rad)))
                                    .addGroup(createQuestionPanelLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(correctChoiceLabel))))))
                    .addGroup(createQuestionPanelLayout.createSequentialGroup()
                        .addGap(342, 342, 342)
                        .addComponent(saveButton)))
                .addContainerGap(141, Short.MAX_VALUE))
        );
        createQuestionPanelLayout.setVerticalGroup(
            createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(createQuestionPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backButton)
                    .addComponent(logoutButton))
                .addGap(38, 38, 38)
                .addComponent(enterQuestionLabel)
                .addGap(18, 18, 18)
                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(correctChoiceLabel))
                .addGap(15, 15, 15)
                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choice1Label)
                    .addComponent(choice1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choice1Rad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choice2Label)
                    .addComponent(choice2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choice2Rad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choice3Label)
                    .addComponent(choice3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choice3Rad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(createQuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(choice4Label)
                    .addComponent(choice4TextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choice4Rad))
                .addGap(33, 33, 33)
                .addComponent(saveButton)
                .addContainerGap(240, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createQuestionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createQuestionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void choice2RadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choice2RadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_choice2RadActionPerformed

    private void choice1TextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choice1TextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_choice1TextFieldActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        if(isFromReport) {
            new ViewReport(instructor, new Exam(model.getExamID())).setVisible(true);
        } else {
            new ViewModels(instructor, model).setVisible(true);
        }
        dispose();
    }//GEN-LAST:event_backButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        int finishChoice = JOptionPane.showConfirmDialog(new JFrame(), "Are you sure?");
        if(finishChoice == JOptionPane.YES_OPTION){
            if(questionValidality()){
                if(isNew) {
                    question = new Question(model.getExamID(), model.getModelNumber(), Character.toLowerCase(correctChoice.charAt(0)), questionTextArea.getText());
                    question.getChoices().add(new QuestionChoice(question.getId(),'a',choice1TextField.getText()));
                    question.getChoices().add(new QuestionChoice(question.getId(),'b',choice2TextField.getText()));
                    question.getChoices().add(new QuestionChoice(question.getId(),'c',choice3TextField.getText()));
                    question.getChoices().add(new QuestionChoice(question.getId(),'d',choice4TextField.getText()));
                    question.add();
                } else {
                    question.setStatement(questionTextArea.getText());
                    question.setCorrectChoice(Character.toLowerCase(correctChoice.charAt(0)));
                    question.getChoices().elementAt(0).setStatement(choice1TextField.getText());
                    question.getChoices().elementAt(1).setStatement(choice2TextField.getText());
                    question.getChoices().elementAt(2).setStatement(choice3TextField.getText());
                    question.getChoices().elementAt(3).setStatement(choice4TextField.getText());
                    question.update();
                }
                new ViewModels(new Instructor(instructor.getUsername()), new Model(model.getExamID(), model.getModelNumber())).setVisible(true);
                dispose();
            }
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        // TODO add your handling code here:
        new Login().setVisible(true);
        dispose();
    }//GEN-LAST:event_logoutButtonActionPerformed
    
    /** Yusuf Nasser 31st Dec 2020 12:14
    * This method is used to validate that the user input doesn't
    * consist completely of white space
    * @param data The data inputed by the user
    * @return boolean This returns whether a certain inputed data consisted of only white spaces or not
    */
    
    private boolean isAllWhiteSpaces(String data){
        int whiteSpaceFreq = 0;
        for(char c : data.toCharArray())
        {
            if (c == ' ')
                whiteSpaceFreq++;
        }
        return whiteSpaceFreq == data.length();
    }
    
    private boolean questionValidality(){
        boolean errorFound = false;
        String getQuestion = questionTextArea.getText();
        String getOption1 = choice1TextField.getText();
        String getOption2 = choice2TextField.getText();
        String getOption3 = choice3TextField.getText();
        String getOption4 = choice4TextField.getText();
        correctChoice = "";
       
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                correctChoice = button.getText();
            
            }
        }
        
        if (getOption1.length() == 0 || getOption2.length() == 0
                || getOption3.length() == 0 || getOption4.length() == 0){
            JOptionPane.showMessageDialog(null, "You should fill out all text fields", "Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
            return false;            
        }
        
        /* Yusuf Nasser 31st Dec 2020 12:10 
        * Bug Hunted by Ayman Hassan
        * Validating that the user input doesn't consist completely of white space
        */
        
        if (isAllWhiteSpaces(getQuestion) || isAllWhiteSpaces(getOption1) || isAllWhiteSpaces(getOption2)
                || isAllWhiteSpaces(getOption3) || isAllWhiteSpaces(getOption4))
        {
            JOptionPane.showMessageDialog(null, "White Spaces Do NOT count as an answer nor as a question", "Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
            return false; 
        }
        
        if(correctChoice.length() == 0){
            JOptionPane.showMessageDialog(null, "You should choose the correct answer", "Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
            return false;

        }
        if (getQuestion.length() == 0){
            JOptionPane.showMessageDialog(null, "You should enter the question statement", "Error", JOptionPane.ERROR_MESSAGE);
            errorFound = true;
            return false;
        }
        if(errorFound == false){
        JOptionPane.showMessageDialog(null, "You have saved the question successfuly", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        return true;
    }
    
    private void displayQuestion(boolean isEditable) {
        
        questionTextArea.setText(question.getStatement());
        questionTextArea.setEditable(isEditable);
        choice1TextField.setText(question.getChoices().elementAt(0).getStatement());
        choice1TextField.setEditable(isEditable);
        choice2TextField.setText(question.getChoices().elementAt(1).getStatement());
        choice2TextField.setEditable(isEditable);
        choice3TextField.setText(question.getChoices().elementAt(2).getStatement());
        choice3TextField.setEditable(isEditable);
        choice4TextField.setText(question.getChoices().elementAt(3).getStatement());
        choice4TextField.setEditable(isEditable);
        switch(question.getCorrectChoice()) {
            case 'a':
                buttonGroup.setSelected(choice1Rad.getModel(), true);
                break;
            case 'b':
                buttonGroup.setSelected(choice2Rad.getModel(), true);
                break;
            case 'c':
                buttonGroup.setSelected(choice3Rad.getModel(), true);
                break;
            case 'd':
                buttonGroup.setSelected(choice4Rad.getModel(), true);
                break;
        }

        /*
         * Added while loop to iterate over the buttonGroup elements and disable them
         */

        Enumeration<AbstractButton> enumeration = buttonGroup.getElements();
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement().setEnabled(isEditable);
        }

    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ViewQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewQuestion(new Instructor("omarhassan"), new Model(1, 2)).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JLabel choice1Label;
    private javax.swing.JRadioButton choice1Rad;
    private javax.swing.JTextField choice1TextField;
    private javax.swing.JLabel choice2Label;
    private javax.swing.JRadioButton choice2Rad;
    private javax.swing.JTextField choice2TextField;
    private javax.swing.JLabel choice3Label;
    private javax.swing.JRadioButton choice3Rad;
    private javax.swing.JTextField choice3TextField;
    private javax.swing.JLabel choice4Label;
    private javax.swing.JRadioButton choice4Rad;
    private javax.swing.JTextField choice4TextField;
    private javax.swing.JLabel correctChoiceLabel;
    private javax.swing.JPanel createQuestionPanel;
    private javax.swing.JLabel enterQuestionLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logoutButton;
    private javax.swing.JTextArea questionTextArea;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
