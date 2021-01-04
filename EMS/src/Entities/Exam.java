/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.time.*;
import javax.xml.transform.Source;

/**
 *
 * @author yn653, Steven Sameh, Yusuf Nasser, Ayman Hassasn
 */
public class Exam implements SqlEntity {
    
    /*
     * All the attributes of the Exam Class   
    */
    // TBC
    final static private int maxQuestion = 50;
    
    final private int id;
    final private Course course;
    final private double totalMarks;
    final String instructorName;
    private LocalTime startTime, endTime;
    private LocalDate date;
    private Duration duration;
    boolean validationStatus;
    
    /*
     * This constructor initializes all the final attributes of the class
    */

    public Exam(int id, Course course, double totalMarks, String instructorName) {
        this.id = id;
        this.course = course;
        this.totalMarks = totalMarks;
        this.instructorName = instructorName;
        this.endTime = startTime.plus(duration);
    }
  
    
    
    public static int getMaxQuestion() {
        return maxQuestion;
    }

    @Override
    public void fillData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
     * All the setters functions of the class: startTime, date, duration, validationStatus
     * @Note endTime will be derived from duration and startTime
    */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setValidationStatus(boolean validationStatus) {
        this.validationStatus = validationStatus;
    }
    
    /*
     * All the getter functions of the Exam class
     * @Note endTime getter function is derived from duration and startTime
    */
    public int getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public Duration getDuration() {
        return duration;
    }

    public boolean isValidationStatus() {
        return validationStatus;
    }
    
    
    
}
