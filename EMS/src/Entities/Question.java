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
    private Vector<Character> correctChoices;
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
                statement = myResultSet.getString(1);
            }
            PreparedStatement choicesStatement = myConnection.prepareStatement("select CHOICESTATEMENT from QUESTIONCHOICE where QUESTIONID = ? order by CHOICENUMBER");
            choicesStatement.setInt(1, id);
            ResultSet choicesResultSet = choicesStatement.executeQuery();
            while (choicesResultSet.next()) {
                choices.add(new String(choicesResultSet.getString(1)));
            }
        } catch (Exception e) {
            System.out.println(e);
        } 
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
