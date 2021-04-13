package Entities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;
import java.sql.PreparedStatement;
import java.time.LocalDate;

/**
 * An extended version of Entities.User.java that represents the Instructor — the
 * entity that represents the tutor of a class. The class implements SqlEntity to
 * override fillData() in order to use in executing SQL statements and data retrieval
 * and add() in Inserting a new user into the database.
 *
 * @author Steven Sameh, AbdelAziz Mostafa, Ziad Khobeiz, Yusuf Nasser
 */

public class Instructor extends User implements SqlEntity {

    Vector<Class> classes;

    /**
     * It initializes a new instructor with the final unique username.
     *
     * @param username The username of the instructor
     */

    public Instructor(String username) {
        super(username);
    }

    /**
     * Constructs a new instructor to be added in the database via calling the
     * parent class corresponding constructor.
     *
     * @param username     the user's unique username
     * @param mobileNumber the user's registered mobile number
     * @param email        the user's contact email
     * @param firstName    the user's first name
     * @param middleName   the user's middle name
     * @param lastName     the user's last name
     * @param birthdate    the user's birthdate value
     */

    public Instructor(String username, String mobileNumber, String email, String firstName, String middleName, String lastName, LocalDate birthdate, String password) {
        super(username, mobileNumber, email, firstName, middleName, lastName, birthdate, password);
    }

    /**
     * It retrieves the classes of the instructor from the database
     */

    @Override
    public void fillData() {
        isFilled = true;
        classes = new Vector<>();
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT CLASSID FROM INSTRUCTOROF WHERE USERNAME = ?");
            myStatement.setString(1, super.getUsername());
            ResultSet myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                classes.add(new Class(myResultSet.getInt(1), false));
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a new user into the database proper table via executing a prepared sql statement.
     */

    @Override
    public void add() {
        super.addUser();
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("insert into instructor values(?)");
            myStatement.setString(1, super.getUsername());
            myStatement.executeQuery();
            myConnection.close();
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

    /**
     * Returns the classes that the instructor teaches.
     *
     * @return the value of the classes vector
     */

    public Vector<Class> getClasses() {
        if (!isFilled) fillData();
        return classes;
    }
}
