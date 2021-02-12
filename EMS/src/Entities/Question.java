/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author bizarre
 */
public class Question implements SqlEntity {
    
    private final int id;
    private int totalFrequency, correctFrequency;
    private char correctChoice;
    private boolean isFilled;
    private String statement;
    private Vector<String> choices;
    
    public Question(int id) {
        this.id = id;
    }

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select STATEMENT from QUESTION where QUESTIONID = ?");
            myStatement.setInt(1, id);
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                statement = new String(myResultSet.getString(1));
            }
            PreparedStatement choicesStatement = myConnection.prepareStatement("select CHOICESTATEMENT from QUESTIONCHOICE where QUESTIONID = ? order by CHOICENUMBER");
            choicesStatement.setInt(1, id);
            ResultSet choicesResultSet = choicesStatement.executeQuery();
            choices = new Vector<String>();
            while (choicesResultSet.next()) {
                choices.add(new String(choicesResultSet.getString(1)));
            }
            PreparedStatement correctChoicesStatement = myConnection.prepareStatement("select CORRECTCHOICENUMBER from CORRECTCHOICE where QUESTIONID = ?");
            correctChoicesStatement.setInt(1, id);
            ResultSet correctChoicesResultSet = correctChoicesStatement.executeQuery();
            if (correctChoicesResultSet.next()) {
                correctChoice = correctChoicesResultSet.getString(1).charAt(0);
            }
            PreparedStatement totalFrequencyStatement = myConnection.prepareStatement("select count(*) from SOLVE where QUESTIONID = ?");
            totalFrequencyStatement.setInt(1, id);
            ResultSet totalFrequencyResultSet = totalFrequencyStatement.executeQuery();
            if (totalFrequencyResultSet.next()) {
                totalFrequency = totalFrequencyResultSet.getInt(1);
            }
            PreparedStatement correctFrequencyStatement = myConnection.prepareStatement("select count(*) from SOLVE where QUESTIONID = ? AND STUDENTCHOICE = ?");
            correctFrequencyStatement.setInt(1, id);
            correctFrequencyStatement.setString(2, Character.toString(correctChoice));
            ResultSet correctFrequencyResultSet = correctFrequencyStatement.executeQuery();
            if (correctFrequencyResultSet.next()) {
                correctFrequency = totalFrequencyResultSet.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        } 
    }

    public String getStatement() {
        if (!isFilled)
        {
            fillData();
        }
        return statement;
    }

    @Override
    public void add() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override   
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
