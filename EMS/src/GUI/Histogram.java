package GUI;

import javax.swing.JFrame;

import Entities.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

/* TODO
 *   add jcommon-1.0.0.jar
 *   and jfreechart-1.0.1.jar from dependencies\jfreechart-1.0.1\lib to the library
 *   of the project structer in order to use the freechart lib
 */

/**
 * Draws a HistogramChart using JfreeChart library
 *
 * @author Ayman Hassan, Yusuf Nasser, Youssef Nader
 */

public class Histogram extends JFrame {
    private final double[] data;
    private JFreeChart HistogramChart;
    private final String title, xAxis, yAxis;

    /**
     * Constructs a Histogram Chart to exemplify a certain dataset in a graphical way to be used as
     * a statistical representation.
     *
     * @param data  The data upon which the histogram is based
     * @param title The title of the histogram — the meaning of the relation represented between X and Y axes values
     * @param xAxis xAxis Label — the element represented by X Axis values
     * @param yAxis yAxis Label — the element represented by Y Axis values
     */

    public Histogram(double[] data, String title, String xAxis, String yAxis) {
        this.data = data;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.title = title;
        initHistogramData();
    }

    /**
     * Creates and initialize the histogram's dataset then initializes
     * the JFreeChart instance in order to create the histogram.
     */

    private void initHistogramData() {
        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Grades", data, 10);
        HistogramChart = ChartFactory.createHistogram(
                title,
                xAxis,
                yAxis,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
    }

    /**
     * Returns the Histogram Chart
     *
     * @return the value of HistogramChart property
     */

    public JFreeChart getHistogramChart() {
        return HistogramChart;
    }

    /**
     * retrieves and returns the students' exams data upon which the histogram is based
     *
     * @param exam — the exam to retrieve its students data
     * @return array of doubles contains the data
     */

    public static double[] HistogramData(Exam exam) {
        int studentIndex = 0, studentCount = exam.getStudentCount();
        double[] studentMarksData = new double[studentCount];

        for (Student student : exam.getExamClass().getStudents()) {
            studentMarksData[studentIndex] = exam.getStudentMark(student.getUsername());
            studentIndex++;
        }

        return studentMarksData;
    }

}

