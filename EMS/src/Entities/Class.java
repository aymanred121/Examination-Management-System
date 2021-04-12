package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 *
 * @author Steven, Yusuf Nasser 19:41 4th, Jan, 2021
 */
public class Class implements SqlEntity {

    /*
     * All the attributes of the class
     */
    final private int id;
    final private boolean isStudentSession;
    private Course course;
    private Vector<Student> students;
    private Vector<Instructor> instructors;
    private Vector<Topic> topics;
    private Vector<Exam> exams;
    private boolean isFilled;
    
    public Class(int id, boolean isStudentSession) {
        this.isStudentSession = isStudentSession;
        this.id = id;
    }

    public Class(int id, Course course) {
        this.id = id;
        this.course = course;
        this.isStudentSession = false;
    }
    
    public int getId() {
        if (!isFilled) {
            fillData();
        }
        return id;
    }
    
    /**
     * it fills all the data of the current class
     */
    @Override
    public void fillData() {
        isFilled = true;
         
        students = new Vector<>();
        instructors = new Vector<>();
        topics = new Vector<>();
        exams = new Vector<>();
        Connection myConnection = SqlConnection.getConnection();
        ResultSet myResultSet;
        PreparedStatement codeStatement;
        if (!isStudentSession) {
            try {
                // Retrieving the instructors assigned to the class
                PreparedStatement instructorsStatement = myConnection.prepareStatement("SELECT USERNAME FROM INSTRUCTOROF WHERE CLASSID = ?");
                instructorsStatement.setInt(1, id);
                myResultSet = instructorsStatement.executeQuery();
                while (myResultSet.next()) {
                    instructors.add(new Instructor(myResultSet.getString(1)));
                }
                // Retrieving the students assigned to the class
                PreparedStatement studentsStatement = myConnection.prepareStatement("SELECT USERNAME FROM STUDENTREGISTER WHERE CLASSID = ?");
                studentsStatement.setInt(1, id);
                myResultSet = studentsStatement.executeQuery();
                while (myResultSet.next()) {
                    students.add(new Student(myResultSet.getString(1)));
                }
                // Retrieving the topics of the class
                PreparedStatement topicsStatement = myConnection.prepareStatement("SELECT NAME FROM TOPIC WHERE CLASSID = ?");
                topicsStatement.setInt(1, id);
                myResultSet = topicsStatement.executeQuery();
                while (myResultSet.next()) {
                    topics.add(new Topic(id, myResultSet.getString(1)));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        try {
            // Retrieving the course code of the class
            codeStatement = myConnection.prepareStatement("SELECT COURSECODE FROM CLASS WHERE ID = ?");
            codeStatement.setInt(1, id);
            myResultSet = codeStatement.executeQuery();
            while (myResultSet.next()) {
                course = new Course(myResultSet.getString(1));
            }
            // Retrieving the exams of the class
            PreparedStatement examsStatement = myConnection.prepareStatement("SELECT EXAMID FROM EXAM WHERE CLASSID = ? ORDER BY STARTTIME");
            examsStatement.setInt(1, id);
            myResultSet = examsStatement.executeQuery();
            while (myResultSet.next()) {
                exams.add(new Exam(myResultSet.getInt(1)));
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
            PreparedStatement myStatement = myConnection.prepareStatement("insert into class values (?,?)");
            myStatement.setInt(1, id);
            myStatement.setString(2, course.getCourseCode());
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

    /*
     * Implementing all the functions of the class
     */
    public Vector<Exam> getExams() {
        if (!isFilled) {
            fillData();
        }
        return exams;
    }

    public Course getCourse() {
        if (!isFilled) {
            fillData();
        }
        return course;
    }

    /**
     * Checks whether a class already exists with the same ID in the database
     *
     * @param classId The ID of the new class trying to add in database
     * @return true if the class ID is already added to the database; false otherwise.
     * @author Steven Sameh and Abdel-Aziz Mostafa
     */

    public static boolean doesClassIdExist(int classId){
        boolean doesExist = false;
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("select count(*) from class where id = ?");
            SQLStatement.setInt(1, classId);
            ResultSet myResultSet = SQLStatement.executeQuery();
            if(myResultSet.next() && myResultSet.getInt(1) >  0) {
                doesExist = true;
            }
            myConnection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
        return doesExist;
    }

    /**
     * Assigns an instructor to class 
     * @param username the username of the instructor trying to assign to class
     * @param classId  the classId of the class 
     */
    public static void assignInstructor( String username , int classId)
    {
        Connection myConnection = SqlConnection.getConnection();
        try{
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("insert into instructorof values(?,?)");
            SQLStatement.setString(2, username);
            SQLStatement.setInt(1, classId);
            SQLStatement.executeQuery();
            myConnection.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
