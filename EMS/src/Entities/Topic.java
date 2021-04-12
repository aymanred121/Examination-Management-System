/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 * Represents a topic is in a certain class syllabus.
 *
 * @author Steven Sameh
 */
public class Topic {
    
    private final int classID;
    private final String name;

    public Topic(int classID, String name) {
        this.classID = classID;
        this.name = name;
    }

}
