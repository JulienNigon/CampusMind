package view.system;

import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import agents.criterion.Criterion;

public class PanelCriterion extends JPanel implements ScheduledItem{

	ChartPanel chartPanel;
	JFreeChart chart;
	XYSeriesCollection dataset;
	Criterion criterion;
	
	public PanelCriterion(Criterion criterion) {
  
		this.setLayout(new FlowLayout());
		this.criterion = criterion;
		this.add(new JLabel("test"));
		dataset = createDataset();
       /* JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(590, 350));
        this.add(chartPanel);*/
		
        
	}
/*
	private JFreeChart createChart(DefaultCategoryDataset dataset2) {
		// TODO Auto-generated method stub
		return null;
	}

	private DefaultCategoryDataset createDataset() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
    private XYSeriesCollection createDataset() {

        XYSeriesCollection collection = new XYSeriesCollection();
        
    	collection.addSeries(new XYSeries("criticity"));

        return collection;

    }
	

	@Override
	public void update() {
		//System.out.println("Update a panel criterion");
		
	}
	
	
}
