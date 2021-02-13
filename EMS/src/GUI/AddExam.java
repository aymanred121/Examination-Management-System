/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Vector;

/**
 *
 * @author Steven, Ziad, Ayman, Yusuf Nasser, Youssef Nader
 */
public class AddExam extends Page {

    private Instructor instructor;
    private Entities.Class userClass;

    /**
     * It constructs a new AddExam page for a certain instructor in a specific
     * class
     *
     * @param instructor The instructor object
     * @param userClass The current class to set an exam for it
     */
    public AddExam(Instructor instructor, Entities.Class userClass) {
        this.instructor = instructor;
        this.userClass = userClass;

        // Creating nex exam to store the new exam data
        Exam newExam = new Exam(userClass);

        // Setting the title label and its properties
        String titleLabel = userClass.getCourse().getName() + " Exam";
        getTitleLabel().setText(titleLabel);
        getTitleLabel().setBounds(500, 500, 100, 100);

        // Adding an action listener for the back button in the superclass to go to the ViewExams page 
        getBackButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ViewExams((User) instructor, userClass).setVisible(true);
                dispose();
            }
        });
        showExamDataInput();
        getExamBasicData(newExam);
        getBackButton().setVisible(true);

        //For testing purposes
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void getExamBasicData(Exam exam) {

    }

    public static void main(String args[]) {
        Instructor instructor = new Instructor("aliyaser");
        Entities.Class classes = new Entities.Class(1, false);
        AddExam exam = new AddExam(instructor, classes);
        exam.setVisible(true);
    }

    private void showExamDataInput() {
        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(new java.awt.Font("Tahoma", 1, 17));
        dateLabel.setBounds(40, 20 + 0, 50, 50);
        getPanel().add(dateLabel);

        //Creating ComboBox to hold years in range 1981 : 2051 and setting its properties
        JComboBox yearComboBox = new JComboBox();
        yearComboBox.setBounds(220, 65, 70, 30);
        
        /* 
         * Creating a vector to store the years in the specified 
         * range and add it to the combobox.
         */
        Vector<Integer> years = new Vector<Integer>();
        for (int yearIterator = 1981; yearIterator <= 2051; yearIterator++) {
            years.add(yearIterator);
        }
        yearComboBox.setModel(new DefaultComboBoxModel(years));

        // Centering the items in the combobox
        DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        yearComboBox.setRenderer(listRenderer);

        // Getting the current year
        Calendar now = Calendar.getInstance();
        yearComboBox.setSelectedItem(now.get(Calendar.YEAR));

        // adding the yearComboBox to the panel
        getPanel().add(yearComboBox);

        //Creating ComboBox to hold month in range 1 : 12 and setting its properties
        JComboBox monthComboBox = new JComboBox();
        monthComboBox.setBounds(120, 65, 70, 30);

        /* 
         * Creating a vector to store the months in the specified 
         * range and add it to the combobox.
         */
        Vector<Integer> months = new Vector<Integer>();
        for (int monthIterator = 1; monthIterator < 13; monthIterator++) {
            months.add(monthIterator);
        }
        monthComboBox.setModel(new DefaultComboBoxModel(months));

        // Centering the items in the combobox
        monthComboBox.setRenderer(listRenderer);

        // Getting the current month
        monthComboBox.setSelectedItem(now.get(Calendar.MONTH) + 1);

        // adding the monthComboBox to the panel
        getPanel().add(monthComboBox);

        //Creating ComboBox to hold month in range 1 : 12 and setting its properties
        JComboBox dayComboBox = new JComboBox();
        dayComboBox.setBounds(40, 65, 70, 30);

        /* 
         * Creating a vector to store the months in the specified 
         * range and add it to the combobox.
         */
        Vector<Integer> days = new Vector<Integer>();
        int dayLimit = getDayLimit((int) monthComboBox.getSelectedItem(), (int) yearComboBox.getSelectedItem()); //setting the dayLimit
        System.out.println((int) monthComboBox.getSelectedItem());
        System.out.println((int) yearComboBox.getSelectedItem());
        for (int dayIterator = 1; dayIterator <= dayLimit; dayIterator++) {
            days.add(dayIterator);
        }
        dayComboBox.setModel(new DefaultComboBoxModel(days));

        // Centering the items in the combobox
        dayComboBox.setRenderer(listRenderer);

        // Getting the current month
        dayComboBox.setSelectedItem(now.get(Calendar.DAY_OF_MONTH));

        // adding the monthComboBox to the panel
        getPanel().add(dayComboBox);

        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new java.awt.Font("Tahoma", 1, 17));
        timeLabel.setBounds(40, 20 + 50, 300, 80);
        getPanel().add(timeLabel);

        JLabel durationLabel = new JLabel("Duration");
        durationLabel.setFont(new java.awt.Font("Tahoma", 1, 17));
        durationLabel.setBounds(40, 20 + 100, 300, 80);
        getPanel().add(durationLabel);

        JLabel numberOfModelsLabel = new JLabel("Models Number");
        numberOfModelsLabel.setFont(new java.awt.Font("Tahoma", 1, 17));
        numberOfModelsLabel.setBounds(40, 20 + 150, 300, 80);
        getPanel().add(numberOfModelsLabel);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new java.awt.Font("Tahoma", 1, 17));
        nameLabel.setBounds(40, 20 + 200, 300, 80);
        getPanel().add(nameLabel);
        
        ActionListener yearActionListener = new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.)
                showExamDataInput();
                //int selectedYear =  (int) yearComboBox.getSelectedItem();//get the selected item
                //int selectedMonth = (int) monthComboBox.getSelectedItem();
                
                
                
            }
        };
    }

    boolean leapYear(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                return year % 400 == 0;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private int getDayLimit(int month, int year) {
        switch (month) {
            case 2:
                if (leapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                break;
        }
        return 31;
    }
    
    
}
