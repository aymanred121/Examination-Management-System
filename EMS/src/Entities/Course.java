/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Represents a course (subject) which is identified via a unique
 * code and a course name, it does not contain a syllabus for the
 * course topics as it differs from a class to another.
 * <p></p>
 * Implements SqlEntity to override fillData() for course information
 * retrieval from the database and add() for inserting new courses into
 * the database.
 *
 * @author Abdel-Aziz Mostafa, Steven Sameh, Yusuf Nasser
 */

public class Course implements SqlEntity {

    private final String courseCode;
    private String courseName;
    private boolean isFilled;

    /**
     * Constructs a course instance in which the new course data will
     * be stored before adding the new course to the database.
     *
     * @param courseCode the new course's code
     * @param name       the new course's name
     */

    public Course(String courseCode, String name) {
        this.courseCode = courseCode;
        this.courseName = name;
    }

    /**
     * Constructs a course instance that holds an existent
     * course in the database
     *
     * @param courseCode the code of which course will be retrieved
     */

    public Course(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Retrieves the course name from the course table existing in the project database
     */

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("SELECT COURSENAME FROM COURSE WHERE COURSECODE = ?");
            myStatement.setString(1, courseCode);
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                courseName = myResultSet.getString(1);
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a new course via inserting it to database — the process is ordered by the system's admin
     */

    @Override
    public void add() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("insert into COURSE values(?,?)");
            myStatement.setString(1, courseCode);
            myStatement.setString(2, courseName);
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

    /**
     * Returns the course name after retrieving it from the database
     *
     * @return the value of courseName property
     */

    public String getName() {
        if (!isFilled) {
            fillData();
        }
        return courseName;
    }

    /**
     * Checks whether a course already exists with the same course code in the database
     *
     * @param courseCode The courseCode of the course trying to add in database
     * @return true if the course code is already added to the database; false otherwise.
     * @author Steven Sameh and Abdel-Aziz Mostafa
     */

    public static boolean doesCourseCodeExist(String courseCode) {
        boolean doesExist = false;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("select count(*) from course where coursecode = ?");
            SQLStatement.setString(1, courseCode);
            ResultSet myResultSet = SQLStatement.executeQuery();
            if (myResultSet.next() && myResultSet.getInt(1) > 0) {
                doesExist = true;
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return doesExist;
    }

    /**
     * Returns the course unique code
     *
     * @return the value of courseCode property
     */

    public String getCourseCode() {
        return courseCode;
    }
}
