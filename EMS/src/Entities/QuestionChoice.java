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
    final int questionId;
    final char choiceNumber; 
    String statement;
    private boolean isFilled;
    
    public QuestionChoice(int questionId, char choiceNumber, String statement) {
        this.questionId = questionId;
        this.choiceNumber = choiceNumber;
        this.statement = statement;
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
            PreparedStatement myStatement = myConnection.prepareStatement("select choiceStatement from questionChoice where QUESTIONID = ? AND choiceNumber = ?");
            myStatement.setInt(1, questionId);
            myStatement.setString(2, Character.toString(choiceNumber));
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                statement = new String(myResultSet.getString(1));
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
                    "insert into questionChoice (questionId, choiceNumber , choiceStatement)\n" +
                    "values (?,?,?)");
            myStatement.setInt(1, questionId);    
            myStatement.setString(2, Character.toString(choiceNumber));
            myStatement.setString(3, statement);
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void update() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement(
                "update questionChoice set choiceStatement = ? where questionID = ? AND choiceNumber = ?");
            myStatement.setString(1, statement);
            myStatement.setInt(2, questionId);    
            myStatement.setString(3, Character.toString(choiceNumber));
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
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

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getStatement() {
        if (!isFilled)
        {
            fillData();
        }
        return statement;
    }
      
}
