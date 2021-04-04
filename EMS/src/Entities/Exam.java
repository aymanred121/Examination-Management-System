package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.*;
import java.util.Collections;
import java.util.Vector;

/**
 * It represents the exam entity that holds the exam basic data, i.e. name, start and end time
 * along with the models that holds the questions.
 * It implements SqlEntity to override fillData(), add() and update() in order to use in executing
 * SQL statements for data retrieval, updates and insertions if needed.
 *
 * @author Steven Sameh, Ziad Khobeiz, Abdel-Aziz Mostafa, Yusuf Nasser, Youssef Nader, Ayman Hassan
 */

public class Exam implements SqlEntity {

    /* Naming convention of static final in java is in SCREAMING_SNAKE_CASE; reference: shorturl.at/dltGK */

    final static private int MODEL_NUMBER_LIMIT = 5, YEAR_LIMIT = 2051, MONTH_LIMIT = 12,
            HOUR_LIMIT = 24, MINUTES_LIMIT = 60, EXAM_DURATION_LIMIT = 180;

    private int id, numberOfModels;
    private Class examClass;
    private String name;
    private LocalDateTime startTime, endTime;
    private Vector<Model> models;
    private Duration duration;
    private boolean isFilled, isPublished, isTaken;

    /**
     * Constructs a new exam for the a specific class chosen
     *
     * @param examClass the class in which the exam is constructed
     */

    public Exam(Entities.Class examClass) {
        this.examClass = examClass;
    }

    /**
     * Constructs an exam instance to retrieve its data from the data base and
     * store them in its fields.
     *
     * @param id the unique id of the exam whose data would be retrieved
     */

    public Exam(int id) {
        this.id = id;
    }

    /**
     * All the possible statuses for the exam
     */

    public enum Status {
        // Exam is published and currently running
        RUNNING,

        // Exam is published and has not started yet
        UPCOMING,

        // Exam is published and has finished
        FINISHED,

        // Exam is unpublished and still under development
        UNPUBLISHED
    }

    // Entities.SqlEntity implemented methods

    /**
     * retrieve all the Exam data from the Database
     *
     * @author Ayman Hassan, Ziad Khobeiz
     */

    @Override
    public void fillData() {
        isFilled = true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select STARTTIME, ENDTIME, NAME, CLASSID, isPublished FROM EXAM where EXAMID = ?");
            myStatement.setInt(1, id);
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                startTime = myResultSet.getTimestamp(1).toLocalDateTime();
                endTime = myResultSet.getTimestamp(2).toLocalDateTime();
                name = myResultSet.getString(3);
                examClass = new Class(myResultSet.getInt(4), false);
                isPublished = myResultSet.getString(5).equals("Y");
                duration = Duration.between(startTime, endTime);
            }
            PreparedStatement modelsStatement = myConnection.prepareStatement("select distinct MODELNUMBER FROM EXAMMODEL where EXAMID = ?");
            modelsStatement.setInt(1, id);
            ResultSet modelsResultSet = modelsStatement.executeQuery();

            models = new Vector<>();

            while (modelsResultSet.next()) {
                models.add(new Model(id, modelsResultSet.getInt(1)));
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Adds a new exam to the database
     */

    @Override
    public void add() {
        id = SqlEntity.generateID("EXAMIDSEQ");
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("insert into exam values (?,?,?,?,?,?)");
            myStatement.setInt(1, id);
            myStatement.setTimestamp(2, Timestamp.valueOf(startTime));
            myStatement.setTimestamp(3, Timestamp.valueOf(endTime));
            myStatement.setInt(4, examClass.getId());
            myStatement.setString(5, name);
            myStatement.setString(6, "N");
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
        for (int modelNumber = 1; modelNumber <= numberOfModels; ++modelNumber) {
            // Adds a new model to the models vector .add() is an associated method with java.util.Vector Class
            models.add(new Model(id, modelNumber));
            // adds the new model to the database .add is an implemented method in Entities.Model Class
            models.lastElement().add();
        }
    }

    /**
     * Updates the existent exam data in the database
     */

    @Override
    public void update() {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("update exam set ispublished = ?, name = ?, starttime = ?, endtime = ? where examid = ?");
            myStatement.setString(1, isPublished ? "Y" : "N");
            myStatement.setString(2, name);
            myStatement.setTimestamp(3, Timestamp.valueOf(startTime));
            myStatement.setTimestamp(4, Timestamp.valueOf(endTime));
            myStatement.setInt(5, id);
            myStatement.executeQuery();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Currently there's no implementation for it as functionality for
     * deleting an exam from the database is yet to be implemented if needed
     */

    @Override
    public void delete() {

    }

    // Exam properties setters

    /**
     * Sets the start time of the exam instance to the passed argument value
     *
     * @param startTime the new start time for the exam
     */

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Sets the end time of the exam instance to the passed argument value
     *
     * @param endTime the new end time for the exam
     */

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Sets the duration of the exam instance to the passed argument value
     *
     * @param duration the new duration for the exam
     */

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     * Sets the name of the exam instance to the passed argument value
     *
     * @param name the name for the exam
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the state of the exam instance to the passed argument value
     *
     * @param isPublished the new state for the exam
     */

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    /**
     * Sets the number of models of the exam instance to the passed argument value
     *
     * @param numberOfModels the number of models for the exam
     */

    public void setNumberOfModels(int numberOfModels) {
        this.numberOfModels = numberOfModels;
    }

    // Exam properties getters

    /**
     * Process all the exam questions from its models and sort them descendingly
     * according to their solving rates, returns from index 0 and up to but not
     * equal the desired size index
     *
     * @param size how many top questions desired
     * @return vector of highest solving rate questions with the size desired
     */

    public Vector<Question> getTopQuestions(int size) {
        if (!isFilled) {
            fillData();
        }

        Vector<Question> topQuestions = new Vector<>();
        for (Model model : models) {
            topQuestions.addAll(model.getQuestions());
        }

        Collections.sort(topQuestions);
        topQuestions.setSize(size);

        return topQuestions;
    }

    /**
     * Returns the currently set MODEL_NUMBER_LIMIT for the whole class
     *
     * @return the limit on how many models allowed for one exam
     */

    public static int getModelNumberLimit() {
        return MODEL_NUMBER_LIMIT;
    }

    /**
     * Returns the currently set YEAR_LIMIT for the whole class
     *
     * @return the limit year allowed for a starting date when creating an exam
     */

    public static int getYearLimit() {
        return YEAR_LIMIT;
    }

    /**
     * Returns the currently set MONTH_LIMIT for the whole class
     *
     * @return the limit month allowed for a starting date when creating an exam
     */

    public static int getMonthLimit() {
        /*
         * it might seem naive to even create a limit on months but the argument
         * here that some systems might prohibit exams after a certain month.
         */
        return MONTH_LIMIT;
    }

    /**
     * Returns the currently set HOUR_LIMIT for the whole class
     *
     * @return the limit hour allowed for a starting time when creating an exam
     */

    public static int getHourLimit() {
        return HOUR_LIMIT;
    }

    /**
     * Returns the currently set MINUTES_LIMIT for the whole class
     *
     * @return the max minutes allowed for a starting time when creating an exam
     */

    public static int getMinutesLimit() {
        return MINUTES_LIMIT;
    }

    /**
     * Returns the currently set EXAM_DURATION_LIMIT for the whole class
     *
     * @return the max duration allowed for an exam when creating one
     */

    public static int getExamDurationLimit() {
        return EXAM_DURATION_LIMIT;
    }

    /**
     * compares the student Answers to the modelAnswer and set the score
     *
     * @return studentMark student score in the taken exam model
     */

    public int getStudentMark(String studentName) {
        if (!isFilled) {
            fillData();
        }

        int studentMark = 0, index = getStudentModelIndex(studentName);
        String modelAnswer = models.elementAt(index).getModelAnswer(),
                studentAnswer = getStudentAnswer(studentName);

        if (studentAnswer.length() == 0) {
            return 0;
        }

        for (int i = 0; i < modelAnswer.length(); i++) {
            if (modelAnswer.charAt(i) == studentAnswer.charAt(i))
                studentMark++;
        }
        return studentMark;
    }

    /**
     * Determines whether a student has taken on and finished an ongoing exam or not
     *
     * @param studentName the unique identifier for the student
     */

    private void getIsTaken(String studentName) {
        if (!isFilled) {
            fillData();
        }

        int index = getStudentModelIndex(studentName);
        Connection myConnection = SqlConnection.getConnection();
        try {
            // Trying to retrieve any existent student answers for this exam from SOLVE TABLE
            for (Question question : models.elementAt(index).getQuestions()) {
                PreparedStatement retrieveStudentAnswerStatement = myConnection.prepareStatement("select STUDENTCHOICE from SOLVE where QUESTIONID = ? AND USERNAME = ?");
                retrieveStudentAnswerStatement.setInt(1, question.getId());
                retrieveStudentAnswerStatement.setString(2, studentName);
                ResultSet studentChoiceSet = retrieveStudentAnswerStatement.executeQuery();

                if (studentChoiceSet.next()) {
                    isTaken = true;
                    break;
                }
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * returns the exam id
     *
     * @return the value of the id property
     */

    public int getId() {
        return id;
    }

    /**
     * returns the class to which the exam belongs
     *
     * @return the value of examClass property after retrieving it from the database
     */

    public Class getExamClass() {
        if (!isFilled) {
            fillData();
        }
        return examClass;
    }

    /**
     * returns the total mark of the exam which is the same as the
     * questions number at one model as MCQ questions all value the same
     *
     * @return the size of questions vector at the first model; after retrieving its value from the database
     */

    public int getTotalMark() {
        if (!isFilled) {
            fillData();
        }
        return this.models.firstElement().getQuestions().size();
    }

    /**
     * returns the studentModelIndex by calculating the outcome of a predefined equation
     *
     * @param username the username of the student
     * @return the calculated studentModelIndex
     */

    public int getStudentModelIndex(String username) {
        if (!isFilled) {
            fillData();
        }

        // Handling the ceiling process without dealing with floats
        int studentsPerModel = (examClass.getStudentsCount() + models.size() - 1) / models.size();
        int studentPos = examClass.getStudentPosition(username);
        int studentModel = studentPos / studentsPerModel;

        return studentModel;
    }

    /**
     * Returns the student answers for this exam after retrieving all his or here choices from
     * the database and appends them all to a string
     *
     * @param studentName the username of the student whose exam answers shall be retrieved
     * @return the value of studentAnswer String after retrieving all answers from the database
     */

    public String getStudentAnswer(String studentName) {
        if (!isFilled) {
            fillData();
        }

        int index = getStudentModelIndex(studentName);
        String studentAnswer = "";
        Connection myConnection = SqlConnection.getConnection();
        try {
            // Retrieving student answers from SOLVE TABLE and building studentAnswer string
            for (Question question : models.elementAt(index).getQuestions()) {
                PreparedStatement retrieveStudentAnswerStatement = myConnection.prepareStatement("select STUDENTCHOICE from SOLVE where QUESTIONID = ? AND USERNAME = ?");
                retrieveStudentAnswerStatement.setInt(1, question.getId());
                retrieveStudentAnswerStatement.setString(2, studentName);
                ResultSet studentChoiceSet = retrieveStudentAnswerStatement.executeQuery();

                while (studentChoiceSet.next()) {
                    studentAnswer += studentChoiceSet.getString(1);
                }
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return studentAnswer;
    }

    /**
     * Returns the start time of the exam after retrieving its value from the database
     *
     * @return the value of the startTime property
     */

    public LocalDateTime getStartTime() {
        if (!isFilled) {
            fillData();
        }
        return startTime;
    }

    /**
     * Returns the end time of the exam after retrieving its value from the database
     *
     * @return the value of the endTime property
     */

    public LocalDateTime getEndTime() {
        if (!isFilled) {
            fillData();
        }
        return endTime;
    }

    /**
     * Returns the exam duration after retrieving its value from the database
     *
     * @return the value of the duration property
     */

    public Duration getDuration() {
        if (!isFilled) {
            fillData();
        }
        return duration;
    }

    /**
     * Returns the models vector of the exam
     *
     * @return the value of models property after retrieving it from the database
     */

    public Vector<Model> getModels() {
        if (!isFilled) {
            fillData();
        }
        return models;
    }

    /**
     * It determines whether the exam is currently running, upcoming, finished or unpublished
     *
     * @return Status The current status of the exam
     */

    public Status getStatus() {
        if (!isFilled) {
            fillData();
        }

        if (!isPublished) {
            return Status.UNPUBLISHED;
        }

        if (startTime.compareTo(java.time.LocalDateTime.now()) > 0) {
            return Status.UPCOMING;
        }

        if (endTime.compareTo(java.time.LocalDateTime.now()) < 0) {
            return Status.FINISHED;
        }

        return Status.RUNNING;
    }

    /**
     * Returns the exam status for a certain student after checking with the exam
     * and the student data in the database.
     *
     * @param studentName the student instance to be checked for their exam status
     * @return the exam status for the student in question
     */

    public Status getStudentStatus(String studentName) {
        if (!isFilled) {
            fillData();
        }

        getIsTaken(studentName);

        if (isTaken) {
            return Status.FINISHED;
        }

        if (startTime.compareTo(java.time.LocalDateTime.now()) > 0) {
            return Status.UPCOMING;
        }

        if (endTime.compareTo(java.time.LocalDateTime.now()) < 0) {
            return Status.FINISHED;
        }

        return Status.RUNNING;
    }

    /**
     * Returns the exam name after retrieving it from the database
     *
     * @return the value of the name property
     */

    public String getName() {
        if (!isFilled) {
            fillData();
        }
        return name;
    }

    /**
     * It checks whether all the models of the exam have the same positive number of question
     * and the exam start time is upcoming (i.e. the exam is ready to be published)
     *
     * @return true if the exam state is suitable for publicity; false otherwise.
     */

    public boolean isReadyToPublish() {
        if (models.size() == 0) {
            return false;
        }
        for (int i = 1; i < models.size(); ++i) {
            if (models.elementAt(i).getQuestions().size() == 0 ||
                    models.elementAt(i).getQuestions().size() != models.elementAt(i - 1).getQuestions().size()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets isPublished to true and update the exam publicity state in the database
     */

    public void publish() {
        isPublished = true;
        update();
    }
}
