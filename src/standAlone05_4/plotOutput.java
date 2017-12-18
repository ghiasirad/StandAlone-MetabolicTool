// Creates a line chart

package standAlone05_4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class plotOutput{
	public void lineChart (ArrayList<Double> output, String whatToWhatch, Double stepSize) throws IOException {
		DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();

		line_chart_dataset.addValue(output.get(0), "Concentrations", "0");
		for (int i = 1 ; i < output.size() ; i++){
			line_chart_dataset.addValue(output.get(i), "Concentrations", Double.toString(stepSize*i));
		}

        /*line_chart_dataset.addValue( 15 , "schools" , "1970" );
        line_chart_dataset.addValue( 30 , "schools" , "1980" );
        line_chart_dataset.addValue( 60 , "schools" , "1990" );
        line_chart_dataset.addValue( 120 , "schools" , "2000" );
        line_chart_dataset.addValue( 240 , "schools" , "2010" ); 
        line_chart_dataset.addValue( 300 , "schools" , "2014" );*/

        JFreeChart lineChartObject = ChartFactory.createLineChart(
        		whatToWhatch + " Concentration","Time (Second)",
           "Concentration (miliMole/Liter)",
           line_chart_dataset,PlotOrientation.VERTICAL,
           true,true,false);

        int width = 1080;    /* Width of the image */
        int height = 720;   /* Height of the image */ 
        File lineChart = new File( "LineChart" + whatToWhatch + ".jpeg" ); 
        ChartUtilities.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);
	}
}
