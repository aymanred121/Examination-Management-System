/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author AbdelAziz Mostafa
 */
public class QuestionChoice implements SqlEntity{
    int questionId ;
    char choiceNumber; 
    String choiceStatement;
    private boolean isFilled;
    
    public QuestionChoice(int questionId, char choiceNumber, String choiceStatement) {
        this.questionId = questionId;
        this.choiceNumber = choiceNumber;
        this.choiceStatement = choiceStatement;
    }

    public QuestionChoice(int questionId, char choiceNumber) {
        this.questionId = questionId;
        this.choiceNumber = choiceNumber;
    }

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement myStatement = myConnection.prepareStatement("select STATEMENT from questionChoice where QUESTIONID = ? AND choiceNumber = ?");
            myStatement.setInt(1, questionId);
            myStatement.setString(2, Character.toString(choiceNumber));
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                choiceStatement = new String(myResultSet.getString(1));
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void add() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement(
                    "insert into  questionChoice (questionId, choiceNumber , choiceStatement)\n" +
                    "values (?,?,?)");
            myStatement.setInt(1, questionId);    
            myStatement.setString(2, Character.toString(choiceNumber));
            myStatement.setString(3, choiceStatement);
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getQuestionId() {
        return questionId;
    }

    public char getChoiceNumber() {
        return choiceNumber;
    }

    public String getChoiceStatement() {
        if (!isFilled)
        {
            fillData();
        }
        return choiceStatement;
    }
    
    public boolean isIsFilled() {
        return isFilled;
    }    
}
