
package GUI;
import javax.jws.WebParam;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Entities.Model;
import Entities.Question;
import Entities.SqlConnection;
import Entities.Student;
import javafx.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.RefineryUtilities;
/* TODO
*   add jcommon-1.0.0.jar
*   and jfreechart-1.0.1.jar from dependencies\jfreechart-1.0.1\lib to the library
*   of the project structer in order to use the freechart lib*/
/*
 *Draw a HistogramChart using JfreeChart lib
 *
 * @author Ayman Hassan
  */
public class Histogram extends JFrame {
    private double[] data;
    private ResultSet myResultSet;
    private JFreeChart HistogramChart;
    public Histogram(double[] data) {
        this.data=data;
        Bar();
    }
//    private CategoryDataset createDataset(){
//        /*
//        * iterate through the Arraylist then pass the pairs values to the dataset
//        * so we could draw a HistogramChart using this data
//        * */
//        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
//        for (Pair<Integer,Integer>itr : data)
//        {
//            dataset.addValue(itr.getValue(),"no of occurrence",itr.getKey());
//        }
//        return dataset;
//    }
    private void Bar(){
        /*
        * Create a HistogramChart by initializing the title and the AxisLable on both X and Y, then passing the dataset
        * from createDataset method, and setting the direction of the drawing.
        * */
        HistogramDataset dataset= new HistogramDataset();
        dataset.addSeries("key",data,20);
         HistogramChart = ChartFactory.createHistogram(
                "the occurrence of the expected difficulty",
                "expected difficulty",
                    "No. of Question",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        /*
        * Create a panel of the HistogramChart to display the content on it.
        * */

//        ChartPanel chartPanel = new ChartPanel( HistogramChart );
//        chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );
//        setContentPane( chartPanel );
    }

    public JFreeChart getHistogramChart() {
        return HistogramChart;
    }

    //testing area
    public static void main(String[] args) throws Exception {
        double[] values = { 95, 49, 14, 59, 50, 66, 47, 40, 1, 67,
                12, 58, 28, 63, 14, 9, 31, 17, 94, 71,
                49, 64, 73, 97, 15, 63, 10, 12, 31, 62,
                93, 49, 74, 90, 59, 14, 15, 88, 26, 57,
                77, 44, 58, 91, 10, 67, 57, 19, 88, 84
        };
        Histogram bar=new Histogram(values);
        bar.pack();
        RefineryUtilities.centerFrameOnScreen(bar);
        bar.setVisible(true);
        bar.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Model model;
    }

}

