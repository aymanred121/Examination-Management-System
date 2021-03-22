package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * It represents the College Class — A certain number of students registered in a certain course
 * It implements SqlEntity to override fillData() in order to use in executing SQL statements and
 * data retrieval and insertions if existed
 *
 * @author Steven Sameh, Ziad Khobeiz, Abdel-Aziz Mostafa, Yusuf Nasser, Youssef Nader
 * @since Jan 4th, 2021 19:41
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

    /**
     * constructs a new class by initializing its member variables
     *
     * @param id the class id desired to retrieve its data
     * @param isStudentSession ID'ing the current session
     */

    public Class(int id, boolean isStudentSession) {
        this.isStudentSession = isStudentSession;
        this.id = id;
    }

    /**
     * it fills all the data of the current class by executing multiple SQL statements
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

    /**
     * returns all the exams in class
     * @return vector of exams existed in class
     */

    public Vector<Exam> getExams() {
        if (!isFilled) {
            fillData();
        }
        return exams;
    }

    /**
     * returns the course in which the class is specialized
     * @return course member variable
     */

    public Course getCourse() {
        if (!isFilled) {
            fillData();
        }
        return course;
    }

    /**
     * returns the current class id
     * @return class id
     */

    public int getId() {
        if (!isFilled) {
            fillData();
        }
        return id;
    }

    /**
     * returns registered students count
     * @return how many students registered in class
     */

    public int getStudentsCount() {
        if (!isFilled)
            fillData();
        return students.size();
    }

    /**
     * returns where the student is located in class
     * @param username the username of the student
     * @return student's index in students vector
     */

    public int getStudentPosition(String username) {
        if (!isFilled)
            fillData();

        int studentPosition = 0;
        while (studentPosition < students.size())
        {
            if (username.equals(students.elementAt(studentPosition).getUsername()))
                break;
            studentPosition++;
        }
        return studentPosition;
    }

}
