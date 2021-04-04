package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * An extended version of Entities.User.java that represent the student — basically the
 * main entity in which the system is based. The class implements SqlEntity to override
 * fillData() in order to use in executing SQL statements and data retrieval.
 *
 * @author Steven Sameh, AbdelAziz Mostafa, Yusuf Nasser, Youssef Nader
 */

public class Student extends User implements SqlEntity {

    private int id;
    private Vector<Class> myClasses;

    /**
     * Constructs a new student instance to store and hold its data
     * after it's been retrieved.
     *
     * @param username the student username that his or her data is in demand
     */

    public Student(String username) {
        super(username);
    }

    /**
     * It retrieves the student's info from the database
     */

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select id from student where username = ?");
            myStatement.setString(1, getUsername());
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                id = myResultSet.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        myClasses = new Vector<>();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT CLASSID FROM STUDENTREGISTER WHERE USERNAME = ?");
            myStatement.setString(1, super.getUsername());
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                myClasses.add(new Class(myResultSet.getInt(1), true));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void add() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns the student unique ID
     *
     * @return the value of the id property
     */

    public int getId() {
        if (!isFilled) {
            fillData();
        }
        return id;
    }

    /**
     * Returns the classes in which the current student is enrolled
     *
     * @return Vector<Class> myClasses — the classes of the current student
     */

    public Vector<Class> getClasses() {
        if (!isFilled) {
            fillData();
        }
        return myClasses;
    }


}
