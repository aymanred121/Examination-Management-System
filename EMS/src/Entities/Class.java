package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Represents the College Class — A certain number of students registered in a certain course
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
     * @param id               the class id desired to retrieve its data
     * @param isStudentSession Identifying the current session
     */

    public Class(int id, boolean isStudentSession) {
        this.isStudentSession = isStudentSession;
        this.id = id;
    }

    /**
     * constructs a class instance to be used in the creation of a new
     * class that will later be inserted in the database.
     *
     * @param id     the new class's id
     * @param course the course that the class teaches
     */

    public Class(int id, Course course) {
        this.id = id;
        this.course = course;
        this.isStudentSession = false;
    }

    /**
     * it fills all the data of the current class by executing multiple SQL statements
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

        try {
            // Retrieving the students assigned to the class
            PreparedStatement studentsStatement = myConnection.prepareStatement("SELECT USERNAME FROM STUDENTREGISTER WHERE CLASSID = ?");
            studentsStatement.setInt(1, id);
            myResultSet = studentsStatement.executeQuery();
            while (myResultSet.next()) {
                students.add(new Student(myResultSet.getString(1)));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        if (!isStudentSession) {
            try {
                // Retrieving the instructors assigned to the class
                PreparedStatement instructorsStatement = myConnection.prepareStatement("SELECT USERNAME FROM INSTRUCTOROF WHERE CLASSID = ?");
                instructorsStatement.setInt(1, id);
                myResultSet = instructorsStatement.executeQuery();
                while (myResultSet.next()) {
                    instructors.add(new Instructor(myResultSet.getString(1)));
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

    /**
     * Adds a new class to the database via its unique ID and course code.
     */

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

    /**
     * returns all the exams in class
     *
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
     *
     * @return course member variable
     */

    public Course getCourse() {
        if (!isFilled) {
            fillData();
        }
        return course;
    }

    /*
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
     *
     * @return how many students registered in class
     */

    public int getStudentsCount() {
        if (!isFilled)
            fillData();
        return students.size();
    }

    /**
     * returns where the student is located in class
     *
     * @param username the username of the student
     * @return student's index in students vector
     */

    public int getStudentPosition(String username) {
        if (!isFilled)
            fillData();

        int studentPosition = 0;
        while (studentPosition < students.size()) {
            if (username.equals(students.elementAt(studentPosition).getUsername()))
                break;
            studentPosition++;
        }
        return studentPosition;
    }

    /**
     * Checks whether a class already exists with the same ID in the database
     *
     * @param classId The ID of the new class trying to add in database
     * @return true if the class ID is already added to the database; false otherwise.
     * @author Steven Sameh and Abdel-Aziz Mostafa
     */

    public static boolean doesClassIdExist(int classId) {
        boolean doesExist = false;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("select count(*) from class where id = ?");
            SQLStatement.setInt(1, classId);
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
     * Assigns an instructor to class
     *
     * @param username the username of the instructor trying to assign to class
     * @param classId  the classId of the class
     */

    public static void assignInstructor(String username, int classId) {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement SQLStatement;
            SQLStatement = myConnection.prepareStatement("insert into instructorof values(?,?)");
            SQLStatement.setString(2, username);
            SQLStatement.setInt(1, classId);
            SQLStatement.executeQuery();
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
