/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.*;
import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;
import org.w3c.dom.Entity;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Vector;
import java.time.*;

import static java.text.DateFormat.DEFAULT;

/**
 * @author Steven, Ziad, Ayman, Yusuf Nasser, Youssef Nader
 */
public class AddExam extends Page {
    private Instructor instructor;
    private Entities.Class userClass;
    private JComboBox yearComboBox, monthComboBox, dayComboBox,
            hourComboBox, minuteComboBox, modelComboBox, durationComboBox;
    private DefaultListCellRenderer listRenderer;
    private Calendar now;
    private Vector<Integer> months, days, years, hours, minutes, models, durations;
    private int currentYear, currentMonth, currentDay, currentHour, currentMinute, examStartHour,
            examStartMinute, examDurationTime, examStartYear, examStartMonth, examStartDay, examModels;
    private JButton addExamButton;
    private JTextField addExamNameField;
    private Exam newExam;
    private LocalDateTime examStartDate, examEndDate;
    // difference between X & Y positions for label and combobox
    final static private int deltaXLabelCombo = 15, deltaYLabelCombo = 3;

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
        newExam = new Exam(userClass);

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
        getBackButton().setVisible(true);

        // For testing purposes
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Setting the size for the AddExam Page
        setSize(new java.awt.Dimension(800, 600));

        /**
         * Remove scrollbar from the side of the panel
         * Disabling the Horizontal and Vertical scroll bar
         */
        getJScrollPane1().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        getJScrollPane1().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
        int baseXPosition = 40; // A base X position so that changing multiple component at once would be easily done.
        int baseYPosition = 40; // A base Y position so that changing multiple component at once would be easily done.

        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        dateLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 50, 40);
        getPanel().add(dateLabel);

        // Creating ComboBox to hold years in range CURRENT_YEAR : Exam.getYearLimit() and setting its properties
        yearComboBox = new JComboBox();
        yearComboBox.setBounds(baseXPosition + 210  + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing years vector to store the years in the valid range.
        years = new Vector<Integer>();
        for (int yearIterator = currentYear; yearIterator <= Exam.getYearLimit(); ) {
            years.add(yearIterator++);
        }

        // Adding years vector to the combobox list of items.
        yearComboBox.setModel(new DefaultComboBoxModel(years));

        // Centering the items in the combobox
        yearComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        yearComboBox.setFont(new java.awt.Font("Tahoma", 1, 13));

        // Setting the selected Item to the current Year
        yearComboBox.setSelectedItem(currentYear);

        // Adding the yearComboBox to the panel
        getPanel().add(yearComboBox);

        // Initializing months vector to store the months in the valid range.
        monthComboBox = new JComboBox();
        monthComboBox.setBounds(baseXPosition + 130 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Creating a vector to store the months in the combobox.
        months = new Vector<Integer>();
        for (int monthIterator = currentMonth; monthIterator <= Exam.getMonthLimit(); ) {
            months.add(monthIterator++);
        }

        // Adding months vector to the combobox list of items.
        monthComboBox.setModel(new DefaultComboBoxModel(months));

        // Centering the items in the combobox
        monthComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        monthComboBox.setFont(new java.awt.Font("Tahoma", 1, 13));

        // Setting the selected item to the current month
        monthComboBox.setSelectedItem(currentMonth);

        // Adding the monthComboBox to the panel
        getPanel().add(monthComboBox);

        // Creating ComboBox to hold month in range 1 : 12 and setting its properties
        dayComboBox = new JComboBox();
        dayComboBox.setBounds(baseXPosition + 50 + deltaXLabelCombo, baseYPosition, 70, 30);

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

        // Setting the font to the combobox
        dayComboBox.setFont(new java.awt.Font("Tahoma", 1, 13));

        // Setting the selected item to the current day
        dayComboBox.setSelectedItem(currentDay);

        // adding the dayComboBox to the panel
        getPanel().add(dayComboBox);
    }

    private void showTimePortion() {
        int baseXPosition = 400; // A base X position so that changing multiple component at once would be easily done.
        int baseYPosition = 40; // A base Y position so that changing multiple component at once would be easily done.

        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        timeLabel.setBounds(baseXPosition + 60, baseYPosition - deltaYLabelCombo, 100, 40);
        getPanel().add(timeLabel);

        // Creating ComboBox to hold valid hours and setting its properties
        hourComboBox = new JComboBox();
        hourComboBox.setBounds(baseXPosition + 110 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing years vector to store the hours in the valid range.
        hours = new Vector<Integer>();
        for (int hourIterator = currentHour; hourIterator < Exam.getHourLimit(); ) {
            hours.add(hourIterator++);
        }

        // Adding hours vector to the combobox list of items.
        hourComboBox.setModel(new DefaultComboBoxModel(hours));

        // Centering the items in the combobox
        hourComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        hourComboBox.setFont(new java.awt.Font("Tahoma", 1, 13));

        // Setting the selected Item to the current hour
        hourComboBox.setSelectedItem(currentYear);

        // Adding the hourComboBox to the panel
        getPanel().add(hourComboBox);

        // Creating ComboBox to hold valid minutes and setting its properties
        minuteComboBox = new JComboBox();
        minuteComboBox.setBounds(baseXPosition + 190 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing years vector to store the minutes in the valid range.
        minutes = new Vector<Integer>();
        for (int minuteIterator = currentMinute; minuteIterator < Exam.getMinutesLimit(); ) {
            minutes.add(minuteIterator++);
        }

        // Adding minutes vector to the combobox list of items.
        minuteComboBox.setModel(new DefaultComboBoxModel(minutes));

        // Centering the items in the combobox
        minuteComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        minuteComboBox.setFont(new java.awt.Font("Tahoma", 1, 13));

        // Setting the selected Item to the current minute
        minuteComboBox.setSelectedItem(currentMinute);

        // Adding the minuteComboBox to the panel
        getPanel().add(minuteComboBox);
    }

    private void showDurationPortion() {
        int baseXPosition = 40; // A base X position so that changing multiple component at once would be easily done.
        int baseYPosition = 100; // A base Y position so that changing multiple component at once would be easily done.

        JLabel durationLabel = new JLabel("Duration");
        durationLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        durationLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 90, 40);
        getPanel().add(durationLabel);

        durationComboBox = new JComboBox();
        durationComboBox.setBounds(baseXPosition + 90 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing years vector to store the minutes in the valid range.
        durations = new Vector<Integer>();
        for (int minuteIterator = 5; minuteIterator <= Exam.getExamDurationLimit(); minuteIterator += 5) {
            durations.add(minuteIterator);
        }

        // Adding minutes vector to the combobox list of items.
        durationComboBox.setModel(new DefaultComboBoxModel(durations));

        // Centering the items in the combobox
        durationComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        durationComboBox.setFont(new java.awt.Font("Tahoma", 1, 13));

        // Adding the minuteComboBox to the panel
        getPanel().add(durationComboBox);

        JLabel minLabel = new JLabel("(min)");
        minLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD + Font.ITALIC, 15));
        minLabel.setBounds(baseXPosition + 170 + deltaXLabelCombo, baseYPosition - deltaYLabelCombo, 90, 40);
        getPanel().add(minLabel);
    }

    private void showModelPortion() {
        int baseXPosition = 430; // A base X position so that changing multiple component at once would be easily done.
        int baseYPosition = 100; // A base Y position so that changing multiple component at once would be easily done.

        JLabel numberOfModelsLabel = new JLabel("Models Number");
        numberOfModelsLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        numberOfModelsLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 160, 40);
        getPanel().add(numberOfModelsLabel);

        modelComboBox = new JComboBox();
        modelComboBox.setBounds(baseXPosition + 160 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing models vector to store modelsNumber until the modelNumberLimit Exceeded.
        models = new Vector<Integer>();

        // Filling the models vector
        for (int modelIterator = 1; modelIterator <= Exam.getModelNumberLimit(); ) {
            models.add(modelIterator++);
        }

        // Adding models vector to the combobox list
        modelComboBox.setModel(new DefaultComboBoxModel(models));

        // Setting the font to the combobox
        modelComboBox.setFont(new java.awt.Font("Tahoma", 1, 13));

        // Centering the items in the combobox
        modelComboBox.setRenderer(listRenderer);
        getPanel().add(modelComboBox);

    }

    private void showNameBox() {
        final int baseXPosition = 40; // A base X position so that changing multiple component at once would be easily done.
        final int baseYPosition = 160; // A base Y position so that changing multiple component at once would be easily done.

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new java.awt.Font("Tahoma", 1, 20));
        nameLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 180, 40);
        getPanel().add(nameLabel);

        /*
         * PlaceHolder LOGIC:
          * Since we're using JAVA .. The most beautiful language .. There is no support for a placeholder
          * So what would we do? we go around it ..

            1 - Set the placeHolding text to the textField

            2 - Give it a gray color

            .. Hold your breath .. here is the real deal ..

            3 - Implement MouseListener in a newly created class PageMouseListener to override mouseClicked

            4 - Make an Instance of our PageMouseListener class and add it to addExamNameField.ddMouseListener()

            5 - Implement mouseClicked So whenever the user clicks inside addExamNameField Triggers two things:

              iff addExamNameField.getText().equals("Enter exam name")

                a - Set addExamNameField text to "" #NOTHING

                b - Give it a black color

             TA DAA .. Nice story, right? U can go to sleep now!

             ******************

             As you know .. A lot of great stories have sequels

             So After we finished the first part .. we found out that after the user exit the textField without
               changing anything, the placeholder is gone which is a tragedy .. because no place should have no holder
               my dear friends

             So What do we do again?

              1 - Override mouseExited() so that whenever the user exit the textField without changing
                    any thing, we would return our placeholder to its rightful place.

              We hoped that THAT would be the end of it, but If it ain't for JAVA ..

              We saw that the cursor stays in the textField which is a bug .. but don't worry .. We fixed it!

              2 - We Added - addExamNameField.transferFocus(); - I know it's not a sexy ending but it took us a lot
                   to find it ..

              Hope you liked our movie .. Yusuf Nasser and Youssef Nader
         */

        addExamNameField = new JTextField("Enter exam name");
        addExamNameField.setForeground(Color.gray);

        addExamNameField.setBounds(baseXPosition + 60 + deltaXLabelCombo, baseYPosition, 200, 35);
        getPanel().add(addExamNameField);
    }
    
    private void showAddExamButton() {
        addExamButton = new JButton("Add Exam");
        addExamButton.setBounds(570, 350, 130, 50);
        addExamButton.setFont(new java.awt.Font("Tahoma", 1, 16));
        addExamButton.setVisible(true);
        getPanel().add(addExamButton);
    }

    private void retrieveDataFromGUI() {
        examDurationTime = (int) durationComboBox.getSelectedItem();
        examStartHour = (int) hourComboBox.getSelectedItem();
        examStartMinute = (int) minuteComboBox.getSelectedItem();
        examStartYear = (int) yearComboBox.getSelectedItem();
        examStartMonth = (int) monthComboBox.getSelectedItem();
        examStartDay = (int) dayComboBox.getSelectedItem();
        examModels = (int) modelComboBox.getSelectedItem();

        examStartDate = LocalDateTime.of(examStartYear, examStartMonth, examStartDay, examStartHour, examStartMinute);
        examEndDate = examStartDate.plusMinutes(examDurationTime);
    }

    private void setExamBasicData(Exam exam) {
        exam.setStartTime(examStartDate);
        exam.setEndTime(examEndDate);
        exam.setDuration(Duration.ofMinutes(examDurationTime));
        exam.setName(addExamNameField.getText());
        exam.setNumberOfModels(examModels);
        exam.setIsPublished(false);
        exam.add();
    }

    private void showExamDataInput() {
        setCurrentDate();

        // Setting the Alignment of the items in the list
        listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        showDatePortion();
        showTimePortion();
        showDurationPortion();
        showModelPortion();
        showNameBox();
        showAddExamButton();

        // Creating an action listener to handle whatever changes might occur while running
        PageActionListener listener = new PageActionListener();

        // Adding an Action Listener to the combobox-es to track the changes occurring to them
        yearComboBox.addActionListener(listener);
        monthComboBox.addActionListener(listener);
        dayComboBox.addActionListener(listener);
        hourComboBox.addActionListener(listener);
        minuteComboBox.addActionListener(listener);
        addExamButton.addActionListener(listener);

        // Creating an event listener to handle mouse events
        PageMouseListener mouseListener = new PageMouseListener();

        // Adding a mouse listener to track mouse events
        addExamNameField.addMouseListener(mouseListener);
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

    boolean isNameEntered() {
        return addExamNameField.getForeground().equals(Color.black);
    }

    boolean userIsSure() {
        String message = new String();
        retrieveDataFromGUI();

        message = "Exam name: " + addExamNameField.getText();
        message += "\nDuration: " + examDurationTime + " min";
        message += ("\nStart time: " + examStartHour + " : " + examStartMinute);
        message += ("\nDate: " + examStartDay + " - " + examStartMonth + " - " + examStartYear );
        message += ("\nNumber of models: " + examModels + "\n");
        return JOptionPane.showConfirmDialog(null, message, "Are you sure to submit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
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

    private class PageActionListener implements ActionListener {
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
                if (!isNameEntered()) {
                    JOptionPane.showMessageDialog(null, "Please Enter a Valid Name");
                }
                else {
                    if (userIsSure())
                        setExamBasicData(newExam);
                }
            }
        }
    }

    private class PageMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == addExamNameField)
            {
                if (addExamNameField.getText().equals("Enter exam name"))
                {
                    addExamNameField.setText("");
                    addExamNameField.setForeground(Color.black);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == addExamNameField)
            {
                if (addExamNameField.getText().equals(""))
                {
                    addExamNameField.setForeground(Color.gray);
                    addExamNameField.setText("Enter exam name");
                    addExamNameField.transferFocus(); // so that the cursor would move away from the textField
                }
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
