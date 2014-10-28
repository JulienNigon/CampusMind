package view.system;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import kernel.Config;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;

import agents.Agent;
import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import blackbox.Input;

public class GrapheBlackBoxPanel extends JPanel{
	
	Graph graph;
	Viewer viewer;
	BlackBox blackBox;
	
	/* ----ToolBar Components----*/
	private JToolBar toolBar;
	private JButton buttonShowValue;
	private JButton buttonShowDefault;
	private JButton buttonShowName;
	
	private int viewMode = 0;


	public GrapheBlackBoxPanel() {
		setLayout(new BorderLayout());

		this.setMinimumSize(new Dimension(400,400));

		toolBar = new JToolBar();

		buttonShowDefault = new JButton(Config.getIcon("tag--plus.png"));
		buttonShowDefault.addActionListener(e -> {showDefault();});
		toolBar.add(buttonShowDefault);

		buttonShowValue = new JButton(Config.getIcon("tag--exclamation.png"));
		buttonShowValue.addActionListener(e -> {showValue();});
		toolBar.add(buttonShowValue);

		buttonShowName = new JButton(Config.getIcon("tag.png"));
		buttonShowName.addActionListener(e -> {showName();});
		toolBar.add(buttonShowName);
		
		this.add(toolBar,BorderLayout.NORTH);

		
		//update();
		
	}
	
	public void showValue() {
		viewMode = 1;
		if (blackBox != null) {
			for (String name : blackBox.getBlackBoxAgents().keySet()) {
				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getValue());		
			}
		}
	}
	
	public void showDefault() {
		viewMode = 0;
		if (blackBox != null) {
			for (String name : blackBox.getBlackBoxAgents().keySet()) {
				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getName() + " " + bba.getValue());		
			}
		}
	}
	
	public void showName() {
		viewMode = 2;
		if (blackBox != null) {
			for (String name : blackBox.getBlackBoxAgents().keySet()) {
				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getName());		
			}
		}
	}
	
	public void setBlackBox(BlackBox blackBox) {
		this.blackBox = blackBox;
		createGraph();
	}
	
	public void update () {
		switch(viewMode) {
		
		case 0 : 
			showDefault();
			break;
			
		case 1 : 
			showValue();
			break;
			
		case 2 :
			showName();
			break;
		}
	}
	
	
	private void createGraph() {
		graph = new SingleGraph("BLACK BOX");
		for (String name : blackBox.getBlackBoxAgents().keySet()) {
			BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);
			graph.addAttribute("ui.stylesheet", "url('file:/home/nigon/git/CampusMind/CampusMind/src/styles/styleBlackBox.css')");
			
		/*	graph.addAttribute("ui.stylesheet", "node { stroke-mode: plain;"
					+ "fill-color: red;"
					+ "shape: box;"
					+ "stroke-color: yellow;"
					+ " }");//text-mode
			graph.addAttribute("ui.style", "shape: box;");*/

			//graph.addAttribute("ui.stylesheet", "node { text-mode: normal; }");//text-mode

			//graph.
			graph.addNode(bba.getName());
			graph.getNode(bba.getName()).addAttribute("ui.class", bba.getClass().getSimpleName());
			graph.getNode(bba.getName()).addAttribute("ui.label", bba.getName());
			//graph.getNode(bba.getName()).;
			//System.out.println(graph.getNode(bba.getName()).getLabe)());
			
		}

		//Draw edge
		for (String name : blackBox.getBlackBoxAgents().keySet()) {
			BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);

			for (Agent target : bba.getTargets()) {
				graph.addEdge(bba.getName() + " " + target.toString(), bba.getName(), target.getName(), true);				
			}
			
		}

		
/*		graph.addNode("XXX");
		graph.addNode("YYY");
		graph.addNode("ZZZ");
		graph.addNode("A" );
		graph.addNode("B" );
		graph.addNode("C" );
		graph.addEdge("AB", "A", "B");
		graph.addEdge("BC", "B", "C");
		graph.addEdge("CA", "C", "A");
		graph.addEdge("XY", "XXX", "YYY");**/

		viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_SWING_THREAD);
		viewer.addDefaultView(false);
		viewer.enableAutoLayout();

		viewer.getDefaultView().setMinimumSize(new Dimension(400,400));
		this.add(viewer.getDefaultView(),BorderLayout.CENTER);
	}
}
