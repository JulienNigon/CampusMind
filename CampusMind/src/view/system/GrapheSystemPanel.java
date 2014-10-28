package view.system;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import kernel.Config;
import kernel.World;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;

import agents.Agent;
import agents.SystemAgent;
import agents.context.Context;
import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import blackbox.Input;

public class GrapheSystemPanel extends JPanel{
	
	Graph graph;
	Viewer viewer;
	World world;
	
	/* ----ToolBar Components----*/
	private JToolBar toolBar;
	private JButton buttonShowValue;
	private JButton buttonShowDefault;
	private JButton buttonShowName;
	private JButton buttonDestroyContext;
	private JButton buttonSoftStyle;
	private JButton buttonStandardStyle;

	private int viewMode = 0;


	public GrapheSystemPanel() {
		setLayout(new BorderLayout());

		this.setMinimumSize(new Dimension(400,400));

		toolBar = new JToolBar(null, JToolBar.VERTICAL);

		buttonShowDefault = new JButton(Config.getIcon("tag--plus.png"));
		buttonShowDefault.addActionListener(e -> {showDefault();});
		toolBar.add(buttonShowDefault);

		buttonShowValue = new JButton(Config.getIcon("tag--exclamation.png"));
		buttonShowValue.addActionListener(e -> {showValue();});
		toolBar.add(buttonShowValue);

		buttonShowName = new JButton(Config.getIcon("tag.png"));
		buttonShowName.addActionListener(e -> {showName();});
		toolBar.add(buttonShowName);
		
		toolBar.addSeparator();
		
		buttonSoftStyle = new JButton(Config.getIcon("tag.png"));
		buttonSoftStyle.addActionListener(e -> {setSoftStyle();});
		toolBar.add(buttonSoftStyle);
		
		buttonStandardStyle = new JButton(Config.getIcon("tag.png"));
		buttonStandardStyle.addActionListener(e -> {setStandardStyle();});
		toolBar.add(buttonStandardStyle);
		
		toolBar.addSeparator();

		buttonDestroyContext = new JButton(Config.getIcon("eraser.png"));
		buttonDestroyContext.addActionListener(e -> {destroyContext();});
		toolBar.add(buttonDestroyContext);
		
		this.add(toolBar,BorderLayout.WEST);

		
		//update();
		
	}
	
	public void setStandardStyle() {
		graph.removeAttribute("ui.stylesheet");
		graph.addAttribute("ui.stylesheet", "url('file:/home/nigon/git/CampusMind/CampusMind/src/styles/styleSystem.css')");
	}
	
	public void setSoftStyle() {
		graph.removeAttribute("ui.stylesheet");
		graph.addAttribute("ui.stylesheet", "url('file:/home/nigon/git/CampusMind/CampusMind/src/styles/styleSystemSoft.css')");
	}
	
	public void destroyContext() {
		world.destroy(Context.class);
	}
	
	public void showValue() {
		viewMode = 1;
		if (world != null) {
//			for (String name : blackBox.getBlackBoxAgents().keySet()) {
//				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
//				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getValue());		
//			}
		}
	}
	
	public void showDefault() {
		viewMode = 0;
		if (world != null) {
//			for (String name : blackBox.getBlackBoxAgents().keySet()) {
//				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
//				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getName() + " " + bba.getValue());		
//			}
		}
	}
	
	public void showName() {
		viewMode = 2;
		if (world != null) {
//			for (String name : blackBox.getBlackBoxAgents().keySet()) {
//				BlackBoxAgent bba = blackBox.getBlackBoxAgents().get(name);	
//				graph.getNode(bba.getName()).setAttribute("ui.label", bba.getName());		
//			}
		}
	}
	
	public void setWorld(World world) {
		this.world = world;
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
		
		//graph.clear();
		for (String name : world.getAgents().keySet()) {
			SystemAgent a = world.getAgents().get(name);
			
			if (graph.getNode(name) != null) {
				//do nothing
			} else {
				graph.addNode(name);
				graph.getNode(name).addAttribute("ui.class", a.getClass().getSimpleName());
				graph.getNode(name).addAttribute("ui.label", a.getName());
			}
			graph.getNode(name).addAttribute("EXIST", true);

		}

		//Draw edge
		for (String name : world.getAgents().keySet()) {
			SystemAgent a = world.getAgents().get(name);

			for (Agent target : a.getTargets()) {
				String fullname = name + " " + target.toString();
				if (graph.getEdge(fullname) == null) {
					graph.addEdge(fullname, a.getName(), target.getName(), true);				
				}
				graph.getEdge(fullname).addAttribute("EXIST", true);
				

				if (a instanceof Context) {
					if ( ((Context) a).checkRanges() == true) {
						graph.getEdge(fullname).addAttribute("ui.class", "validContext");
					}
					else {
						graph.getEdge(fullname).removeAttribute("ui.class");
					}
				}
			}

		}
		
		for (Node node : graph) {
			if (node.hasAttribute("EXIST")) {
				node.removeAttribute("EXIST");
			} else {
				graph.removeNode(node);
			}
		}
		
		for (Edge edge : graph.getEachEdge()) {
			if (edge.hasAttribute("EXIST")) {
				edge.removeAttribute("EXIST");
			} else {
				graph.removeEdge(edge);
			}
		}
		
	}
	
	
	private void createGraph() {
		System.out.println("Create graph system");
		graph = new SingleGraph("SYSTEM");
		for (String name : world.getAgents().keySet()) {
			SystemAgent a = world.getAgents().get(name);
			graph.addAttribute("ui.stylesheet", "url('file:/home/nigon/git/CampusMind/CampusMind/src/styles/styleSystem.css')");
			
		/*	graph.addAttribute("ui.stylesheet", "node { stroke-mode: plain;"
					+ "fill-color: red;"
					+ "shape: box;"
					+ "stroke-color: yellow;"
					+ " }");//text-mode
			graph.addAttribute("ui.style", "shape: box;");*/

			//graph.addAttribute("ui.stylesheet", "node { text-mode: normal; }");//text-mode

			//graph.
			graph.addNode(a.getName());
			graph.getNode(a.getName()).addAttribute("ui.class", a.getClass().getSimpleName());
			graph.getNode(a.getName()).addAttribute("ui.label", a.getName());
			//graph.getNode(bba.getName()).;
			//System.out.println(graph.getNode(bba.getName()).getLabe)());
			
		}

		//Draw edge
		for (String name : world.getAgents().keySet()) {
			SystemAgent a = world.getAgents().get(name);

			for (Agent target : a.getTargets()) {
				graph.addEdge(a.getName() + " " + target.toString(), a.getName(), target.getName(), true);				
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

	public void newAgent(Agent a) {
		// TODO Auto-generated method stub
		
	}
}
