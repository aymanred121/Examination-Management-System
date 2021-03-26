
package GUI;
import javax.swing.JFrame;
import java.sql.ResultSet;
import Entities.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

/* TODO
*   add jcommon-1.0.0.jar
*   and jfreechart-1.0.1.jar from dependencies\jfreechart-1.0.1\lib to the library
*   of the project structer in order to use the freechart lib*/

/**
 * Draw a HistogramChart using JfreeChart lib
 * @author Ayman Hassan, Yusuf Nasser, Youssef Nader
 */

public class Histogram extends JFrame {
    private double[] data;
    private ResultSet myResultSet;
    private JFreeChart HistogramChart;
    private String title, xAxis, yAxis;
    public Histogram(double[] data, String title, String xAxis, String yAxis) {
        this.data = data;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.title = title;
        Bar();
    }

    /**
     * Create a HistogramChart by initializing the title and the AxisLable on both X and Y, then
     * passing the dataset from createDataset method, and setting the direction of the drawing.
     */

    private void Bar(){
        HistogramDataset dataset= new HistogramDataset();
        dataset.addSeries("key",data,20);
         HistogramChart = ChartFactory.createHistogram(
                title,
                xAxis,
                yAxis,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
    }

    public JFreeChart getHistogramChart() {
        return HistogramChart;
    }

    /**
     * retrieves and returns the students' exams data upon which the histogram is based
     * @param exam — the exam to retrieve its students data
     * @return array of doubles contains the data
     */

    public static double[] HistogramData(Exam exam){
        int studentIndex = 0, studentCount = exam.getStudentCount();
        double[] studentMarksData = new double[studentCount];

        for (Student student :  exam.getExamClass().getStudents() )
        {
            studentMarksData[studentIndex] = exam.getStudentMark(student.getUsername());
            studentIndex++;
        }

        return studentMarksData;
    }

}

