package view.global;

import java.awt.FlowLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import kernel.World;
import ncs.NCS;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import view.system.ScheduledItem;
import agents.criterion.Criterion;

public class PanelChart extends JPanel implements ScheduledItem {

	/* Agents chart */
	ChartPanel chartPanelAgents;
	JFreeChart chartAgents;
	XYSeriesCollection dataSetAgents;

	/* NCS chart */
	ChartPanel chartPanelNCS;
	JFreeChart chartNCS;
	XYSeriesCollection dataSetNCS;

	World world;

	public PanelChart(World world) {

		this.setLayout(new FlowLayout());
		this.world = world;

		/* Create Agent chart */
		dataSetAgents = createDataSetAgents();
		JFreeChart chart = createChart();
		chartPanelAgents = new ChartPanel(chart);
		chartPanelAgents.setPreferredSize(new java.awt.Dimension(600, 400));
		this.add(chartPanelAgents);

		/* Create NCS chart */
		dataSetNCS = createDataSetNCS();
		JFreeChart chartNCS = createChartNCS();
		chartPanelNCS = new ChartPanel(chartNCS);
		chartPanelNCS.setPreferredSize(new java.awt.Dimension(600, 400));
		this.add(chartPanelNCS);

	}

	/*
	 * private JFreeChart createChart(DefaultCategoryDataset dataset2) { // TODO
	 * Auto-generated method stub return null; }
	 * 
	 * private DefaultCategoryDataset createDataset() { // TODO Auto-generated
	 * method stub return null; }
	 */

	private XYSeriesCollection createDataSetAgents() {

		XYSeriesCollection collection = new XYSeriesCollection();

		collection.addSeries(new XYSeries("Context"));
		collection.addSeries(new XYSeries("Variable"));
		collection.addSeries(new XYSeries("Controller"));
		collection.addSeries(new XYSeries("Criterion"));

		return collection;

	}

	private XYSeriesCollection createDataSetNCS() {

		XYSeriesCollection collection = new XYSeriesCollection();

		
		for (NCS ncs : NCS.values()) {
			collection.addSeries(new XYSeries(ncs.toString()));
		}
		return collection;

	}

	private JFreeChart createChart() {

		// create subplot 1...
		final XYDataset data1 = dataSetAgents;
		final XYItemRenderer renderer1 = new StandardXYItemRenderer();
		final NumberAxis rangeAxis1 = new NumberAxis("Agents in AMAS");
		final XYPlot subplot1 = new XYPlot(data1, null, rangeAxis1, renderer1);
		subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		// parent plot...
		final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
				new NumberAxis("Tick"));
		plot.setGap(10.0);

		// add the subplots...
		plot.add(subplot1, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);

		// return a new chart containing the overlaid plot...
		return new JFreeChart(" Agents in AMAS",
				JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	}

	private JFreeChart createChartNCS() {

		// create subplot 1...
		final XYDataset data1 = dataSetNCS;
		final XYItemRenderer renderer1 = new StandardXYItemRenderer();
		final NumberAxis rangeAxis1 = new NumberAxis("Total number of NCS");
		final XYPlot subplot1 = new XYPlot(data1, null, rangeAxis1, renderer1);
		subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		// parent plot...
		final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
				new NumberAxis("Tick"));
		plot.setGap(10.0);

		// add the subplots...
		plot.add(subplot1, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);

		// return a new chart containing the overlaid plot...
		return new JFreeChart("Total of NCS",
				JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	}

	@Override
	public void update() {

		int tick = world.getScheduler().getTick();
		
		dataSetAgents.getSeries("Criterion").add(
				tick, world.getNumberOfAgents().get("Criterion"));
		dataSetAgents.getSeries("Context").add(
				tick, world.getNumberOfAgents().get("Context"));
		dataSetAgents.getSeries("Variable").add(
				tick, world.getNumberOfAgents().get("Variable"));
		dataSetAgents.getSeries("Controller").add(
				tick, world.getNumberOfAgents().get("Controller"));

		
		for (NCS ncs : NCS.values()) {
			dataSetNCS.getSeries(ncs.toString()).add(tick, world.getNumberOfNCS().get(ncs));
		}

	}

	// }

}
