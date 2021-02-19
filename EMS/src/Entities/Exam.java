/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;
import java.util.Vector;
import javax.xml.transform.Result;
import javax.xml.transform.Source;

/**
 *
 * @author yn653, Steven Sameh, Yusuf Nasser, Ayman Hassasn
 */
public class Exam implements SqlEntity {

    /*
     TODO
      Initialize variables in the constructor to avoid null pointer exceptions
     */

    /*
     * All the attributes of the Exam Class   
    */
    // TBC
    /** Yusuf Nasser writing, Ayman Hassan on Zoom 10th Jan 2021, 16:18
     * Naming convention of static final in java is in SCREAMING_SNAKE_CASE
     * reference: shorturl.at/dltGK
     * Variable was made by Yusuf Nader, He's more equipped to answer for that
     * but yet it's obvious, it's made to limit the number of questions an examiner
     * can input in a test.
    */
    
    final static private int MAX_QUESTION = 50, MODEL_NUMBER_LIMIT = 5, YEAR_LIMIT = 2051,
            MONTH_LIMIT = 12, HOUR_LIMIT = 24, MINUTES_LIMIT = 60, EXAM_DURATION_LIMIT = 180;
    
    private int id , numberOfModels;
    private Class examClass;
    private String instructorName;
    private String name;
    private LocalDateTime startTime, endTime;
    private Vector<Model> models;
    private Duration duration;
    private boolean isFilled;
    private boolean isPublished;
    
    
    /*
     * This constructor initializes all the final attributes of the class
    */

    public Exam(Entities.Class examClass) {
        models = new Vector<Model>();
        this.examClass = examClass;
    }
    
    /** Note left by Ziad Khobeiz and Abdel-Aziz Mostafa on https://www.notion.so/Meeting-Notes-ddad1da729614ee9b04ef04fd54faef4
	GENERAL NOTE: FOR ALL CLASSES (NOT ONLY THIS CLASS)
	Since the id is generated by DB sequence (.nextval), the id will be removed from the constructor
    */
    
    /** Yusuf Nasser writing, Ayman Hassan on Zoom 10th Jan 2021, 16:26
     * We will still need the id attribute to store the id in it
     * ID will be generated in the constructor
     * 
     * On the matter of removing the course object, we won't remove it until further
     * discussions and arguments provided.
    */
    
    public Exam(int id) {
        models = new Vector<Model>();
        this.id = id;
    }
    /**
     * All the possible statuses for the exam
     */
    public enum Status{
        /**
         * Exam is published and currently running
         */
        RUNNING,
        /**
         * Exam is published and has not started yet
         */
        UPCOMING,
        /**
         * Exam is published and has finished
         */
        FINISHED,
        /**
         * Exam is unpublished
         */
        UNPUBLISHED
    }
    private void generateID()
    {
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select EXAMIDSEQ.NEXTVAL from dual");
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                id = myResultSet.getInt(1);
           }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static int getMaxQuestion() {
        return MAX_QUESTION;
    }

    public static int getModelNumberLimit() { return MODEL_NUMBER_LIMIT; }

    public static int getYearLimit() { return YEAR_LIMIT; }

    public static int getMonthLimit() { return MONTH_LIMIT; }

    public static int getHourLimit() { return HOUR_LIMIT; }

    public static int getMinutesLimit() { return MINUTES_LIMIT; }

    public static int getExamDurationLimit() { return EXAM_DURATION_LIMIT; }

    /**
     * Ayman Hassan, Ziad Khobeiz
     * finished fillData function
     * retrieve all the Exam data from the Database
     * 
     */
    @Override
    public void fillData() {
        isFilled=true;
        Connection myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select STARTTIME, ENDTIME, NAME, CLASSID, isPublished FROM EXAM where EXAMID = ?");
            myStatement.setInt(1, id);
            ResultSet myResultSet = myStatement.executeQuery();
            if (myResultSet.next()) {
                startTime = myResultSet.getTimestamp(1).toLocalDateTime();
                endTime = myResultSet.getTimestamp(2).toLocalDateTime();
                name = myResultSet.getString(3);
                examClass = new Class(myResultSet.getInt(4),false);
                isPublished = myResultSet.getString(5).equals("Y");
                duration = Duration.between(startTime,endTime);
           }
            PreparedStatement modelsStatement = myConnection.prepareStatement("select MODELNUMBER FROM EXAMMODEL where EXAMID = ?");
            modelsStatement.setInt(1, id);
            ResultSet modelsResultSet = modelsStatement.executeQuery();
            while (modelsResultSet.next()) {
                models.add(new Model(id, modelsResultSet.getInt(1)));
            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    @Override
    public void add() {
        this.generateID();
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
        for(int modelNumber = 1; modelNumber <= numberOfModels; ++modelNumber) {
            models.add(new Model(id, modelNumber));
            models.lastElement().add();
        }
    }

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

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /*
     * All the setters functions of the class: startTime and duration
     * @Note endTime will be derived from duration and startTime
    */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }
    /*
     * All the getter functions of the Exam class
    */

    public void setNumberOfModels(int numberOfModels) { this.numberOfModels = numberOfModels; }

    public int getId() {
        return id;
    }

    public Class getExamClass() {
        
        if(!isFilled) {
            fillData();
        }
        return examClass;
    }

   

    public String getInstructorName() {
        if(!isFilled) {
            fillData();
        }
        return instructorName;
    }

    public LocalDateTime getStartTime() {
        if(!isFilled) {
            fillData();
        }
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if(!isFilled) {
            fillData();
        }
        return endTime;
    }

    public Duration getDuration() {
        if(!isFilled) {
            fillData();
        }
        return duration;
    }

    public Vector<Model> getModels() {
        if(!isFilled) {
            fillData();
        }
        return models;
    }
    
    /**
     * It determines whether the exam is currently running, upcoming, finished or unpublished
     * @return Status The current status of the exam
     */
    public Status getStatus() {
        if(!isFilled) {
            fillData();
        }
        if(!isPublished) {
            return Status.UNPUBLISHED;
        }
        if(startTime.compareTo(java.time.LocalDateTime.now()) > 0) {
            return Status.UPCOMING;
        }
        if(endTime.compareTo(java.time.LocalDateTime.now()) < 0) {
            return Status.FINISHED;
        }
        return Status.RUNNING;
    }

    public String getName() {
        if(!isFilled) {
            fillData();
        }
        return name;
    }

    /**
     * It checks whether all the models of the exam have the same positive number of question
     * and the exam start time is upcoming (i.e. the exam is ready to be published)
     * @return boolean Whether the exam is ready to be published
     */
    public boolean isReadyToPublish() {
        if(models.size() == 0) {
            return false;
        }
        for(int i = 1; i < models.size(); ++i) {
            if(models.elementAt(i).getQuestions().size() == 0 || 
                    models.elementAt(i).getQuestions().size() != models.elementAt(i - 1).getQuestions().size()) {
                return false;
            }
        }
        return true;
    }
    
    public void publish() {
        isPublished = true;
        update();
    }
    
    public static void main(String[] args) {
        Exam test = new Exam(new Entities.Class  (5,false));
        System.out.println(test.getId());
        test.setName("Testing2");
        test.setStartTime(LocalDateTime.now());
        test.setEndTime(LocalDateTime.now());
        test.setIsPublished(true);
        test.add();
    }
}
