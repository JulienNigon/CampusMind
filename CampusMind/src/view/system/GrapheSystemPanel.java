package view.system;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import kernel.Config;
import kernel.World;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.Viewer;
import org.graphstream.ui.swingViewer.ViewerListener;
import org.graphstream.ui.swingViewer.ViewerPipe;

import view.system.paving.Panel1DPaving;
import agents.Agent;
import agents.SystemAgent;
import agents.Percept;
import agents.context.Context;
import agents.criterion.Criterion;
import blackbox.BlackBox;
import blackbox.BlackBoxAgent;
import blackbox.Input;

public class GrapheSystemPanel extends JPanel implements ViewerListener, MouseInputListener{
	
	Graph graph;
	Viewer viewer;
	World world;
	
	/* ----Interaction with system----*/
	ViewerPipe pipe;
	Boolean rightClick = false;

	/* ----ToolBar Components----*/
	private JToolBar toolBar;
	private JButton buttonShowValue;
	private JButton buttonShowDefault;
	private JButton buttonShowName;
	private JButton buttonDestroyContext;
	private JButton buttonSoftStyle;
	private JButton buttonStandardStyle;
	private JButton buttonDarkStyle;
	private JButton buttonEnableAutoLayout;
	private JButton buttonDisableAutoLayout;

	private int viewMode = 0;
	private MouseEvent mouseEvent;


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
		
		buttonSoftStyle = new JButton(Config.getIcon("flag-white.png"));
		buttonSoftStyle.addActionListener(e -> {setSoftStyle();});
		toolBar.add(buttonSoftStyle);
		
		buttonStandardStyle = new JButton(Config.getIcon("flag-green.png"));
		buttonStandardStyle.addActionListener(e -> {setStandardStyle();});
		toolBar.add(buttonStandardStyle);
		
		buttonDarkStyle = new JButton(Config.getIcon("flag-black.png"));
		buttonDarkStyle.addActionListener(e -> {setDarkStyle();});
		buttonDarkStyle.setToolTipText("Switch to dark style.");
		toolBar.add(buttonDarkStyle);
		
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
	
	public void setDarkStyle() {
		graph.removeAttribute("ui.stylesheet");
		graph.addAttribute("ui.stylesheet", "url('file:/home/nigon/git/CampusMind/CampusMind/src/styles/styleSystemDark.css')");
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
						graph.getEdge(fullname).addAttribute("layout.weight", 0.5);
					}
					else {
						graph.getEdge(fullname).removeAttribute("ui.class");
						graph.getEdge(fullname).addAttribute("layout.weight", 1);
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

		/*If we want to improve perf on display...*/
		//graph.addAttribute("layout.stabilization-limit", 0.85);
		
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
		
		if (rightClick) {
			if (world.getAgents().get(id) instanceof Criterion) {
				popupCriterion(id);
			} else if (world.getAgents().get(id) instanceof Percept) {
				popupPercept(id);
			}
			
			rightClick = false;
		}


		
	}
	
	private void startPanelCriterion(String id) {
		PanelCriterion pan = new PanelCriterion((Criterion) world.getAgents().get(id), world);
		JFrame frame = new JFrame(id);
		world.getScheduler().addScheduledItem(pan);
        frame.setAlwaysOnTop(true);
		frame.setContentPane(pan);
		frame.setVisible(true);
		frame.pack();
	}
	
	private void startPanel1DPaving(String id) {
		Panel1DPaving pan = new Panel1DPaving((Percept) world.getAgents().get(id), world);
		JFrame frame = new JFrame(id);
		world.getScheduler().addScheduledItem(pan);
        frame.setAlwaysOnTop(true);
		frame.setContentPane(new JScrollPane(pan));
		frame.setVisible(true);
		frame.pack();
	}
	
	public void popupCriterion(String id){
		
		JPopupMenu popup = new JPopupMenu("Criterion");
		JMenuItem itemChartCriterion = new JMenuItem("Show charts");
		itemChartCriterion.addActionListener(e -> {startPanelCriterion(id);});
		itemChartCriterion.setIcon(Config.getIcon("pencil.png"));
		popup.add(itemChartCriterion);
		
		popup.show(this, this.getX() + mouseEvent.getX(), this.getY() + mouseEvent.getY());
	}
	
	public void popupPercept(String id){
					
		JPopupMenu popup = new JPopupMenu("Percept");
		JMenuItem itemShow1DPaving = new JMenuItem("Show 1D paving");
		itemShow1DPaving.addActionListener(e -> {startPanel1DPaving(id);});
		itemShow1DPaving.setIcon(Config.getIcon("pencil.png"));
		popup.add(itemShow1DPaving);
		
		popup.show(this, this.getX() + mouseEvent.getX(), this.getY() + mouseEvent.getY());
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
		mouseEvent = e;
		if(SwingUtilities.isRightMouseButton(e)){
			rightClick = true;
			Robot bot;
			try {
				bot = new Robot();
				int mask = InputEvent.BUTTON1_DOWN_MASK;
				bot.mousePress(mask);  
				bot.mouseRelease(mask);  
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
   
		}
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
