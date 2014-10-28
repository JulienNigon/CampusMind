package view.system;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import view.animation.JJPanel;

import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.table.DefaultTableModel;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.Viewer;

import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import kernel.World;

public class BlackBoxPanel extends JPanel{

	DefaultTableModel tableModel;
	private World world;
	GrapheBlackBoxPanel graphPanel;
	
	public BlackBoxPanel(World world) {
		this.world = world;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JSplitPane splitPane_1 = new JSplitPane();
		add(splitPane_1);
		
		graphPanel = new GrapheBlackBoxPanel();
		splitPane_1.setLeftComponent(graphPanel);
	//	this.add(graphPanel);
		
		String col[] = {"Type","Name","ID","Value"};
		tableModel = new DefaultTableModel(col, 0);
		
		update();
		
		JTable table = new JTable(tableModel);
		splitPane_1.setRightComponent(table);
		
	/*	Graph graph = new SingleGraph("I can see dead pixels");
		graph.addNode("XXX");
		graph.addNode("YYY");
		graph.addNode("ZZZ");
		graph.addNode("A" );
		graph.addNode("B" );
		graph.addNode("C" );
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
		viewer.addDefaultView(false);

		viewer.getDefaultView().setMinimumSize(new Dimension(400,400));
		this.add(viewer.getDefaultView());*/
		
	}
	
	
	public void update(){
		tableModel.setRowCount(0);
		
		Object[] entete = {"TYPE","NAME","ID","VALUE"};
		tableModel.addRow(entete);
		
		BlackBox bb = world.getBlackbox();
		HashMap<String, BlackBoxAgent> hashAgents = bb.getBlackBoxAgents();
		for (String s : hashAgents.keySet()) {
			BlackBoxAgent a = hashAgents.get(s);
			Object[] data = {
					a.getClass().getSimpleName(),
					a.getName(),
					a.getID(),
					a.getValue()
					};
			tableModel.addRow(data);
		}
		
		graphPanel.update();
		
	}


	public DefaultTableModel getTableModel() {
		return tableModel;
	}


	public void setTableModel(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
	}


	public World getWorld() {
		return world;
	}


	public void setWorld(World world) {
		this.world = world;
	}
	
	public void setBlackBox(BlackBox blackBox) {
		graphPanel.setBlackBox(blackBox);
	}
}
