package GUI;

import Entities.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Vector;
import java.time.*;

/**
 * An extended version of GUI.Page that is used to create a new exam for
 * the logged-in instructor in a specific class of his/her choice. It does that
 * by taking the exam basic data through several combobox-es and other GUI components
 * - some of the data is validated in the process of showing the combobox-es lists and others
 * validated after being inserted - and calls Entities.Exam.add()
 *
 * @author Yusuf Nasser, Youssef Nader, Ayman Hassan, Steven Sameh, Ziad Khobeiz
 * @version 1.1
 */

public class AddExam extends Page {
    private final Instructor instructor;
    private Exam newExam, editedExam;
    private JComboBox<Integer> yearComboBox, monthComboBox, dayComboBox,
            hourComboBox, minuteComboBox, modelComboBox, durationComboBox;
    private DefaultListCellRenderer listRenderer;
    private Vector<Integer> months, days, hours, minutes;
    private int currentYear, currentMonth, currentDay, currentHour, currentMinute, examStartHour,
            examStartMinute, examDurationTime, examStartYear, examStartMonth, examStartDay, examModels;
    private JButton addExamButton, saveChangesButton;
    private JTextField enterExamNameField;
    private String examName;
    private LocalDateTime examStartDate, examEndDate;
    // difference between X & Y positions for label and combobox
    final static private int deltaXLabelCombo = 15, deltaYLabelCombo = 3;

    /*
     * NOTE:
     * All Unboxing of ComboBox.getSelectedItem() warnings for as it might produce 'NullPointerException'
     * warnings are implicitly handled as the combobox selected item is always set to a certain value.
     */

    /**
     * Constructs a new page that is the base for creating a new exam
     * or editing an existent one.
     *
     * @param instructor The logged-in instructor instance
     */

    public AddExam(Instructor instructor) {
        this.instructor = instructor;

        getBackButton().setVisible(true);

        // For testing purposes
        // setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Setting the size for the AddExam Page
        setSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        /*
          Remove scrollbar from the side of the panel
          Disabling the Horizontal and Vertical scroll bar
         */

        getJScrollPane1().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        getJScrollPane1().setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Constructs a new page in which the logged-in instructor can
     * create a new exam for a specific class chosen at viewExams page
     *
     * @param instructor The logged-in instructor instance
     * @param userClass  The chosen class to set an exam for it
     */

    public AddExam(Instructor instructor, Entities.Class userClass) {
        this(instructor);
        // Creating nex exam to store the new exam data
        newExam = new Exam(userClass);

        // Setting the title label and its properties
        setTitleLabel(userClass);

        // Adding an action listener for the back button in the superclass to go to the ViewExams page
        getBackButton().addActionListener(e -> {
            new ViewExams(instructor, userClass).setVisible(true);
            dispose();
        });

        // Showing the data input portions
        showExamDataInput();
    }

    /**
     * Constructs a new page in which the logged-in instructor can
     * edit the exam starting date and time so that they would be valid
     *
     * @param instructor The logged-in instructor instance
     * @param exam       The current exam to be edited
     */

    public AddExam(Instructor instructor, Exam exam) {
        this(instructor);

        // Setting the member variables
        this.editedExam = exam;

        // Setting the title label and its properties
        setTitleLabel(exam.getExamClass());

        // Adding an action listener for the back button in the superclass to go to the ViewExams page
        getBackButton().addActionListener(e -> {
            new ViewModels(instructor, exam.getModels().firstElement()).setVisible(true);
            dispose();
        });

        // Showing the exam time portion to edit so it'd be valid.
        showExamTimeInput();
    }

    /**
     * Sets the properties of the title label to the course name Exams
     *
     * @param userClass — the current user class
     */

    private void setTitleLabel(Entities.Class userClass) {
        String titleLabel = userClass.getCourse().getName() + " Exam";
        getTitleLabel().setText(titleLabel);
        getTitleLabel().setBounds(500, 500, 100, 100);
    }

    /**
     * This method retrieves the current date and time and store each portion
     * of the date in a member variable to be used in other methods.
     * It does that using an instance of java.util.Calendar
     */

    private void setCurrentDate() {
        // Getting the current year, month, day, hour HH, minute
        Calendar now = Calendar.getInstance();

        /*
         * You can't add exams as of the same moment u opened the create new exam page
         * At least one hour is required to set the exam questions of one model or more
         */

        now.add(Calendar.HOUR_OF_DAY, 1);

        // Setting the member variables to their respective values
        currentYear = now.get(Calendar.YEAR);
        currentMonth = now.get(Calendar.MONTH) + 1; // Months are indexed from 0 .. Java, right?
        currentDay = now.get(Calendar.DAY_OF_MONTH);
        currentHour = now.get(Calendar.HOUR_OF_DAY);
        currentMinute = now.get(Calendar.MINUTE);
    }

    /**
     * Sets the properties of a certain combobox and assign its list and selected item and finally add it to the panel
     *
     * @param comboBox     — the combobox to set its properties
     * @param list         — the list to assign to the combobox
     * @param selectedItem — the selected item to assign to the combobox
     */

    private void setComboBoxProperties(JComboBox<Integer> comboBox, Vector<Integer> list, int selectedItem) {
        // Adding list vector to the combobox list of items.
        comboBox.setModel(new DefaultComboBoxModel<>(list));

        // Centering the items in the combobox
        comboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        comboBox.setFont(new java.awt.Font("Tahoma", Font.BOLD, 13));

        // Setting the selected Item to the passed item
        comboBox.setSelectedItem(selectedItem);

        // Adding the comboBox to the panel
        getPanel().add(comboBox);
    }

    /**
     * This method adds the time input related GUI components to the panel
     * It does that by using an instance of javax.swing.JLabel to label the
     * date portion and several instances of javax.swing.JComboBox<E> to
     * take the day, month and year input.
     */

    private void showDatePortion() {
        // A base X and Y positions so that changing multiple component at once would be easily done.
        int baseXPosition = 40;
        int baseYPosition = 40;

        // Creating a Label for the date portion and setting its properties
        JLabel dateLabel = new JLabel("Date");
        dateLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
        dateLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 50, 40);
        getPanel().add(dateLabel);

        // Initializing a ComboBox to hold years in range CURRENT_YEAR : Exam.getYearLimit() and setting its properties
        yearComboBox = new JComboBox<>();
        yearComboBox.setBounds(baseXPosition + 210 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Creating years vector to store the years in the valid range.
        Vector<Integer> years = new Vector<>();
        for (int yearIterator = currentYear; yearIterator <= Exam.getYearLimit(); ) {
            years.add(yearIterator++);
        }

        setComboBoxProperties(yearComboBox, years, currentYear);

        // Initializing months vector to store the months in the valid range.
        monthComboBox = new JComboBox<>();
        monthComboBox.setBounds(baseXPosition + 130 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing a vector to store the months in the combobox.
        months = new Vector<>();
        for (int monthIterator = currentMonth; monthIterator <= Exam.getMonthLimit(); ) {
            months.add(monthIterator++);
        }

        setComboBoxProperties(monthComboBox, months, currentMonth);

        // Creating ComboBox to hold month in range 1 : 12 and setting its properties
        dayComboBox = new JComboBox<>();
        dayComboBox.setBounds(baseXPosition + 50 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing days vector to store the days in the valid range
        days = new Vector<>();
        //setting the dayLimit
        int dayLimit = getDayLimit((int) monthComboBox.getSelectedItem(), (int) yearComboBox.getSelectedItem());
        for (int dayIterator = currentDay; dayIterator <= dayLimit; ) {
            days.add(dayIterator++);
        }

        setComboBoxProperties(dayComboBox, days, currentDay);
    }

    /**
     * This method adds the time input related GUI components to the panel
     * It does that by using an instance of javax.swing.JLabel to label the
     * time portion and couple of instances of javax.swing.JComboBox<E> to
     * take the hour and minute input.
     */

    private void showTimePortion() {
        // A base X and Y positions so that changing multiple component at once would be easily done.
        int baseXPosition = 400;
        int baseYPosition = 40;

        // Creating a Label for the time portion and setting its properties
        JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
        timeLabel.setBounds(baseXPosition + 60, baseYPosition - deltaYLabelCombo, 100, 40);
        getPanel().add(timeLabel);

        // Initializing ComboBox to hold the valid hours and setting its properties
        hourComboBox = new JComboBox<>();
        hourComboBox.setBounds(baseXPosition + 110 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing hours vector to store the hours in the valid range.
        hours = new Vector<>();
        for (int hourIterator = currentHour; hourIterator < Exam.getHourLimit(); ) {
            hours.add(hourIterator++);
        }

        // Adding hours vector to the combobox list of items.
        hourComboBox.setModel(new DefaultComboBoxModel<>(hours));

        // Centering the items in the combobox
        hourComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        hourComboBox.setFont(new java.awt.Font("Tahoma", Font.BOLD, 13));

        // Setting the selected Item to the current hour
        hourComboBox.setSelectedItem(currentYear);

        // Adding the hourComboBox to the panel
        getPanel().add(hourComboBox);

        // Initializing ComboBox to hold valid minutes and setting its properties
        minuteComboBox = new JComboBox<>();
        minuteComboBox.setBounds(baseXPosition + 190 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Initializing minutes vector to store the minutes in the valid range.
        minutes = new Vector<>();
        for (int minuteIterator = currentMinute; minuteIterator < Exam.getMinutesLimit(); ) {
            minutes.add(minuteIterator++);
        }

        // Adding minutes vector to the combobox list of items.
        minuteComboBox.setModel(new DefaultComboBoxModel<>(minutes));

        // Centering the items in the combobox
        minuteComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        minuteComboBox.setFont(new java.awt.Font("Tahoma", Font.BOLD, 13));

        // Setting the selected Item to the current minute
        minuteComboBox.setSelectedItem(currentMinute);

        // Adding the minuteComboBox to the panel
        getPanel().add(minuteComboBox);
    }

    /**
     * This method adds the duration input related GUI components to the panel
     * It does that by using an instance of javax.swing.JComboBox<E> and an
     * instance of javax.swing.JLabel
     */

    private void showDurationPortion() {
        // A base X and Y positions so that changing multiple component at once would be easily done.
        int baseXPosition = 40;
        int baseYPosition = 100;

        // Creating a Label for the duration portion and setting its properties
        JLabel durationLabel = new JLabel("Duration");
        durationLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
        durationLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 90, 40);
        getPanel().add(durationLabel);

        // Initializing ComboBox to hold valid durations and setting its properties
        durationComboBox = new JComboBox<>();
        durationComboBox.setBounds(baseXPosition + 90 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Creating durations vector to store the minutes in the valid range.
        Vector<Integer> durations = new Vector<>();
        for (int minuteIterator = 5; minuteIterator <= Exam.getExamDurationLimit(); minuteIterator += 5) {
            durations.add(minuteIterator);
        }

        // Adding durations vector to the combobox list of items.
        durationComboBox.setModel(new DefaultComboBoxModel<>(durations));

        // Centering the items in the combobox
        durationComboBox.setRenderer(listRenderer);

        // Setting the font to the combobox
        durationComboBox.setFont(new java.awt.Font("Tahoma", Font.BOLD, 13));

        // Adding the durationComboBox to the panel
        getPanel().add(durationComboBox);

        // Creating a Label to indicate to the user that the durations are in minutes and setting its properties
        JLabel minLabel = new JLabel("(min)");
        minLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD + Font.ITALIC, 15));
        minLabel.setBounds(baseXPosition + 170 + deltaXLabelCombo, baseYPosition - deltaYLabelCombo, 90, 40);
        getPanel().add(minLabel);
    }

    /**
     * This method adds the number of models input related GUI components to the panel
     * It does that by using an instance of javax.swing.JComboBox<E> and an instance of
     * javax.swing.JLabel
     */

    private void showModelPortion() {
        // A base X and Y positions so that changing multiple component at once would be easily done.
        int baseXPosition = 430;
        int baseYPosition = 100;

        // Creating a Label for the model portion and setting its properties
        JLabel numberOfModelsLabel = new JLabel("Models Number");
        numberOfModelsLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
        numberOfModelsLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 160, 40);
        getPanel().add(numberOfModelsLabel);

        // Initializing ComboBox to hold numberOfModel in the desired range and setting its properties
        modelComboBox = new JComboBox<>();
        modelComboBox.setBounds(baseXPosition + 160 + deltaXLabelCombo, baseYPosition, 70, 30);

        // Creating models vector to store modelsNumber until the modelNumberLimit Exceeded.
        Vector<Integer> models = new Vector<>();

        // Filling the models vector
        for (int modelIterator = 1; modelIterator <= Exam.getModelNumberLimit(); ) {
            models.add(modelIterator++);
        }

        // Adding models vector to the combobox list
        modelComboBox.setModel(new DefaultComboBoxModel<>(models));

        // Setting the font to the combobox
        modelComboBox.setFont(new java.awt.Font("Tahoma", Font.BOLD, 13));

        // Centering the items in the combobox
        modelComboBox.setRenderer(listRenderer);
        getPanel().add(modelComboBox);

    }

    /**
     * This method adds the exam name input related GUI components to the panel
     * It does that by using an instance of javax.swing.JTextField and javax.swing.JLabel
     */

    private void showNameBox() {
        // A base X and Y positions so that changing multiple component at once would be easily done.
        final int baseXPosition = 40;
        final int baseYPosition = 160;

        // Creating a Label for the name box and setting its properties
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 20));
        nameLabel.setBounds(baseXPosition, baseYPosition - deltaYLabelCombo, 180, 40);
        getPanel().add(nameLabel);

        /*
         * PlaceHolder LOGIC:
          * Since we're using JAVA .. There is no support for a placeholder
          * So we have to go around it somehow .. here is how

            1 - Set the placeHolding text to the textField

            2 - Give it a gray color

            ....

            3 - Implement MouseListener in a newly created class PageMouseListener to override mousePressed

            4 - Make an Instance of our PageMouseListener class and add it to enterExamNameField.ddMouseListener()

            5 - Implement mousePressed So whenever the user clicks inside enterExamNameField Triggers two things:

              iff enterExamNameField.getText().equals("Enter exam name")

                a - Set enterExamNameField text to "" #NOTHING

                b - Give it a black color

             ....

             ******************

             we found out that after the user exit the textField without
             changing anything, the placeholder is gone.

              1 - Add another condition to mousePressed() so that whenever the user press anywhere outside
                    the textField without changing any thing, we would return our placeholder to its rightful place.

              We saw that the cursor stays in the textField which is a bug ..

              2 - We Added - enterExamNameField.transferFocus();

              .. Yusuf Nasser and Youssef Nader
         */

        // Initializing the textField and giving it an initial place holding text in GRAY
        enterExamNameField = new JTextField("Enter exam name");
        enterExamNameField.setForeground(Color.gray);

        // Setting the textField properties
        enterExamNameField.setBounds(baseXPosition + 60 + deltaXLabelCombo, baseYPosition, 200, 35);
        getPanel().add(enterExamNameField);
    }

    /**
     * This method adds the add exam button to the panel
     * It does that by using an instance of javax.swing.JButton
     */

    private void showAddExamButton() {
        // Initializing the addExamButton and setting its properties
        addExamButton = new JButton("Add Exam");
        addExamButton.setBounds(570, 350, 130, 50);
        addExamButton.setFont(new java.awt.Font("Tahoma", Font.BOLD, 16));
        addExamButton.setVisible(true);
        getPanel().add(addExamButton);
    }

    /**
     * This method adds the save changes button to the panel
     * It does that by using an instance of javax.swing.JButton
     */

    private void showSaveChangesButton() {
        // Initializing the saveChangesButton and setting its properties
        saveChangesButton = new JButton("Save Changes");
        saveChangesButton.setBounds(530, 350, 170, 50);
        saveChangesButton.setFont(new java.awt.Font("Tahoma", Font.BOLD, 16));
        saveChangesButton.setVisible(true);
        getPanel().add(saveChangesButton);
    }

    /**
     * This method retrieves the exam basic data from the GUI combobox-es
     * and store them in the member variables and generate the exam start
     * and end date preparing to insert the data in the database.
     */

    private void retrieveDataFromGUI() {
        examDurationTime = (int) durationComboBox.getSelectedItem();
        examStartHour = (int) hourComboBox.getSelectedItem();
        examStartMinute = (int) minuteComboBox.getSelectedItem();
        examStartYear = (int) yearComboBox.getSelectedItem();
        examStartMonth = (int) monthComboBox.getSelectedItem();
        examStartDay = (int) dayComboBox.getSelectedItem();
        examModels = (int) modelComboBox.getSelectedItem();
        examName = enterExamNameField.getText();

        examStartDate = LocalDateTime.of(examStartYear, examStartMonth, examStartDay, examStartHour, examStartMinute);
        examEndDate = examStartDate.plusMinutes(examDurationTime);
    }

    /**
     * This method retrieves the exam edited time and date from their GUI combobox-es
     * and store them in the member variables, set the other member variables to the unedited data
     * generate the exam start and end date preparing to insert the data in the database.
     */

    private void retrieveExamData() {
        // Retrieving the edited data from the GUI
        examStartHour = (int) hourComboBox.getSelectedItem();
        examStartMinute = (int) minuteComboBox.getSelectedItem();
        examStartYear = (int) yearComboBox.getSelectedItem();
        examStartMonth = (int) monthComboBox.getSelectedItem();
        examStartDay = (int) dayComboBox.getSelectedItem();

        // Retrieving the unedited data
        examDurationTime = (int) editedExam.getDuration().toMinutes();
        examModels = editedExam.getModels().size();
        examName = editedExam.getName();

        // Setting the new start and end date and time
        examStartDate = LocalDateTime.of(examStartYear, examStartMonth, examStartDay, examStartHour, examStartMinute);
        examEndDate = examStartDate.plusMinutes(examDurationTime);
    }

    /**
     * This method sets all exam basic data by calling Entities.Exam setters
     * and calls Entities.Exam.add() to add a new exam to the data base and its models
     *
     * @param exam The new exam instance to be filled with its basic data
     */

    private void setExamBasicData(Exam exam) {
        exam.setStartTime(examStartDate);
        exam.setEndTime(examEndDate);
        exam.setDuration(Duration.ofMinutes(examDurationTime));
        exam.setName(examName);
        exam.setNumberOfModels(examModels);
        exam.setIsPublished(false); // TB Changed later after editing the models and adding questions to them
        exam.add();
    }

    /**
     * This method updates all exam basic data by calling Entities.Exam setters
     * and calls Entities.Exam.update() to update the exam after editing it
     *
     * @param exam The new exam instance to be Updated to the edited data
     */

    private void updateExamBasicData(Exam exam) {
        exam.setStartTime(examStartDate);
        exam.setEndTime(examEndDate);
        exam.setDuration(Duration.ofMinutes(examDurationTime));
        exam.setName(examName);
        exam.setNumberOfModels(examModels);
        exam.setIsPublished(false); // TB Changed later after editing the models and adding questions to them
        exam.update();
    }

    /**
     * This method calls the GUI portions methods and draw them on the JFrame.
     * It sets the current date, Initialize the listRenderer member variable to
     * be used in multiple combobox-es, Creates and Initialize the PageActionListener and PageMouseListener
     * instances and add all GUI components to both listeners.
     */

    private void showExamDataInput() {
        // Sets the current date and time
        setCurrentDate();

        // Setting the Alignment of the items in the list
        listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        // Showing the page portions one by one by calling its dedicated method
        showDatePortion();
        showTimePortion();
        showDurationPortion();
        showModelPortion();
        showNameBox();
        showAddExamButton();

        // Creating an action listener instance to track whatever changes might occur while running
        PageActionListener listener = new PageActionListener();

        // Adding the Action Listener to the combobox-es
        yearComboBox.addActionListener(listener);
        monthComboBox.addActionListener(listener);
        dayComboBox.addActionListener(listener);
        hourComboBox.addActionListener(listener);
        minuteComboBox.addActionListener(listener);
        addExamButton.addActionListener(listener);

        // Creating a mouse listener to keep track of mouse events that can occur while running
        PageMouseListener mouseListener = new PageMouseListener();

        // Adding a mouse listener to several GUI components
        getPanel().addMouseListener(mouseListener);
        getTopBar().addMouseListener(mouseListener);
        enterExamNameField.addMouseListener(mouseListener);
        yearComboBox.addMouseListener(mouseListener);
        monthComboBox.addMouseListener(mouseListener);
        dayComboBox.addMouseListener(mouseListener);
        hourComboBox.addMouseListener(mouseListener);
        minuteComboBox.addMouseListener(mouseListener);
        durationComboBox.addMouseListener(mouseListener);
        modelComboBox.addMouseListener(mouseListener);
    }

    /**
     * This method calls the GUI portions methods and draw them on the JFrame.
     * It sets the current date, Initialize the listRenderer member variable to
     * be used in multiple combobox-es, Creates and Initialize the PageActionListener and PageMouseListener
     * instances and add all GUI components to both listeners.
     */

    private void showExamTimeInput() {
        // Sets the current date and time
        setCurrentDate();

        // Setting the Alignment of the items in the list
        listRenderer = new DefaultListCellRenderer();
        listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);

        // Showing the page portions one by one by calling its dedicated method
        showDatePortion();
        showTimePortion();
        showSaveChangesButton();

        // Creating an action listener instance to track whatever changes might occur while running
        PageActionListener listener = new PageActionListener();

        // Adding the Action Listener to the combobox-es
        yearComboBox.addActionListener(listener);
        monthComboBox.addActionListener(listener);
        dayComboBox.addActionListener(listener);
        hourComboBox.addActionListener(listener);
        minuteComboBox.addActionListener(listener);
        saveChangesButton.addActionListener(listener);
    }

    /**
     * This method refreshes the month list so that every month available
     * to the instructor to choose is a valid month.
     * It does that by clearing the month vector, checks the lower limit
     * for the month list and adds all the month from the lower limit up
     * to Exam.getMonthLimit().
     */

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
        }

        // Adding the valid months to the vector
        while (monthIterator <= Exam.getMonthLimit()) {
            months.add(monthIterator++);
        }

        // Adding months vector to the combobox list of items.
        monthComboBox.setModel(new DefaultComboBoxModel<>(months));

        // Setting the current month to avoid erasing the user's selection if not necessary
        if (selectedMonth < monthLowerLimit) selectedMonth = currentMonth;
        monthComboBox.setSelectedItem(selectedMonth);
    }

    /**
     * This method refreshes the day list so that every day available
     * to the instructor to choose is a valid day.
     * It does that by clearing the day vector, checks the lower limit
     * for the month list and adds all the month from the lower limit up
     * to the upper limit returned from getDayLimit(selectedMonth, selectedYear).
     */

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
        dayComboBox.setModel(new DefaultComboBoxModel<>(days));

        // Getting the current month to avoid erasing the user's selection if not necessary
        if (selectedDay < dayLowerLimit) selectedDay = currentDay;
        dayComboBox.setSelectedItem(selectedDay);
    }

    /**
     * This method refreshes the hour list so that every hour available
     * to the instructor to choose is a valid hour.
     * It does that by clearing the hour vector, checks the lower limit
     * for the hour list and adds all the hours from the lower limit up
     * to but not including Exam.getHourLimit().
     */

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
        hourComboBox.setModel(new DefaultComboBoxModel<>(hours));

        // Setting the current hour to avoid erasing the user's selection if not necessary
        if (selectedHour < hourLowerLimit) selectedHour = currentHour;
        hourComboBox.setSelectedItem(selectedHour);
    }

    /**
     * This method refreshes the minute list so that every minute available
     * to the instructor to choose is a valid minute.
     * It does that by clearing the minute vector, checks the lower limit
     * for the minute list and adds all the minutes from the lower limit up
     * to but not including Exam.getMinutesLimit().
     */

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
        minuteComboBox.setModel(new DefaultComboBoxModel<>(minutes));

        // Setting the selected minute to avoid erasing the user's selection if not necessary
        if (selectedMinute < minuteLowerLimit) selectedMinute = currentMinute;
        minuteComboBox.setSelectedItem(selectedMinute);
    }

    /**
     * This method checks if the user entered a name for the exam, it does
     * that by checking the color of the text returned as if it's black that
     * means the user have write down something and replaced the gray place
     * holder AND checking its size after trimming all white spaces.
     *
     * @return true if the foreground color is black and not empty; false otherwise.
     */

    boolean isNameEntered() {
        // Trimming the nameEntered from all white spaces.
        String nameEntered = enterExamNameField.getText().trim();

        // Checking for the color and length after trimming.
        return enterExamNameField.getForeground().equals(Color.black)
                && !(nameEntered.isEmpty());
    }

    /**
     * This method shows the instructor the exam basic data entered and
     * asks whether the instructor wants to submit it or edit it.
     * It does that via showConfirmDialog() that only take Yes or No as an answer
     *
     * @return true if the instructor is sure about the exam data; false otherwise.
     */

    boolean userIsSure() {
        // Generating the message from the exam basic data
        String message = "Exam name: " + examName;
        message += "\nDuration: " + examDurationTime + " min";
        message += ("\nStart time: " + examStartHour + " : " + examStartMinute);
        message += ("\nDate: " + examStartDay + " - " + examStartMonth + " - " + examStartYear);
        message += ("\nNumber of models: " + examModels + "\n");

        // Showing the data to the instructor and returning the answer.
        return JOptionPane.showConfirmDialog(null, message, "Are you sure to submit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /**
     * This method checks if a year is leap or not, It does that via
     * several if conditions.
     *
     * @param year the year in question
     * @return true if the year is a leap year; false otherwise;
     */

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

    /**
     * It checks which month and returns its fixed limit and if it's February,
     * checks for leap year as well.
     *
     * @param month the month in question
     * @param year  the year in question
     * @return how many days in the month and the year in question
     */

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

    /**
     * An Implementation for the ActionListener class used to override
     * actionPerformed() and acting upon every event occurring while running.
     *
     * @author Yusuf Nasser
     */

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
                } else {
                    retrieveDataFromGUI();
                    if (userIsSure()) {
                        setExamBasicData(newExam);
                        new ViewModels(instructor, newExam.getModels().firstElement()).setVisible(true);
                        dispose();
                    }
                }
            } else if (event.getSource() == saveChangesButton) {
                retrieveExamData();
                if (userIsSure()) {
                    updateExamBasicData(editedExam);
                    new ViewModels(instructor, editedExam.getModels().firstElement()).setVisible(true);
                    dispose();
                }
            }
        }
    }

    /**
     * An Implementation for the MouseListener class used to override
     * mousePressed() and acting upon every event mouse press while running.
     * It is used to support the implementation of creating a place holder in JTextField.
     *
     * @author Yusuf Nasser
     */

    private class PageMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == enterExamNameField) {
                if (enterExamNameField.getText().equals("Enter exam name")) {
                    enterExamNameField.setText("");
                    enterExamNameField.setForeground(Color.black);
                }
            } else if (e.getSource() != enterExamNameField) {
                if (enterExamNameField.getText().isEmpty()) {
                    enterExamNameField.setForeground(Color.gray);
                    enterExamNameField.setText("Enter exam name");
                    enterExamNameField.transferFocus(); // so that the cursor would move away from the textField
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * This method is used for running and testing AddExam.java while in development
     *
     * @param args arguments that can be passed in running the program
     */

    public static void main(String[] args) {
        Instructor instructor = new Instructor("aliyaser");
        Entities.Class classes = new Entities.Class(1, false);
        AddExam exam = new AddExam(instructor, classes);
        exam.setVisible(true);
    }
}
