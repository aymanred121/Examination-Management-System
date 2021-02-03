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

    public int getId() {
        if (!isFilled) {
            fillData();
        }
        return id;
    }

    /**
     * Implementing all the functions of the interface
     */
    /**
     * it fills all the data of the current class
     */
    @Override
    public void fillData() {
        isFilled = true;
         
        students = new Vector<Student>();
        instructors = new Vector<Instructor>();
        topics = new Vector<Topic>();
        exams = new Vector<Exam>();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

}
