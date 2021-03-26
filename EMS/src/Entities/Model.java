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

public class Model implements SqlEntity {
    
    private final int examID, modelNumber;
    private Vector<Question> questions;
    private boolean isFilled;

    public Model(int examID, int modelNumber) {
        this.examID = examID;
        this.modelNumber = modelNumber;
    }

    public Vector<Question> getQuestions() {
       if (!isFilled) {
           fillData();
       }
        return questions;
    }

    public int getExamID() {
        return examID;
    }

    public int getModelNumber() {
        return modelNumber;
    }

    /**
     * generates the modelAnswer by storing the correct choices in a string TB used in the grading process
     * @return String — the model answer to this exam model
     */

    public String getModelAnswer() {
        if (!isFilled) {
            fillData();
        }

        String modelAnswer = "";

        for (Question question : questions) {
            modelAnswer += question.getCorrectChoice();
        }
        return modelAnswer;
    }

    /**
     * it fills all the data of the current Exam Model by executing multiple SQL statements
     */

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select QUESTIONID from QUESTION where EXAMID = ? AND MODELNUMBER = ?");
            myStatement.setInt(1, examID);
            myStatement.setInt(2, modelNumber);
            ResultSet myResultSet = myStatement.executeQuery();
            questions = new Vector<>();
            while (myResultSet.next()) {
                questions.add(new Question(myResultSet.getInt(1)));
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void add() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("insert into exammodel values (?,?)");
            myStatement.setInt(1, examID);
            myStatement.setInt(2, modelNumber);
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

}
