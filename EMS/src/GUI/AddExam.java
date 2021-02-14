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
import java.util.Calendar;
import java.util.Vector;

/**
 * @author Steven, Ziad, Ayman, Yusuf Nasser, Youssef Nader
 */
public class AddExam extends Page {

    private Instructor instructor;
    private Entities.Class userClass;
    private JComboBox yearComboBox, monthComboBox, dayComboBox,
            hourComboBox, minuteComboBox, modelNumberComboBox, durationComboBox;
    private DefaultListCellRenderer listRenderer;
    private Calendar now;
    private Vector<Integer> months, days, years, hours, minutes, models, durations;
    private int currentYear, currentMonth, currentDay, currentHour, currentMinute;
    private JButton addExamButton;

    /**
     * It constructs a new AddExam page for a certain instructor in a specific
     * class
     *
     * @param instructor The instructor object
     * @param userClass  The current class to set an exam for it
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

        // For testing purposes
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Setting the size for the AddExam Page
        setSize(new java.awt.Dimension(800, 600));

        // Disabling the scroll bar
        getJScrollPane1().getVerticalScrollBar().setEnabled(false);
    }

    private void setCurrentDate() {
        // Getting the current year, month, day, hour HH, minute
        now = Calendar.getInstance();
        currentYear = now.get(Calendar.YEAR);
        currentMonth = now.get(Calendar.MONTH) + 1; // Months are indexed from 0 .. Java, right?
        currentDay = now.get(Calendar.DAY_OF_MONTH);
        currentHour = now.get(Calendar.HOUR_OF_DAY);
        currentMinute = now.get(Calendar.MINUTE);
    }

    private void showDatePortion() {
        int baseXPosition = 40; // A base position so that changing multiple component at once would be easily done.
        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        dateLabel.setBounds(baseXPosition, 37, 50, 40);
        getPanel().add(dateLabel);

        //Creating ComboBox to hold years in range CURRENT_YEAR : 2051 and setting its properties
        yearComboBox = new JComboBox();
        yearComboBox.setBounds(baseXPosition + 220, 40, 70, 30);

        // Initializing years vector to store the years in the valid range.
        years = new Vector<Integer>();
        for (int yearIterator = currentYear; yearIterator <= Exam.getYearLimit(); ) {
            years.add(yearIterator++);
        }

        // Adding years vector to the combobox list of items.
        yearComboBox.setModel(new DefaultComboBoxModel(years));

        // Centering the items in the combobox
        yearComboBox.setRenderer(listRenderer);

        // Setting the selected Item to the current Year
        yearComboBox.setSelectedItem(currentYear);

        // Adding the yearComboBox to the panel
        getPanel().add(yearComboBox);


        // Initializing months vector to store the months in the valid range.
        monthComboBox = new JComboBox();
        monthComboBox.setBounds(baseXPosition + 140, 40, 70, 30);

        // Creating a vector to store the months in the combobox.
        months = new Vector<Integer>();
        for (int monthIterator = currentMonth; monthIterator <= Exam.getMonthLimit(); ) {
            months.add(monthIterator++);
        }

        // Adding months vector to the combobox list of items.
        monthComboBox.setModel(new DefaultComboBoxModel(months));

        // Centering the items in the combobox
        monthComboBox.setRenderer(listRenderer);

        // Setting the selected item to the current month
        monthComboBox.setSelectedItem(currentMonth);

        // Adding the monthComboBox to the panel
        getPanel().add(monthComboBox);

        // Creating ComboBox to hold month in range 1 : 12 and setting its properties
        dayComboBox = new JComboBox();
        dayComboBox.setBounds(baseXPosition + 60, 40, 70, 30);

        // Initializing days vector to store the days in the valid range
        days = new Vector<Integer>();
        //setting the dayLimit
        int dayLimit = getDayLimit((int) monthComboBox.getSelectedItem(), (int) yearComboBox.getSelectedItem());
        for (int dayIterator = currentDay; dayIterator <= dayLimit; ) {
            days.add(dayIterator++);
        }

        // Adding days vector to the combobox list of items.
        dayComboBox.setModel(new DefaultComboBoxModel(days));

        // Centering the items in the combobox
        dayComboBox.setRenderer(listRenderer);

        // Setting the selected item to the current day
        dayComboBox.setSelectedItem(currentDay);

        // adding the dayComboBox to the panel
        getPanel().add(dayComboBox);
    }

    private void showTimePortion() {
        int baseXPosition = 400; // A base position so that changing multiple component at once would be easily done.
        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        timeLabel.setBounds(baseXPosition + 60, 37, 100, 40);
        getPanel().add(timeLabel);

        // Creating ComboBox to hold valid hours and setting its properties
        hourComboBox = new JComboBox();
        hourComboBox.setBounds(baseXPosition + 120, 40, 70, 30);

        // Initializing years vector to store the hours in the valid range.
        hours = new Vector<Integer>();
        for (int hourIterator = currentHour; hourIterator < Exam.getHourLimit(); ) {
            hours.add(hourIterator++);
        }

        // Adding hours vector to the combobox list of items.
        hourComboBox.setModel(new DefaultComboBoxModel(hours));

        // Centering the items in the combobox
        hourComboBox.setRenderer(listRenderer);

        // Setting the selected Item to the current hour
        hourComboBox.setSelectedItem(currentYear);

        // Adding the hourComboBox to the panel
        getPanel().add(hourComboBox);

        // Creating ComboBox to hold valid minutes and setting its properties
        minuteComboBox = new JComboBox();
        minuteComboBox.setBounds(baseXPosition + 200, 40, 70, 30);

        // Initializing years vector to store the minutes in the valid range.
        minutes = new Vector<Integer>();
        for (int minuteIterator = currentMinute; minuteIterator < Exam.getMinutesLimit(); ) {
            minutes.add(minuteIterator++);
        }

        // Adding minutes vector to the combobox list of items.
        minuteComboBox.setModel(new DefaultComboBoxModel(minutes));

        // Centering the items in the combobox
        minuteComboBox.setRenderer(listRenderer);

        // Setting the selected Item to the current minute
        minuteComboBox.setSelectedItem(currentMinute);

        // Adding the minuteComboBox to the panel
        getPanel().add(minuteComboBox);
    }

    private void getExamBasicData(Exam exam) {
        //exam.setStartTime();
        //exam.setDuration();
        //exam.setName();
        exam.setIsPublished(false);
    }

    private void showExamDataInput() {
        setCurrentDate();

        // Setting the Alignment of the items in the list
        listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        showDatePortion();
        showTimePortion();

        JLabel durationLabel = new JLabel("Duration");
        durationLabel.setFont(new java.awt.Font("Tahoma", 1, 18));
        durationLabel.setBounds(40, 20 + 100, 180, 40);
        getPanel().add(durationLabel);

        JLabel numberOfModelsLabel = new JLabel("Models Number");
        numberOfModelsLabel.setFont(new java.awt.Font("Tahoma", 1, 18));
        numberOfModelsLabel.setBounds(40, 20 + 150, 180, 40);
        getPanel().add(numberOfModelsLabel);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new java.awt.Font("Tahoma", 1, 18));
        nameLabel.setBounds(40, 20 + 200, 180, 40);
        getPanel().add(nameLabel);

        addExamButton = new JButton("Add Exam");
        addExamButton.setBounds(570, 350, 100, 35);
        addExamButton.setVisible(true);
        getPanel().add(addExamButton);

        // Creating an event handler to handle whatever changes might occur while running
        PageEventHandler handler = new PageEventHandler();

        // Adding an Action Listener to the combobox-es to track the changes occurring to them
        yearComboBox.addActionListener(handler);
        monthComboBox.addActionListener(handler);
        dayComboBox.addActionListener(handler);
        hourComboBox.addActionListener(handler);
        minuteComboBox.addActionListener(handler);
        addExamButton.addActionListener(handler);
    }

    void refreshMonthList() {
        // Declaring the month iterator variable
        int monthIterator = 1, monthLowerLimit = 1,
                selectedMonth = (int) monthComboBox.getSelectedItem();

        // Clearing the months vector
        months.clear();

        /*
         * Logic:
         * IF the current year is the year selected then the valid
         * months will be starting from the current month to December
         * ELSE every month shall be added since any other year would be
         * an upcoming one.
         */

        if ((int) yearComboBox.getSelectedItem() == currentYear) {
            monthIterator = currentMonth;
            monthLowerLimit = currentMonth;
        } else
            monthIterator = 1;

        // Adding the valid months to the vector
        while (monthIterator <= Exam.getMonthLimit()) {
            months.add(monthIterator++);
        }

        // Adding months vector to the combobox list of items.
        monthComboBox.setModel(new DefaultComboBoxModel(months));

        // Setting the current month
        // to avoid erasing the user's selection if not necessary
        if (selectedMonth < monthLowerLimit) selectedMonth = currentMonth;
        monthComboBox.setSelectedItem(selectedMonth);
    }

    void refreshDayList() {
        // Declaring the day iterator variable
        int dayIterator = 1, dayLowerLimit = 1, selectedDay = (int) dayComboBox.getSelectedItem(),
                dayLimit = getDayLimit((int) monthComboBox.getSelectedItem(), (int) yearComboBox.getSelectedItem()); //setting the dayLimit

        // clearing days vector
        days.clear();

        /*
         * Logic:
         * IF the current year is the year selected AND the current month
         * is the selected month THEN the valid days will be starting from
         * the current day to the day limit returned from getDayLimit()
         * ELSE every day shall be added since any other month would be an upcoming one.
         */

        if ((int) yearComboBox.getSelectedItem() == currentYear) {
            if ((int) monthComboBox.getSelectedItem() == currentMonth) {
                dayIterator = currentDay;
                dayLowerLimit = currentDay;
            }
        }

        // filling the vector with the newly chosen month days range
        while (dayIterator <= dayLimit) {
            days.add(dayIterator++);
        }

        // Adding days vector to the combobox list of items.
        dayComboBox.setModel(new DefaultComboBoxModel(days));

        // Getting the current month
        // to avoid erasing the user's selection if not necessary
        if (selectedDay < dayLowerLimit) selectedDay = currentDay;
        dayComboBox.setSelectedItem(selectedDay);
    }

    void refreshHourList() {
        // Declaring the hour iterator variable
        int hourIterator = 0, hourLowerLimit = 0,
                selectedHour = (int) hourComboBox.getSelectedItem();

        // Clearing hours vector
        hours.clear();

        /*
         * Logic:
         * IF the current year is the year selected AND the current month
         * is the selected month and the current day is the selected day
         * THEN the valid hours will be starting from the current hour to the hour limit
         * ELSE every hour shall be added since any other day would be an upcoming one.
         */

        if ((int) dayComboBox.getSelectedItem() == currentDay) {
            if ((int) monthComboBox.getSelectedItem() == currentMonth) {
                if ((int) yearComboBox.getSelectedItem() == currentYear) {
                    hourIterator = currentHour;
                    hourLowerLimit = currentHour;
                }

            }
        }

        // filling the vector with the valid hours range
        while (hourIterator < Exam.getHourLimit()) {
            hours.add(hourIterator++);
        }

        // Adding hours vector to the combobox list of items.
        hourComboBox.setModel(new DefaultComboBoxModel(hours));

        // Setting the current hour
        // to avoid erasing the user's selection if not necessary
        if (selectedHour < hourLowerLimit) selectedHour = currentHour;
        hourComboBox.setSelectedItem(selectedHour);
    }

    void refreshMinuteList() {
        // Declaring the minute iterator variable, getting the selected minute
        int minuteIterator = 0, minuteLowerLimit = 0,
                selectedMinute = (int) minuteComboBox.getSelectedItem();

        // Clearing minutes vector
        minutes.clear();

        /*
         * Logic:
         * IF the current year is the year selected AND the current month
         * is the selected month and the current day is the selected day and
         * the current hour is the selected hour THEN the valid minutes will
         * be starting from the current minute to the hour limit ELSE every
         * minute shall be added since any other hour would be an upcoming one.
         */

        if ((int) hourComboBox.getSelectedItem() == currentHour) {
            if ((int) dayComboBox.getSelectedItem() == currentDay) {
                if ((int) monthComboBox.getSelectedItem() == currentMonth) {
                    if ((int) yearComboBox.getSelectedItem() == currentYear) {
                        minuteIterator = currentMinute;
                        minuteLowerLimit = currentMinute;
                    }
                }
            }
        }

        // filling the vector with the valid minutes range
        while (minuteIterator < Exam.getMinutesLimit()) {
            minutes.add(minuteIterator++);
        }

        // Adding hours vector to the combobox list of items.
        minuteComboBox.setModel(new DefaultComboBoxModel(minutes));

        // Setting the selected minute
        // to avoid erasing the user's selection if not necessary
        if (selectedMinute < minuteLowerLimit) selectedMinute = currentMinute;
        minuteComboBox.setSelectedItem(selectedMinute);
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

    private class PageEventHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == yearComboBox) {
                // Refreshing the current time
                // #order_of_the_refreshing_matters cliché, haa?
                /*
                 * As refreshing goes, comboBox-es lists gets cleared
                 * So they should be refreshed (cleared) backwards.
                 */
                setCurrentDate();
                refreshMinuteList();
                refreshHourList();
                refreshDayList();
                refreshMonthList();
            } else if (event.getSource() == monthComboBox) {
                setCurrentDate();
                refreshMinuteList();
                refreshHourList();
                refreshDayList();
            } else if (event.getSource() == dayComboBox) {
                setCurrentDate();
                refreshMinuteList();
                refreshHourList();
            } else if (event.getSource() == hourComboBox) {
                setCurrentDate();
                refreshMinuteList();
            } else if (event.getSource() == addExamButton) {
                /*
                 * TODO
                 *  * CREATE and add models to the database
                 *  * Forward the user to Show models of the Exam
                 */
            }
        }
    }

    public static void main(String args[]) {
        Instructor instructor = new Instructor("aliyaser");
        Entities.Class classes = new Entities.Class(1, false);
        AddExam exam = new AddExam(instructor, classes);
        exam.setVisible(true);
    }
}
