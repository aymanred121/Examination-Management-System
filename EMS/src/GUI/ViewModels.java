package GUI;

import Entities.*;

import java.awt.Font;
import java.time.LocalDateTime;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Views the exam models to the instructor so he or she would edit it;
 * if the exam is yet to published, or view the models' questions if it's
 * already published. Questions can also be ranked.
 *
 * @author Abdel-Aziz Mostafa, Ayman Hassan
 */

public class ViewModels extends Page {

    private final Instructor instructor;
    private final Model model;
    private final Exam exam;
    private boolean isRanked;
    private final Font myFont = new java.awt.Font("Tahoma", Font.PLAIN, 11);

    /**
     * Constructs a JFrame page in order to view the instructor a certain exam model
     *
     * @param instructor The exam's class instructor
     * @param model      The model to be viewed
     */

    public ViewModels(Instructor instructor, Model model) {
        setSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        this.instructor = instructor;
        this.model = model;
        exam = new Exam(model.getExamID());
        getBackButton().addActionListener(e -> {
            new ViewExams(instructor, exam.getExamClass()).setVisible(true);
            dispose();
        });

        showQuestions();
        showTopBarElements();
    }

    /**
     * Constructs a JFrame page in order to view the instructor a certain
     * exam model's questions ranked.
     *
     * @param instructor The exam's class instructor
     * @param model      The model to be viewed
     */

    public ViewModels(Instructor instructor, Model model, boolean isRanked) {
        this(instructor, model);
        this.isRanked = isRanked;
    }

    /**
     * Shows the top bar components — previous & next buttons, model's number and exam's title.
     */

    private void showTopBarElements() {
        int baseXPosition = 160, baseYPosition = 42;

        getTitleLabel().setText("Model " + model.getModelNumber() + '/' + exam.getModels().size());

        JButton previousModelButton = new JButton("Previous");
        previousModelButton.setBounds(baseXPosition, baseYPosition, 90, 30);
        getTopBar().add(previousModelButton);

        if (model.getModelNumber() == 1) {
            previousModelButton.setEnabled(false);
        } else {
            previousModelButton.addActionListener(e -> {
                new ViewModels(instructor, new Model(exam.getId(), model.getModelNumber() - 1)).setVisible(true);
                dispose();
            });
        }

        JButton nextModelButton = new JButton("Next");
        nextModelButton.setBounds(baseXPosition + 100, baseYPosition, 90, 30);
        getTopBar().add(nextModelButton);

        if (model.getModelNumber() == exam.getModels().size()) {
            nextModelButton.setEnabled(false);
        } else {
            nextModelButton.addActionListener(e -> {
                new ViewModels(instructor, new Model(exam.getId(), model.getModelNumber() + 1)).setVisible(true);
                dispose();
            });
        }

    }

    /**
     * <p>Shows the model's questions to the instructor — It only shows the question statement
     * with an option to view the question by pressing the button.</p>
     * <p>It allows editing the model if it's yet to be published</p>
     */

    private void showQuestions() {
        Vector<Question> questions;

        if (isRanked) {
            questions = exam.getTopQuestions(model.getQuestions().size());
        } else {
            questions = model.getQuestions();
        }

        int delta = 0, currentIndex = 0;
        for (Question question : questions) {
            // Creating, initializing, showing the question's number label.
            JLabel questionNumber = new JLabel("Q" + (currentIndex + 1) + ": ");
            questionNumber.setBounds(20, delta, 300, 80);
            questionNumber.setFont(myFont);
            getPanel().add(questionNumber);

            // Creating, initializing, showing the question's statement text field.
            JTextField questionStatement = new JTextField(question.getStatement());
            questionStatement.setEditable(false);
            questionStatement.setBounds(50, 25 + delta, 550, 30);
            questionStatement.setFont(myFont);
            getPanel().add(questionStatement);

            // Creating, initializing, setting then adding the question button to the panel.
            JButton showQuestionButton = new JButton("Question");
            showQuestionButton.setFont(myFont);
            showQuestionButton.setBounds(630, 25 + delta, 130, 30);
            showQuestionButton.addActionListener(e -> {
                new ViewQuestion(instructor, question, model, false).setVisible(true);
                dispose();
            });
            getPanel().add(showQuestionButton);

            currentIndex++;
            delta += 45; // Adjusting the next question vertical position on the panel
        }

        // In case of the exam is yet to be published, editing and publishing are allowed

        if (exam.getStatus() == Exam.Status.UNPUBLISHED) {
            // Creating, initializing, setting then adding the add question button to the panel.
            JButton addQuestionButton = new JButton("Add Question");
            addQuestionButton.setBounds(490, 50 + delta, 130, 30);
            getPanel().add(addQuestionButton);

            // Adding an action listener to the button so it would view the question when pressed
            addQuestionButton.addActionListener(e -> {
                new ViewQuestion(instructor, model).setVisible(true);
                dispose();
            });

            // Creating, initializing, setting then adding the publish exam button to the panel.
            JButton publishExamButton = new JButton("Publish Exam");
            publishExamButton.setBounds(630, 50 + delta, 130, 30);
            getPanel().add(publishExamButton);

            /*
             * Adding an action listener to the button so whenever it is pressed,
             * it would start the validating process and then publishing process if possible
             */

            publishExamButton.addActionListener(e -> {
                if (exam.getStartTime().isBefore(LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(null, "The exam start time is Invalid, You will be directed to Edit it to a new valid date.");
                    new AddExam(instructor, exam).setVisible(true);
                    dispose();
                } else if (exam.isReadyToPublish()) {
                    showAlertMessage("The exam has been published successfully");
                    exam.publish();
                    new ViewExams(new Instructor(instructor.getUsername()), new Entities.Class(exam.getExamClass().getId(), false)).setVisible(true);
                    dispose();
                } else {
                    showAlertMessage("The exam cannot be published. Please make sure that:\n"
                            + "1- All the models have the same number of questions.\n"
                            + "2- There is at least one question in each model.");
                }
            });
        }
    }
}


