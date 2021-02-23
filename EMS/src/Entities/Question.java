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
public class Question implements SqlEntity, Comparable {

    private final int id;
    private int totalFrequency, correctFrequency, examId, modelNumber, classId;
    private char correctChoice;
    private boolean isFilled;
    private String statement;
    private Vector<QuestionChoice> choices;

    public Question(int id) {
        this.id = id;
        choices = new Vector<>();
    }

    public Question(int examId, int modelNumber, char correctChoice, String statement) {
        id = SqlEntity.generateID("QUESTIONIDSEQ");  
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
            PreparedStatement myStatement = myConnection.prepareStatement("select STATEMENT, MODELNUMBER, EXAMID from QUESTION where QUESTIONID = ?");
            myStatement.setInt(1, id);
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                statement = new String(myResultSet.getString(1));
                modelNumber = myResultSet.getInt(2);
                examId = myResultSet.getInt(3);
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
            choices.add(new QuestionChoice(id, choiceNumber));
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
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement(
                    "insert into  question (questionId, statement , modelNumber, examId , classId) "
                    + "values (?,?,?,?,?)");
            myStatement.setInt(1, id);
            myStatement.setString(2, statement);
            myStatement.setInt(3, modelNumber);
            myStatement.setInt(4, examId);
            myStatement.setInt(5, classId);
            myStatement.executeQuery();
            myStatement = myConnection.prepareStatement("insert into correctChoice"
                    + " (questionID , correctChoiceNumber) values (?, ?)");
            myStatement.setInt(1, id);
            myStatement.setString(2, String.valueOf(correctChoice));
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
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement(
                "update question set statement = ? where questionID = ?");
            myStatement.setString(1, statement);
            myStatement.setInt(2, id);
            myStatement.executeQuery();
            myStatement = myConnection.prepareStatement("update correctChoice"
                    + " set correctChoiceNumber = ? where questionID = ?");
            myStatement.setString(1, String.valueOf(correctChoice));
            myStatement.setInt(2, id);
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        for (QuestionChoice choice : choices) {
            choice.update();
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Vector<QuestionChoice> getChoices() {
        return choices;
    }

    public int getExamId() {
        if(!isFilled) {
            fillData();
        }
        return examId;
    }

    public char getCorrectChoice() {
        if(!isFilled) {
            fillData();
        }
        return correctChoice;
    }

    public void setCorrectChoice(char correctChoice) {
        this.correctChoice = correctChoice;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
    
    public int getModelNumber() {
        if(!isFilled) {
            fillData();
        }
        return modelNumber;
    }

    public int getId() {
        return id;
    }
    
    public double getSolvingRate() {
        if(!isFilled) {
            fillData();
        }
        if(totalFrequency == 0) {
            return 0;
        } else {
            return (double) correctFrequency / totalFrequency;
        }
        
    }

    @Override
    public int compareTo(Object t) {
        
        Question otherQuestion = (Question) t;
        if(getSolvingRate() < otherQuestion.getSolvingRate()) {
            return 1;
        } else if(getSolvingRate() > otherQuestion.getSolvingRate()) {
            return -1;
        } else {
            return 0;
        }
        
    }

}
