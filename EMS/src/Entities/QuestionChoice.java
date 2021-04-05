package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Represents a choice for a question in an exam model, Implements SqlEntity
 *
 * @author AbdelAziz Mostafa
 */
public class QuestionChoice implements SqlEntity {
    private final int questionId;
    private final char choiceNumber;
    private String statement;
    private boolean isFilled;

    /**
     * Constructs a new QuestionChoice instance and providing it with all the member fields
     *
     * @param questionId   the question to which the choice belongs
     * @param choiceNumber the unique number of the choice between other choices
     * @param statement    the choice statement
     */

    public QuestionChoice(int questionId, char choiceNumber, String statement) {
        this.questionId = questionId;
        this.choiceNumber = choiceNumber;
        this.statement = statement;
    }

    /**
     * Constructs an instance of QuestionChoice to hold its data after retrieval
     *
     * @param questionId   the question to which the choice belongs
     * @param choiceNumber the unique number of the choice between other choices
     */

    public QuestionChoice(int questionId, char choiceNumber) {
        this.questionId = questionId;
        this.choiceNumber = choiceNumber;
    }

    /**
     * Retrieves all the existent data of a question choice.
     */

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select choiceStatement from questionChoice where QUESTIONID = ? AND choiceNumber = ?");
            myStatement.setInt(1, questionId);
            myStatement.setString(2, Character.toString(choiceNumber));
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                statement = new String(myResultSet.getString(1));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a new question choice to the choices table in the database
     */

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

    /**
     * Update an existent choice statement
     */

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

    /**
     * Sets the value of the statement property to the passed argument value.
     *
     * @param statement the value to set the statement property to.
     */

    public void setStatement(String statement) {
        this.statement = statement;
    }

    /**
     * Returns the statement property value after making sure it's retrieved.
     *
     * @return the value of the statement property
     */

    public String getStatement() {
        if (!isFilled) {
            fillData();
        }
        return statement;
    }

}
