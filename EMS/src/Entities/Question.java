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
 * @author Steven Sameh
 */

public class Question implements SqlEntity, Comparable<Question> {

    private final int id;
    private int totalFrequency, correctFrequency, examId, modelNumber, classId;
    private char correctChoice;
    private boolean isFilled;
    private String statement;
    private final Vector<QuestionChoice> choices; // final ONLY prevents the vector from being reassigned after initialization

    /**
     * Constructs a question instance to hold its retrieved data
     *
     * @param id the id of the question to retrieve its data
     */

    public Question(int id) {
        this.id = id;
        choices = new Vector<>();
    }

    /**
     * Constructs a new question instance to store the newly created question info.
     *
     * @param examId        the id of the exam to which the model containing the question belongs
     * @param modelNumber   the number of the model containing the question
     * @param correctChoice the question's correctChoice letter
     * @param statement     the statement of the question
     */

    public Question(int examId, int modelNumber, char correctChoice, String statement) {
        id = SqlEntity.generateID("QUESTIONIDSEQ");
        this.examId = examId;
        this.modelNumber = modelNumber;
        this.correctChoice = correctChoice;
        this.statement = statement;
        this.classId = new Exam(examId).getExamClass().getId();
        choices = new Vector<>();
    }

    /**
     * Retrieves the question data depending on its provided unique id
     */

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select STATEMENT, MODELNUMBER, EXAMID from QUESTION where QUESTIONID = ?");
            myStatement.setInt(1, id);
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                statement = myResultSet.getString(1);
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

    /**
     * Inserts a new question to the database
     */

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

    /**
     * Updates an existent question's data in the database
     */

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

    /**
     * Sets the correct choice letter to the passed argument value.
     *
     * @param correctChoice the new value for the correct choice letter
     */

    public void setCorrectChoice(char correctChoice) {
        this.correctChoice = correctChoice;
    }

    /**
     * Sets the question statement to the passed argument value.
     *
     * @param statement the new question's statement
     */

    public void setStatement(String statement) {
        this.statement = statement;
    }

    /**
     * Returns the question's choices containing vector
     *
     * @return the value of choices vector
     */

    public Vector<QuestionChoice> getChoices() {
        if (!isFilled) {
            fillData();
        }
        return choices;
    }

    /**
     * Returns the id of the exam to which the model containing the question belongs
     *
     * @return value of the examId field
     */

    public int getExamId() {
        if (!isFilled) {
            fillData();
        }
        return examId;
    }

    /**
     * Returns the correctChoice letter
     *
     * @return value of correctChoice field
     */

    public char getCorrectChoice() {
        if (!isFilled) {
            fillData();
        }
        return correctChoice;
    }

    /**
     * Returns the number of the model to which the question belongs
     *
     * @return value of the modelNumber property
     */

    public int getModelNumber() {
        if (!isFilled) {
            fillData();
        }
        return modelNumber;
    }

    /**
     * Returns the unique ID for the question.
     *
     * @return the value of the id property
     */

    public int getId() {
        return id;
    }

    /**
     * Returns the statement of the question
     *
     * @return value of the statement field
     */

    public String getStatement() {
        if (!isFilled) {
            fillData();
        }
        return statement;
    }

    /**
     * Returns the question's solving rate by dividing how many times students
     * got it correct over how many times students experienced that question.
     *
     * @return the double value of (correctFrequency / totalFrequency);
     */

    public double getSolvingRate() {
        if (!isFilled) {
            fillData();
        }
        if (totalFrequency == 0) {
            return 0;
        } else {
            return (double) correctFrequency / totalFrequency;
        }

    }

    /**
     * Compares this question to another question.
     * The Comparison is mainly used to sort a vector of questions descendingly.
     *
     * @param otherQuestion the second party for the comparison
     * @return the returned value of the double comparison between otherQuestion
     * and this question, positive if less and negative is greater
     */

    @Override
    public int compareTo(Question otherQuestion) {
        return Double.compare(otherQuestion.getSolvingRate(), getSolvingRate());
    }

}
