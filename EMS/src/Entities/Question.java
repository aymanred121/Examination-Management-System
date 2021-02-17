/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;

/**
 *
 * @author bizarre
 */
public class Question implements SqlEntity {

    private int id, totalFrequency, correctFrequency, examId, modelNumber, classId;
    private char correctChoice;
    private boolean isFilled;
    private String statement;
    private Vector<QuestionChoice> choices;

    public Question(int id) {
        this.id = id;
        choices = new Vector<>();
    }

    public Question(int examId, int modelNumber, char correctChoice, String statement) {
        this.examId = examId;
        this.modelNumber = modelNumber;
        this.correctChoice = correctChoice;
        this.statement = statement;
        this.classId = new Exam(examId).getExamClass().getId();
        choices = new Vector<>();
    }

    // Both examID and modelNumber should be retrieved from database
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
        for (char choiceNumber = 'a'; choiceNumber <= 'd'; choiceNumber++) {
            choices.add(new QuestionChoice(examId, choiceNumber));
        }
    }

    public String getStatement() {
        if (!isFilled) {
            fillData();
        }
        return statement;
    }

    @Override
    public void add() {
        id = SqlEntity.generateID("QUESTIONIDSEQ");   
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement(
                    "insert into  question (questionId, statement , modelNumber, examId , classId)\n"
                    + "values (?,?,?,?,?)");
            myStatement.setInt(1, id);
            myStatement.setString(2, statement);
            myStatement.setInt(3, modelNumber);
            myStatement.setInt(4, examId);
            myStatement.setInt(5, classId);
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        for (QuestionChoice choice : choices) {
            choice.add();
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

    public Vector<QuestionChoice> getChoices() {
        return choices;
    }

    public int getExamId() {
        return examId;
    }

    public int getModelNumber() {
        return modelNumber;
    }

}
