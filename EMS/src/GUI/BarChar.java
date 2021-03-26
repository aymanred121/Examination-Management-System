
package GUI;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Entities.SqlConnection;
import javafx.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
/* TODO
*   add jcommon-1.0.0.jar
*   and jfreechart-1.0.1.jar from dependencies\jfreechart-1.0.1\lib to the library
*   of the project structer in order to use the freechart lib*/
/*
 *Draw a BarChart using JfreeChart lib
 *
 * @author Ayman Hassan
  */
public class BarChar extends JFrame {
    private ArrayList<Pair<Integer,Integer>> data;
    private Connection myConnection;
    private ResultSet myResultSet;
    public BarChar(String appTitle) {
        super(appTitle);
        data=getData();
        Bar();
    }
    private ArrayList getData() {
        /*
         * initialize Arraylist of Pair of int so we can keep track of each grade as a key and the number of it's
         * occurrence as a value
         * by pulling the data directly from the DB
          */
        ArrayList <Pair<Integer,Integer>> data = new ArrayList<>();
        /* Note
        *   make exam with 1 as it's id and add some question to test the sql statement */
        myConnection = SqlConnection.getConnection();
        try {
            PreparedStatement myStatement = myConnection.prepareStatement("select  q.expecteddifficulty,COUNT(q.expecteddifficulty) AS numInstances\n" +
                    "from question q\n" +
                    "where q.examid=1\n" +
                    "GROUP BY q.expecteddifficulty");
             myResultSet = myStatement.executeQuery();
            while (myResultSet.next()) {
                data.add(new Pair<>(myResultSet.getInt(1),myResultSet.getInt(2)));

            }
            myConnection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        //System.out.println(data.get(0).getKey());

        return data;
    }
    private CategoryDataset createDataset(){
        /*
        * iterate through the Arraylist then pass the pairs values to the dataset
        * so we could draw a barchart using this data
        * */
        DefaultCategoryDataset dataset=new DefaultCategoryDataset();
        for (Pair<Integer,Integer>itr : data)
        {
            dataset.addValue(itr.getValue(),"no of occurrence",itr.getKey());
        }
        return dataset;
    }
    private void Bar(){
        /*
        * Create a barchart by initializing the title and the AxisLable on both X and Y, then passing the dataset
        * from createDataset method, and setting the direction of the drawing.
        * */
        JFreeChart barChart = ChartFactory.createBarChart(
                "the occurrence of the expected difficulty",
                "expected difficulty",
                "No. of Question",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        /*
        * Create a panel of the barchart to display the content on it.
        * */

        ChartPanel chartPanel = new ChartPanel( barChart );
        chartPanel.setPreferredSize(new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }


        //testing area
    public static void main(String[] args) throws Exception {
        BarChar bar=new BarChar("just a title");
        //bar.pack();
        RefineryUtilities.centerFrameOnScreen(bar);
        bar.setVisible(true);
        bar.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

}

