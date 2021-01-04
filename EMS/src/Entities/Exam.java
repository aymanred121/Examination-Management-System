/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author yn653
 */
public class Exam implements SqlEntity {
    final static private int maxQuestion = 50;

    public static int getMaxQuestion() {
        return maxQuestion;
    }
    
    
    
}
