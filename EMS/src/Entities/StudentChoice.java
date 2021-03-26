package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * It represents the Student Solution in a certain question in a certain exam model
 * It implements SqlEntity to override add() in order to use it in
 * executing SQL statements and data insertions
 *
 * @author Yusuf Nasser, Youssef Nader
 * @since Mar 22nd, 2021 08:07
 */
public class StudentChoice implements SqlEntity{
    final int questionId;
    final char choiceNumber;
    final String username;

    /**
     * it constructs a new student choice to be inserted in the database
     * @param questionId — the question answered
     * @param choiceNumber — the chosen choiceNumber
     * @param username — the student username
     */

    public StudentChoice(int questionId, char choiceNumber, String username) {
        this.questionId = questionId;
        this.choiceNumber = choiceNumber;
        this.username = username;
    }

    @Override
    public void fillData() {}

    /**
     * Inserts the student solution into solve table
     */

    @Override
    public void add() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("insert into SOLVE values(?, ?, ?)");
            myStatement.setInt(1, questionId);
            myStatement.setString(2, username);
            myStatement.setString(3, String.valueOf(choiceNumber));
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void update() {}

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
