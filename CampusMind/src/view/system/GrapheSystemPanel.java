package view.system;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.MouseInputListener;

import kernel.Config;
import kernel.World;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import agents.Agent;
import agents.SystemAgent;
import agents.context.Context;
import agents.criterion.Criterion;
import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import blackbox.Input;

public class GrapheSystemPanel extends JPanel implements ViewerListener, MouseInputListener{
	
	Graph graph;
	Viewer viewer;
	World world;
	ViewerPipe pipe;
	
	/* ----ToolBar Components----*/
	private JToolBar toolBar;
	private JButton buttonShowValue;
	private JButton buttonShowDefault;
	private JButton buttonShowName;
	private JButton buttonDestroyContext;
	private JButton buttonSoftStyle;
	private JButton buttonStandardStyle;
	private JButton buttonEnableAutoLayout;
	private JButton buttonDisableAutoLayout;

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
		
		buttonEnableAutoLayout = new JButton(Config.getIcon("node-select-all.png"));
		buttonEnableAutoLayout.addActionListener(e -> {enableAutoLayout();});
		buttonEnableAutoLayout.setToolTipText("Enable auto layout.");
		toolBar.add(buttonEnableAutoLayout);
		
		buttonDisableAutoLayout = new JButton(Config.getIcon("node.png"));
		buttonDisableAutoLayout.addActionListener(e -> {disableAutoLayout();});
		buttonDisableAutoLayout.setToolTipText("Disable auto layout.");
		toolBar.add(buttonDisableAutoLayout);
		
		this.add(toolBar,BorderLayout.WEST);

		
		//ViewerListener
		//update();
		
	}
	
	public void enableAutoLayout() {
		viewer.enableAutoLayout();
	}
	
	public void disableAutoLayout() {
		viewer.disableAutoLayout();
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
		world.destroy(Context.class);  //TODO
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
			
			if (a instanceof Context) {
				if (((Context)a).getNSelection() > 0) {
					graph.getNode(name).addAttribute("ui.class", "ContextSelected");;
				}
				else {
					graph.getNode(name).addAttribute("ui.class", "Context");;
				}
			
			}
			graph.getNode(name).addAttribute("EXIST", true);

		}

		//Draw edge
		for (String name : world.getAgents().keySet()) {
			SystemAgent a = world.getAgents().get(name);

			for (Agent target : a.getTargets()) {
				String fullname = name + " " + target.getName();
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
				graph.addEdge(a.getName() + " " + target.getName(), a.getName(), target.getName(), true);				
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
		viewer.getDefaultView().addMouseListener(this);
		

		pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        pipe.addSink(graph);

		viewer.getDefaultView().setMinimumSize(new Dimension(400,400));
		this.add(viewer.getDefaultView(),BorderLayout.CENTER);
	}

	public void newAgent(Agent a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonPushed(String id) {
		System.out.println("node pushed : " + id);
		
		System.out.println(world.getAgents().get(id).toString());
		
		if (world.getAgents().get(id) instanceof Criterion) {
			PanelCriterion pan = new PanelCriterion((Criterion) world.getAgents().get(id));
			JFrame frame = new JFrame(id);
			world.getScheduler().addScheduledItem(pan);
			frame.setContentPane(pan);
			frame.setVisible(true);
			frame.pack();
		}

		
	}

	@Override
	public void buttonReleased(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewClosed(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("click");
		pipe.pump();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
